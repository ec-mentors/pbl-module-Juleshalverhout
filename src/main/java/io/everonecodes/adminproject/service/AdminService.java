package io.everonecodes.adminproject.service;

import io.everonecodes.adminproject.model.Employee;
import io.everonecodes.adminproject.model.Project;
import io.everonecodes.adminproject.repository.EmployeeRepository;
import io.everonecodes.adminproject.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AdminService {

    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;

    public AdminService(EmployeeRepository employeeRepository, ProjectRepository projectRepository) {
        this.employeeRepository = employeeRepository;
        this.projectRepository = projectRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    // Assign a project to an employee
    @Transactional
    public Employee assignProjectToEmployee(Long employeeId, Long projectId) {

        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);

        if (optionalEmployee.isEmpty()) {

            throw new EntityNotFoundException("Assignment failed: Employee not found with id: " + employeeId);
        }

        Employee employee = optionalEmployee.get();

        Optional<Project> optionalProject = projectRepository.findById(projectId);

        if (optionalProject.isEmpty()) {

            throw new EntityNotFoundException("Assignment failed: Project not found with id: " + projectId);
        }

        Project project = optionalProject.get();

        // --- Step 3: Establish the Relationship in Memory ---
        // At this point, 'employee' and 'project' are Java objects living in the computer's memory.
        // We now modify the 'employee' object by adding the 'project' object to its set of assigned projects.
        // This change has NOT been saved to the database yet. It only exists in memory.
        employee.getAssignedProjects().add(project);


        // --- Step 4: Persist the Changes to the Database ---
        // Now we tell the repository to save the modified 'employee' object.
        // Because 'Employee' is the "owning side" of the Many-to-Many relationship (it has the @JoinTable annotation),
        // JPA is smart. It detects that the 'assignedProjects' collection has changed.
        // It will automatically generate and execute the correct SQL statement to insert a new row
        // into our join table ('employee_project_assignments') to link this employee and project.
        // The method then returns the saved employee, which now reflects the new relationship.
        return employeeRepository.save(employee);
    }

    //Get individual employee by ID
    public Employee getEmployeeById(Long employeeId) {

        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);

        if (optionalEmployee.isEmpty()) {
            throw new EntityNotFoundException("Employee not found with id: " + employeeId);
        }
        return optionalEmployee.get();
    }

    //Get individual project by ID
    public Project getProjectById(Long projectId) {

        Optional<Project> optionalProject = projectRepository.findById(projectId);

        if (optionalProject.isEmpty()) {
            throw new EntityNotFoundException("Project not found with id: " + projectId);
        }
        return optionalProject.get();
    }

    //Delete individual project by ID
    @Transactional
    public void deleteProjectById(Long projectId) {
        Optional<Project> projectToDelete = projectRepository.findById(projectId);

        if (projectToDelete.isEmpty()) {
            throw new EntityNotFoundException("Project not found with id: " + projectId);
        } else {

            for (Employee employee : projectToDelete.get().getAssignedEmployees()) {
                employee.getAssignedProjects().remove(projectToDelete);
            }
            projectRepository.delete(projectToDelete.get());
        }
    }

    //Update individual project by ID
    @Transactional
    public Project updateProjectById(Long projectId, Project updatedProjectDetails) {
        Project existingProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + projectId));

        existingProject.setProjectName(updatedProjectDetails.getProjectName());
        existingProject.setDescription(updatedProjectDetails.getDescription());
        existingProject.setLocation(updatedProjectDetails.getLocation());
        existingProject.setAssigned(updatedProjectDetails.isAssigned());

        return projectRepository.save(existingProject);
    }

    @Transactional
    public void unassignEmployeeFromProject(Long employeeId, Long projectId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EntityNotFoundException("Employee not found")); // might replace with isEmpty check
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("Project not found"));

        employee.getAssignedProjects().remove(project);
    }

    @Transactional
    public void deleteEmployeeById(Long employeeId) {
        Employee employeeToDelete = employeeRepository.findById(employeeId).orElseThrow(() -> new EntityNotFoundException("Cannot delete: Employee not found with id: " + employeeId));

        Set<Project> projectsToUnassign = employeeToDelete.getAssignedProjects();

        for (Project project : new HashSet<>(projectsToUnassign)) {

            project.getAssignedEmployees().remove(employeeToDelete);
        }
        employeeToDelete.getAssignedProjects().clear();
        employeeRepository.delete(employeeToDelete);
    }
}

