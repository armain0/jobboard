package com.example.jobboard.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "employers")
public class EmployerEntity extends UserEntity {

    @ManyToOne
    @JoinColumn(name = "company_id")
    private CompanyEntity company;

}
