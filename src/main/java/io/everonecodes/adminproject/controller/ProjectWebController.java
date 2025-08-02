package io.everonecodes.adminproject.controller;

import io.everonecodes.adminproject.model.Employee;
import io.everonecodes.adminproject.model.Project;
import io.everonecodes.adminproject.repository.ProjectRepository;
import io.everonecodes.adminproject.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class ProjectWebController {

    private final ProjectRepository projectRepository;
    private final AdminService adminService;

    public ProjectWebController(ProjectRepository projectRepository, AdminService adminService) {
        this.projectRepository = projectRepository;
        this.adminService = adminService;
    }

    @GetMapping("/")
    public String showLandingPage(Model model) {
        List<Project> unfinishedProjects = projectRepository.findByIsFinishedFalse();
        model.addAttribute("projects", unfinishedProjects);
        return "index";
    }

    @GetMapping("/projects")
    public String showAllProjects(Model model) {
        List<Project> allProjects = projectRepository.findAll();
        model.addAttribute("projects", allProjects);
        return "projects";
    }

    @GetMapping("/projects/new")
    public String showCreateForm(Model model) {
        model.addAttribute("project", new Project());
        model.addAttribute("pageTitle", "Create New Project");
        return "project-form";
    }

    @PostMapping("projects/save")
    public String saveProject(Project project) {
        projectRepository.save(project);
        return "redirect:/projects";
    }

    @GetMapping("/projects/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        if (optionalProject.isPresent()) {
            model.addAttribute("project", optionalProject.get());
            model.addAttribute("pageTitle", "Edit Project (ID: " + id + ")");
            return "project-form";
        }

        return "redirect:/projects";
    }

    @PostMapping("/projects/delete/{id}")
    public String deleteProject(@PathVariable Long id) {
        projectRepository.deleteById(id);
        return "redirect:/projects";
    }

    @GetMapping("/projects/details/{projectId}")
    public String showProjectDetails(@PathVariable Long projectId, Model model) {
        // Get the project we want to display
        Project project = adminService.getProjectById(projectId);
        // Get a list of all employees to populate the "assign" dropdown
        List<Employee> allEmployees = adminService.getAllEmployees();

        model.addAttribute("project", project);
        model.addAttribute("allEmployees", allEmployees);

        return "project-details"; // This will look for a new file "project-details.html"
    }

    // --- ADD THIS NEW METHOD to handle the 'Assign Employee' form submission ---
    @PostMapping("/projects/{projectId}/assignEmployee")
    public String assignEmployee(@PathVariable Long projectId, @RequestParam Long employeeId) {
        // @RequestParam grabs the 'employeeId' value from the form submission
        adminService.assignProjectToEmployee(employeeId, projectId);
        return "redirect:/projects/details/" + projectId; // Redirect back to the details page
    }

    // --- ADD THIS NEW METHOD to handle the 'Unassign' action ---
    @PostMapping("/projects/{projectId}/unassignEmployee/{employeeId}")
    public String unassignEmployee(@PathVariable Long projectId, @PathVariable Long employeeId) {
        adminService.unassignEmployeeFromProject(employeeId, projectId);
        return "redirect:/projects/details/" + projectId; // Redirect back to the details page
    }
}
