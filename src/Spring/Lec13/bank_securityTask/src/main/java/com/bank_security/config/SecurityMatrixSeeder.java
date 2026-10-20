package com.bank_security.config;

import com.bank_security.model.Permission;
import com.bank_security.model.Role;
import com.bank_security.repo.PermissionRepo;
import com.bank_security.repo.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Task 38 security matrix, materialized as role -> permission grants.
// Idempotent: existing names are reused, missing ones are created, and
// each role's permission set is converged to the matrix on every boot.
// Runs after JPA schema creation (CommandLineRunner), before serving traffic.
@Component
public class SecurityMatrixSeeder implements CommandLineRunner {

    private static final List<String> ALL_PERMISSIONS = List.of(
            "ACCOUNT_READ",
            "ACCOUNT_CREATE",
            "ACCOUNT_UPDATE",
            "ACCOUNT_DELETE",
            "TRANSFER_CREATE",
            "TRANSFER_READ",
            "TRANSFER_APPROVE",
            "CUSTOMER_READ",
            "CUSTOMER_UPDATE",
            "REPORT_VIEW"
    );

    private static final Map<String, List<String>> ROLE_PERMISSIONS = buildMatrix();

    private static Map<String, List<String>> buildMatrix() {
        Map<String, List<String>> matrix = new LinkedHashMap<>();

        // Create/Read/Update/Delete/Transfer/Reports: ADMIN sees everything.
        matrix.put("ADMIN", ALL_PERMISSIONS);

        // Manager: full read/update supervision, approval rights, no creation/deletion.
        matrix.put("BANK_MANAGER", List.of(
                "CUSTOMER_READ",
                "CUSTOMER_UPDATE",
                "ACCOUNT_READ",
                "ACCOUNT_UPDATE",
                "TRANSFER_READ",
                "TRANSFER_APPROVE"
        ));

        // Customer: read own account, create own transfers, read own transfers.
        matrix.put("CUSTOMER", List.of(
                "ACCOUNT_READ",
                "TRANSFER_CREATE",
                "TRANSFER_READ"
        ));

        // Auditor: read-only visibility plus reports.
        matrix.put("AUDITOR", List.of(
                "ACCOUNT_READ",
                "TRANSFER_READ",
                "REPORT_VIEW"
        ));

        return matrix;
    }

    private final RoleRepo roleRepo;

    private final PermissionRepo permissionRepo;

    @Autowired
    public SecurityMatrixSeeder(RoleRepo roleRepo, PermissionRepo permissionRepo) {
        this.roleRepo = roleRepo;
        this.permissionRepo = permissionRepo;
    }

    @Override
    public void run(String... args) {

        // Step 1: make sure every matrix permission exists.
        Map<String, Permission> permissions = new LinkedHashMap<>();
        for (String permissionName : ALL_PERMISSIONS) {
            Permission permission = permissionRepo.findByName(permissionName)
                    .orElseGet(() -> {
                        Permission created = new Permission();
                        created.setName(permissionName);
                        return permissionRepo.save(created);
                    });
            permissions.put(permissionName, permission);
        }

        // Step 2: make sure every matrix role exists with exactly its grants.
        for (Map.Entry<String, List<String>> matrixRow : ROLE_PERMISSIONS.entrySet()) {
            Role role = roleRepo.findByName(matrixRow.getKey())
                    .orElseGet(() -> roleRepo.save(new Role()));
            role.setName(matrixRow.getKey());

            List<Permission> grantedPermissions = new ArrayList<>();
            for (String permissionName : matrixRow.getValue()) {
                grantedPermissions.add(permissions.get(permissionName));
            }
            role.setPermissions(grantedPermissions);

            roleRepo.save(role);
        }
    }
}
