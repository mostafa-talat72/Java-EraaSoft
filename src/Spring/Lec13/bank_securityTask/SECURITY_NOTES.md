# Bank Security — Security Documentation (Tasks 37, 38, 39, 40)

## Task 37 — Role-Based vs Permission-Based Authorization

Both approaches are implemented and available via method security:

| Approach | Example | Used For |
|---|---:|---|
| Role-based | `@PreAuthorize("hasRole('ADMIN')")` | High-level, coarse access control |
| Permission-based | `@PreAuthorize("hasAuthority('TRANSFER_CREATE')")` | Fine-grained, operation-level control |

### When to use each

**Use ROLES when:**
- The decision is about "who" the user is organizationally (identity tier), not a single operation.
  - Example: blocking/unblocking customers is an ADMIN-only duty → `hasRole('ADMIN')` on `CustomerService.blockCustomerById(...)`.
- Anything under `/admin/**` is guarded at the URL level with `hasRole("ADMIN")` and at method level.
- The set of allowed operations is large and would otherwise require listing many permissions.

**Use PERMISSIONS when:**
- The decision is about one specific operation, independent of which role holds it.
  - Example: approving a transfer (`TRANSFER_APPROVE`) is held by BOTH `ADMIN` and `BANK_MANAGER` — checking the permission lets both pass without role duplication.
- You expect the role-to-operation mapping to evolve (add/revoke one permission instead of rewriting role logic).
- The operation is narrow and sensitive (e.g. `ACCOUNT_DELETE` is ADMIN-only even though ADMIN holds other rights).

**Rule of thumb:** *Roles = grouping of a user's nature. Permissions = grouping of what the user may execute. A role is a bag of permissions; authorization checks should nearly always test the permission, and test the role only when the check is inherently organizational.*

## Task 38 — Security Rules Matrix

### The matrix

| Operation        | ADMIN | BANK_MANAGER | CUSTOMER  | AUDITOR |
| ---------------- | :---: | :----------: | :-------: | :-----: |
| Create Customer  | ✅    | ❌           | ❌        | ❌      |
| Read Customer    | ✅    | ✅           | ❌        | ❌      |
| Update Customer  | ✅    | ✅           | ❌        | ❌      |
| Create Account   | ✅    | ❌           | ❌        | ❌      |
| Read Account     | ✅    | ✅           | Own only  | ✅      |
| Update Account   | ✅    | ✅           | ❌        | ❌      |
| Delete Account   | ✅    | ❌           | ❌        | ❌      |
| Create Transfer  | ✅    | ❌           | ✅        | ❌      |
| Read Transfer    | ✅    | ✅           | Own only  | ✅      |
| Approve Transfer | ✅    | ✅           | ❌        | ❌      |
| View Reports     | ✅    | ❌           | ❌        | ✅      |

### Materialized grants (seeded by `SecurityMatrixSeeder`, idempotent on boot)

- **ADMIN** → `ACCOUNT_READ, ACCOUNT_CREATE, ACCOUNT_UPDATE, ACCOUNT_DELETE, TRANSFER_CREATE, TRANSFER_READ, TRANSFER_APPROVE, CUSTOMER_READ, CUSTOMER_UPDATE, REPORT_VIEW`
- **BANK_MANAGER** → `CUSTOMER_READ, CUSTOMER_UPDATE, ACCOUNT_READ, ACCOUNT_UPDATE, TRANSFER_READ, TRANSFER_APPROVE`
- **CUSTOMER** → `ACCOUNT_READ, TRANSFER_CREATE, TRANSFER_READ`
- **AUDITOR** → `ACCOUNT_READ, TRANSFER_READ, REPORT_VIEW`

### How each row is enforced (defense in depth)

