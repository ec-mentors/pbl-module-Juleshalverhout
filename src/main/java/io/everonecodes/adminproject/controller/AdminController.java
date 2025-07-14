package io.everonecodes.adminproject.controller;

import io.everonecodes.adminproject.model.Employee;
import io.everonecodes.adminproject.model.Project;
import io.everonecodes.adminproject.service.AdminService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminController {

    private final AdminService adminService; // Inject the service, NOT repositories

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // --- Employee Endpoints ---

    @PostMapping("/employees")
    public Employee createEmployee(@RequestBody Employee employee) {
        // Delegate to the service
        return adminService.createEmployee(employee);
    }

    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        // Delegate to the service
        return adminService.getAllEmployees();
    }

    // --- Project Endpoints ---

    @PostMapping("/projects")
    public Project createProject(@RequestBody Project project) {
        // Delegate to the service
        return adminService.createProject(project);
    }

    @GetMapping("/projects")
    public List<Project> getAllProjects() {
        // Delegate to the service
        return adminService.getAllProjects();
    }

    // --- Relationship Endpoint ---

    @PostMapping("/employees/{employeeId}/projects/{projectId}")
    public Employee assignProjectToEmployee(@PathVariable Long employeeId, @PathVariable Long projectId) {
        // Delegate to the service
        return adminService.assignProjectToEmployee(employeeId, projectId);
    }
}

