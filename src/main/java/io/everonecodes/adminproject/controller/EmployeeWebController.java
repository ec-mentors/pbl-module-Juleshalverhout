package io.everonecodes.adminproject.controller;

import io.everonecodes.adminproject.model.Employee;
import io.everonecodes.adminproject.repository.EmployeeRepository;
import io.everonecodes.adminproject.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/employees")
public class EmployeeWebController {

    private final EmployeeRepository employeeRepository;
    private final AdminService adminService;

    public EmployeeWebController(EmployeeRepository employeeRepository, AdminService adminService) {
        this.employeeRepository = employeeRepository;
        this.adminService = adminService;
    }

    @GetMapping
    public String showAllEmployees(Model model) {
        List<Employee> allEmployees = employeeRepository.findAll();
        model.addAttribute("employees", allEmployees);
        return "employees";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("pageTitle", "Create New Employee");
        return "employee-form";
    }

    @PostMapping("/save")
    public String saveEmployee(Employee employee) {
        employeeRepository.save(employee);
        return "redirect:/employees";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);

        if (optionalEmployee.isPresent()) {
            model.addAttribute("employee", optionalEmployee.get());
            model.addAttribute("pageTitle", "Edit Employee (ID: " + id + ")");
            return "employee-form";
        }
        return "redirect:/employees";
    }

    @PostMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        adminService.deleteEmployeeById(id);
        return "redirect:/employees";

    }
}
