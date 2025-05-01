package com.linkedin_clone_application.repository;

import com.linkedin_clone_application.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepo extends JpaRepository<Job,Integer> {
    // Fetch jobs by company
//    List<Job> findByCompanyId(int companyId);

    // Fetch jobs that match user’s interested job roles
    List<Job> findByJobTitleIn(List<String> interestedRoles);

    // Search jobs by tag or company role
    List<Job> findByJobTitleContainingOrJobDescriptionContaining(String jobTitle, String jobDescription);

}
