package com.example.jobboard.repositories;

import com.example.jobboard.domain.entities.EmployerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployerRepository extends CrudRepository<EmployerEntity, Long> {

    Optional<EmployerEntity> findByUsername(String username);

}
