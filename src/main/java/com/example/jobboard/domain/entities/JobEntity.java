package com.example.jobboard.domain.entities;

import com.example.jobboard.domain.JobStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "jobs")
public class JobEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "job_id_seq")
    @SequenceGenerator(
            name = "job_id_seq",
            sequenceName = "job_id_seq",
            allocationSize = 50
    )
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    @ManyToOne
    @JoinColumn(name = "employer_id")
    private EmployerEntity employer;

    @OneToMany(mappedBy = "job")
    private Set<ApplicationEntity> applications;

}
