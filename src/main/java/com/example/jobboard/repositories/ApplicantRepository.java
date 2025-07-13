package com.example.jobboard.repositories;

import com.example.jobboard.domain.entities.ApplicantEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicantRepository extends CrudRepository<ApplicantEntity, Long> {

    Optional<ApplicantEntity> findByUsername(String username);

}
