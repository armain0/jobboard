package com.example.jobboard.repositories;

import com.example.jobboard.domain.entities.EmployerEntity;
import com.example.jobboard.domain.entities.JobEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends CrudRepository<JobEntity, Long> {

    List<JobEntity> findByEmployer(EmployerEntity employer);

}