| Enforcement layer | Location |
|---|---|
| URL rules | `SecurityConfig.authorizeHttpRequests` — `/admin/**` → `hasRole("ADMIN")`, `/reports/**`, `/accounts/**`, `/transfers/**`, `/customers/**` → `authenticated()` |
| Permissions (service level) | `@PreAuthorize` on `AccountService`, `TransferService`, `CustomerService`, `RoleService`, `PermissionService` interfaces |
| Ownership ("Own only") | `AccountServiceImpl.getAccountById` (principal account-id match; ADMIN/MANAGER/AUDITOR bypass) and `TransferServiceImpl.getTransferById` (principal creator-email match; ADMIN/MANAGER/AUDITOR bypass). Identity always comes from the SECURITY CONTEXT (`customUserDetails.getUsername()`), never from the request body |
| "View Reports" | `/reports/**` + `hasAuthority('REPORT_VIEW')` → only ADMIN and AUDITOR hold it |

Notes:
- "Create Customer" in the matrix refers to administrative customer creation; `/auth/register` remains the public self-signup endpoint (Task 39 Scenario 1 proves role hijacking is neutralized there).
- Ownership rows (Read Account/Read Transfer for CUSTOMER) are enforced only for non-privileged callers; ADMIN, BANK_MANAGER and AUDITOR see any record, matching the ✅/Own-only cells above.

## Task 39 — Security Attack Scenarios

| # | Scenario | Attack Vector | Blocked By | Expected |
|---|---|---|---|---|
| 1 | `ROLE_ADMIN` sent during registration | Bogus `role` field in body | `CustomerRequestDTO` has no `role`/`roles` field (Jackson ignores unknown JSON); role is always assigned server-side via `RoleService.findByName("CUSTOMER")` | 201 Created, and `/auth/me` shows `roles = ["CUSTOMER"]` only |
| 2 | Customer reads `/accounts/20` (other customer) | IDOR | Ownership check in `getAccountById` + `AccessDeniedException` → filter-level handler returns 403 JSON | 403 |
| 3 | Customer calls `POST /transfers/10/approve` | Privilege escalation | `hasAuthority('TRANSFER_APPROVE')` on `TransferService.approveTransferById`; CUSTOMER does not hold the permission | 403 |
| 4 | Unauthenticated `GET /accounts/10` | Missing credentials | URL rule `authenticated()` + custom 401 entry point | 401 JSON (timestamp/status/error/message/path) |
| 5 | Another user's email in request body | Identity spoofing | Ownership and source-account logic never reads email/identity from the body; source account is the principal's own (`customerService.getCustomerEntityByEmail(customUserDetails.getUsername())`), body email is ignored | Body values cannot override ownership |

## Task 40 — The Complete Security Flow

```text
Client
   ↓  HTTP Request (Basic Auth header)
SecurityFilterChain
   ↓
Authentication (BasicAuthenticationFilter extracts credentials)
   ↓
AuthenticationManager (ProviderManager)
   ↓
BankAuthenticationProvider
   ↓
CustomerService / Repository
   ↓
PasswordEncoder
   ↓
CustomUserDetails
   ↓
Roles → Permissions → GrantedAuthority
   ↓
SecurityContext
   ↓
Authorization (AuthorizationFilter + @PreAuthorize)
   ↓
Controller → Service → Database
   ↓
Response (401/403 JSON on denial, 200/4xx/5xx on normal flow)
```

### Component responsibilities

