package io.everonecodes.adminproject.controller;

import io.everonecodes.adminproject.model.Employee;
import io.everonecodes.adminproject.model.Project;
import io.everonecodes.adminproject.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AdminController {

    private final AdminService adminService; // Inject the service, NOT repositories

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/employees")
    public Employee createEmployee(@RequestBody Employee employee) {
        return adminService.createEmployee(employee);
    }

    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return adminService.getAllEmployees();
    }

    @PostMapping("/projects")
    public Project createProject(@RequestBody Project project) {
        return adminService.createProject(project);
    }

    @GetMapping("/projects")
    public List<Project> getAllProjects() {
        return adminService.getAllProjects();
    }

    @PostMapping("/employees/{employeeId}/projects/{projectId}")
    public Employee assignProjectToEmployee(@PathVariable Long employeeId, @PathVariable Long projectId) {
        return adminService.assignProjectToEmployee(employeeId, projectId);
    }

    @GetMapping("employees/{employeeId}")
    public Employee getEmployeeById(@PathVariable Long employeeId) {
        return adminService.getEmployeeById(employeeId);
    }

    @GetMapping("projects/{projectId}")
    public Project getProjectById(@PathVariable Long projectId) {
        return adminService.getProjectById(projectId);
    }

    @DeleteMapping("/projects/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        adminService.deleteProjectById(projectId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/projects/{projectId}")
    public Project updateProject(@PathVariable Long projectId, @RequestBody Project projectDetails) {
        return adminService.updateProjectById(projectId, projectDetails);
    }
}

