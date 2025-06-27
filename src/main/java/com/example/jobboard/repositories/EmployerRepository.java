package com.example.jobboard.repositories;

import com.example.jobboard.domain.entities.EmployerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployerRepository extends CrudRepository<EmployerEntity, Long> {
}