| Component | Responsibility |
|---|---|
| Client | Sends HTTP request + `Authorization: Basic base64(email:password)` |
| SecurityFilterChain | Boot-web-wired filter chain: URL rules, CSRF off (stateless API), httpBasic, exception handling (custom 401/403) |
| Authentication (extractor) | `BasicAuthenticationFilter` parses the header into `UsernamePasswordAuthenticationToken` (unauthenticated) |
| AuthenticationManager | `ProviderManager`: picks a provider that `supports(...)` the token type, applies its decision, or propagates failure |
| BankAuthenticationProvider | Business authentication: loads the customer, validates password/state (disabled → `DisabledException`, blocked → `LockedException`, closed → `AccountExpiredException`), assembles roles+permissions into authorities. Returns `CustomUserDetails` as its principal |
| CustomerService / Repository | Data access — looks the customer up by email (also used by `CustomUserDetailsService`) |
| PasswordEncoder | `BCryptPasswordEncoder`; `matches(raw, hash)` — hashes are never compared in plain text and are never returned to clients |
| CustomUserDetails | `UserDetails` implementation wrapping `CustomerDTO`; exposes username (email), password, `isEnabled()`, and the `GrantedAuthority` collection |
| Roles | `ROLE_*` authorities — coarse organizational identity (e.g. `ROLE_ADMIN`) |
| Permissions | Fine-grained authorities (e.g. `TRANSFER_APPROVE`) |
| GrantedAuthority | The unified `ROLE_*`/`*` strings that both URL rules and `@PreAuthorize` evaluate |
| SecurityContext | Stores the authenticated token for the life of the request (stateless: no session persistence; every request re-authenticates) |
| Authorization | Two gates in order: URL rules (`AuthorizationFilter`, filter level) then method security (`@PreAuthorize`, AOP around controller/service methods) |
| Controller | Delegates validated/authorized work to services; never touches entities or repos directly |
| Service | Business rules, ownership checks, transactional money movement; returns DTOs entities are mapped to |
| Database | Oracle; JPA repositories; schema via `ddl-auto: update`, grants via `SecurityMatrixSeeder` |
| Response | Success → 200/201 + DTO; denied → 401/403 JSON from `/error`; business errors → 400 JSON; unknown fault → 500 JSON |

## Final Deliverables — Where Everything Lives

| # | Deliverable | Location |
|---|---|---|
| 1 | Complete project | `src/.../bank_securityTask` (whole tree) |
| 2 | Database SQL script | Oracle schema auto-managed (`application.yaml` + entities); seed logic in `SecurityMatrixSeeder` |
| 3 | Postman collection | `postman_collection.json` (project root — Tasks 36/38/39 folders with test scripts) |
| 4 | Customer auth implementation | `BankAuthenticationProvider`, `SecurityConfig`, `AuthController` |
| 5 | Custom `UserDetails` | `com.bank_security.helper.CustomUserDetails` |
| 6 | Custom `UserDetailsService` | `com.bank_security.service.impl.CustomUserDetailsService` |
| 7 | Custom `AuthenticationProvider` | `com.bank_security.service.provider.BankAuthenticationProvider` |
| 8 | `PasswordEncoder` | `SecurityConfig.passwordEncoder()` (BCrypt) |
| 9 | Role implementation | `model.Role`, `repo.RoleRepo`, `service.RoleService(+Impl)`, `controller.RoleController` |
| 10 | Permission implementation | `model.Permission`, `repo.PermissionRepo`, `service.PermissionService(+Impl)`, `controller.PermissionController` |
| 11 | SecurityFilterChain | `config.SecurityConfig.securityFilterChain` |
| 12 | Method-level security | `@PreAuthorize` on service interfaces + `@EnableMethodSecurity` |
| 13 | Custom 401 handler | `SecurityConfig` exceptionHandling entry point → Boot `/error` JSON |
| 14 | Custom 403 handler | `SecurityConfig` exceptionHandling accessDeniedHandler → Boot `/error` JSON |
| 15 | Account ownership security | `AccountServiceImpl.getAccountById` / `getMyAccount` |
| 16 | Transfer authorization | `TransferServiceImpl` (create/own-only read/approve) |
| 17 | Admin management APIs | `RoleController`, `PermissionController`, admin endpoints in `CustomerController`, `POST /accounts` |
| 18 | Security test cases | `postman_collection.json` + matrix in this document |
| 19 | Security rules matrix | Section "Task 38" above + `SecurityMatrixSeeder` |
| 20 | Auth & authz flow documentation | Section "Task 40" above |
