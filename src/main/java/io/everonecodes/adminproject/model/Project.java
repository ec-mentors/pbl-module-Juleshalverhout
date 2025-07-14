package io.everonecodes.adminproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long projectId;

    @Column(nullable = false)
    private String projectName;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String location;

    private boolean isAssigned;

    @Column(nullable = false)
    private LocalDateTime creationDate = LocalDateTime.now();

    @ManyToMany(mappedBy = "assignedProjects", fetch = FetchType.LAZY)

    @JsonIgnore
    private Set<Employee> assignedEmployees;
}
