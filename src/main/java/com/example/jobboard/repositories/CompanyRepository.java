package com.example.jobboard.repositories;

import com.example.jobboard.domain.entities.CompanyEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends CrudRepository<CompanyEntity, Long> {
    Optional<CompanyEntity> findByName(String name);
}
