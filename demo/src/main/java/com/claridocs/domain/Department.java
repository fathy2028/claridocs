package com.claridocs.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter @NoArgsConstructor
@Entity
@Table(name = "departments",
       uniqueConstraints = @UniqueConstraint(name = "uq_departments_name", columnNames = "name"))
public class Department extends BaseEntity {

    @NotBlank
    @Column(nullable = false, length = 120)
    private String name;

    @Column(length = 500)
    private String description;
}
