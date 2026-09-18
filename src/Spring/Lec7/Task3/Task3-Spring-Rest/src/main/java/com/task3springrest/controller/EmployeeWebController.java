package com.task3springrest.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task3springrest.model.Employee;
import com.task3springrest.model.EmployeeListForm;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Thymeleaf pages that talk to the REST API via HTTP (server-side).
 * All /web/employees/** pages call /employees/** REST endpoints via RestTemplate.
 * No direct EmployeeService injection here.
 *
 * REST base: http://localhost:8085/employees (EmployeeController)
 */
@Controller
@RequestMapping("/web/employees")
public class EmployeeWebController {

    private static final String API_BASE = "http://localhost:8085/employees";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public EmployeeWebController(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    // ============ LIST ============
    // GET /web/employees -> employees/list.html (data from GET /employees)
    @GetMapping
    public String list(Model model) {
        try {
            Employee[] arr = restTemplate.getForObject(API_BASE, Employee[].class);
            List<Employee> employees = arr == null ? Collections.emptyList() : Arrays.asList(arr);
            model.addAttribute("employees", employees);
        } catch (Exception e) {
            model.addAttribute("employees", Collections.emptyList());
            model.addAttribute("loadError", "Cannot load employees from REST API: " + e.getMessage());
        }
        return "employees/list";
    }

    // ============ PROFILE ============
    // GET /web/employees/{id} -> employees/details.html (data from GET /employees/{id})
    @GetMapping("/{id:\\d+}")
    public String profile(@PathVariable long id, Model model) {
        try {
            Employee emp = restTemplate.getForObject(API_BASE + "/" + id, Employee.class);
            model.addAttribute("employee", emp);
        } catch (RestClientResponseException e) {
            return "redirect:/web/employees";
        }
        return "employees/details";
    }

    // ============ BY IDS ============
    // GET /web/employees/byIds?ids=1,2,3 -> employees/list.html filtered
    // calls GET /employees/byIds?ids=1,2,3
    @GetMapping("/byIds")
    public String byIds(@RequestParam(required = false) String ids, Model model) {
        if (ids == null || ids.isBlank()) {
            return "redirect:/web/employees";
        }
        try {
            String url = API_BASE + "/byIds?ids=" + ids;
            Employee[] arr = restTemplate.getForObject(url, Employee[].class);
            List<Employee> employees = arr == null ? Collections.emptyList() : Arrays.asList(arr);
            model.addAttribute("employees", employees);
            model.addAttribute("filterInfo", "Filtered by IDs: " + ids);
        } catch (Exception e) {
            model.addAttribute("employees", Collections.emptyList());
            model.addAttribute("loadError", "byIds failed: " + e.getMessage());
        }
        return "employees/list";
    }

    // ============ SEARCH ============
    // GET /web/employees/search?name=ahmed -> employees/search.html
    // calls GET /employees/search/{name} which returns [derived, native, jpql]
    @GetMapping("/search")
    public String search(@RequestParam(required = false) String name, Model model) {
        if (name == null || name.isBlank()) {
            return "redirect:/web/employees";
        }
        try {
            String url = API_BASE + "/search/" + UriComponentsBuilder.fromPath(name).build().toUriString().substring(1);
            // Use array-of-array: List<List<Employee>>
            Employee[][] result = restTemplate.getForObject(API_BASE + "/search/" + name, Employee[][].class);
            if (result != null && result.length == 3) {
                model.addAttribute("derived", result[0] == null ? Collections.emptyList() : Arrays.asList(result[0]));
                model.addAttribute("nativeQ", result[1] == null ? Collections.emptyList() : Arrays.asList(result[1]));
                model.addAttribute("jpql", result[2] == null ? Collections.emptyList() : Arrays.asList(result[2]));
            } else {
                model.addAttribute("derived", Collections.emptyList());
                model.addAttribute("nativeQ", Collections.emptyList());
                model.addAttribute("jpql", Collections.emptyList());
            }
            model.addAttribute("searchName", name);
        } catch (Exception e) {
            model.addAttribute("loadError", "Search failed: " + e.getMessage());
            model.addAttribute("derived", Collections.emptyList());
            model.addAttribute("nativeQ", Collections.emptyList());
            model.addAttribute("jpql", Collections.emptyList());
            model.addAttribute("searchName", name);
        }
        return "employees/search";
    }

    // ============ SINGLE CREATE ============
    @GetMapping("/new")
    public String showCreate(Model model) {
        if (!model.containsAttribute("employee")) {
            model.addAttribute("employee", new Employee());
        }
        return "employees/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("employee") Employee employee,
                         BindingResult result) {
        if (result.hasErrors()) {
            return "employees/form";
        }
        try {
            restTemplate.postForObject(API_BASE, employee, Employee.class);
        } catch (RestClientResponseException e) {
            mapRestErrors(e, result);
            return "employees/form";
        }
        return "redirect:/web/employees";
    }

    // ============ SINGLE EDIT ============
    @GetMapping("/edit/{id}")
    public String showEdit(@PathVariable long id, Model model) {
        try {
            Employee emp = restTemplate.getForObject(API_BASE + "/" + id, Employee.class);
            model.addAttribute("employee", emp);
        } catch (RestClientResponseException e) {
            return "redirect:/web/employees";
        }
        return "employees/form";
    }

    // form sends POST + _method=PUT
    @PutMapping("/{id:\\d+}")
    public String update(@PathVariable long id,
                         @Valid @ModelAttribute("employee") Employee employee,
                         BindingResult result) {
        if (result.hasErrors()) {
            return "employees/form";
        }
        employee.setId(id);
        try {
            restTemplate.put(API_BASE, employee);
        } catch (RestClientResponseException e) {
            mapRestErrors(e, result);
            return "employees/form";
        }
        return "redirect:/web/employees";
    }

    // ============ SINGLE DELETE ============
    // form sends POST + _method=DELETE
    @DeleteMapping("/{id:\\d+}")
    public String deleteOne(@PathVariable long id) {
        restTemplate.delete(API_BASE + "/" + id);
        return "redirect:/web/employees";
    }

    // ============ DELETE ALL ============
    // form sends POST + _method=DELETE to /web/employees
    @DeleteMapping
    public String deleteAll() {
        restTemplate.delete(API_BASE);
        return "redirect:/web/employees";
    }

    // ============ DELETE LIST BY IDS ============
    // form: POST /web/employees/delete/list + _method=DELETE, body: ids=1,2,3 (csv)
    @DeleteMapping("/delete/list")
    public String deleteList(@RequestParam String ids) {
        List<Long> idList = parseIds(ids);
        if (!idList.isEmpty()) {
            // REST expects DELETE /employees/list with body List<Long>
            // RestTemplate.delete() cannot send body, so use exchange()
            org.springframework.http.HttpEntity<List<Long>> entity =
                    new org.springframework.http.HttpEntity<>(idList);
            restTemplate.exchange(API_BASE + "/list",
                    org.springframework.http.HttpMethod.DELETE, entity, Void.class);
        }
        return "redirect:/web/employees";
    }

    // ============ BULK ADD (Add List) ============
    // Shows form with dynamic rows to add multiple employees at once
    @GetMapping("/add-list")
    public String showAddList(Model model) {
        EmployeeListForm form = new EmployeeListForm();
        // start with 2 empty rows, user can add more via JS
        form.getEmployees().add(new Employee());
        form.getEmployees().add(new Employee());
        model.addAttribute("bulkForm", form);
        return "employees/add-list";
    }

    // Handles add-list form: POST /web/employees/add-list -> POST /employees/list via RestTemplate
    @PostMapping("/add-list")
    public String addList(@Valid @ModelAttribute("bulkForm") EmployeeListForm bulkForm,
                          BindingResult result, Model model) {
        // Filter out completely empty rows (user left them blank)
        List<Employee> toSave = bulkForm.getEmployees().stream()
                .filter(e -> !(isBlank(e.getName()) && e.getAge() == null && isBlank(e.getPhoneNumber())))
                .collect(Collectors.toList());

        if (toSave.isEmpty()) {
            result.reject("global", "Add at least one employee");
            return "employees/add-list";
        }

        // If any row has validation error, redisplay (BindingResult has indexed errors like employees[0].name)
        if (result.hasErrors()) {
            // keep only non-empty for redisplay
            bulkForm.setEmployees(toSave);
            return "employees/add-list";
        }

        try {
            restTemplate.postForObject(API_BASE + "/list", toSave, Employee[].class);
        } catch (RestClientResponseException e) {
            result.reject("global", extractError(e));
            bulkForm.setEmployees(toSave);
            return "employees/add-list";
        }
        return "redirect:/web/employees";
    }

    // ============ BULK UPDATE (Update List) ============
    // Shows editable table for selected IDs, or all if no ids chosen
    // e.g. /web/employees/update-list?ids=1,2,3  or  /web/employees/update-list
    @GetMapping("/update-list")
    public String showUpdateList(@RequestParam(required = false) String ids, Model model) {
        List<Employee> employees;
        try {
            if (ids != null && !ids.isBlank()) {
                Employee[] arr = restTemplate.getForObject(API_BASE + "/byIds?ids=" + ids, Employee[].class);
                employees = arr == null ? Collections.emptyList() : Arrays.asList(arr);
                model.addAttribute("filterInfo", "Editing IDs: " + ids);
            } else {
                Employee[] arr = restTemplate.getForObject(API_BASE, Employee[].class);
                employees = arr == null ? Collections.emptyList() : Arrays.asList(arr);
            }
        } catch (Exception e) {
            model.addAttribute("loadError", "Cannot load employees: " + e.getMessage());
            employees = Collections.emptyList();
        }
        EmployeeListForm form = new EmployeeListForm(new java.util.ArrayList<>(employees));
        model.addAttribute("bulkForm", form);
        return "employees/update-list";
    }

    // Handles update-list form: POST + _method=PUT -> PUT /employees/list
    @PutMapping("/update-list")
    public String updateList(@Valid @ModelAttribute("bulkForm") EmployeeListForm bulkForm,
                             BindingResult result, Model model) {
        List<Employee> toUpdate = bulkForm.getEmployees();

        if (toUpdate == null || toUpdate.isEmpty()) {
            result.reject("global", "No employees to update");
            return "employees/update-list";
        }

        if (result.hasErrors()) {
            return "employees/update-list";
        }

        try {
            restTemplate.put(API_BASE + "/list", toUpdate);
        } catch (RestClientResponseException e) {
            result.reject("global", extractError(e));
            return "employees/update-list";
        }
        return "redirect:/web/employees";
    }

    private boolean isBlank(String s) { return s == null || s.isBlank(); }

    // ============ Helpers ============
    private List<Long> parseIds(String csv) {
        if (csv == null || csv.isBlank()) return Collections.emptyList();
        return Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }

    private void mapRestErrors(RestClientResponseException e, BindingResult result) {
        try {
            Map<String, String> body = objectMapper.readValue(
                    e.getResponseBodyAsString(), new TypeReference<>() {});
            body.forEach((field, msg) -> {
                if ("error".equals(field)) {
                    result.reject("global", msg);
                } else {
                    try {
                        result.rejectValue(field, "", msg);
                    } catch (Exception ex) {
                        result.reject("global", field + ": " + msg);
                    }
                }
            });
            if (body.isEmpty()) {
                result.reject("global", "REST API error: " + e.getStatusCode());
            }
        } catch (Exception parseEx) {
            result.reject("global", "REST API error: " + e.getStatusCode() + " - " + extractError(e));
        }
    }

    private String extractError(RestClientResponseException e) {
        String body = e.getResponseBodyAsString();
        if (!body.isBlank()) {
            final String s = body.length() > 500 ? body.substring(0, 500) : body;
            try {
                Map<String, Object> map = objectMapper.readValue(body, new TypeReference<>() {});
                if (map.containsKey("error")) return String.valueOf(map.get("error"));
                return s;
            } catch (Exception ex) {
                return s;
            }
        }
        return e.getStatusCode().toString();
    }
}
