package com.example.jobboard.repositories;

import com.example.jobboard.domain.entities.ApplicantEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantRepository extends CrudRepository<ApplicantEntity, Long> {
}
