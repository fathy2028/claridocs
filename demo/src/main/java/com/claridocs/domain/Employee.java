package com.claridocs.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor
@Entity
@Table(name = "employees")
public class Employee extends BaseEntity {

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true,
                foreignKey = @ForeignKey(name = "fk_employee_user"))
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_employee_department"))
    private Department department;

    @NotBlank
    @Column(nullable = false, length = 150)
    private String fullName;

    @Column(length = 30)
    private String phone;

    @PastOrPresent
    @Column(nullable = false)
    private LocalDate dateJoined = LocalDate.now();

    @Column(length = 120)
    private String title;
}
