package com.example.jobboard.repositories;

import com.example.jobboard.domain.entities.ApplicantEntity;
import com.example.jobboard.domain.entities.ApplicationEntity;
import com.example.jobboard.domain.entities.JobEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends CrudRepository<ApplicationEntity, Long> {

    List<ApplicationEntity> findByJobIn(List<JobEntity> jobList);

    List<ApplicationEntity> findByApplicant(ApplicantEntity applicant);


}
