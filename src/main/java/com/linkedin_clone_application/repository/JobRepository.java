package com.linkedin_clone_application.repository;

import com.linkedin_clone_application.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job,Integer> {
    List<Job> findByJobTitleIn(List<String> interestedRoles);

    List<Job> findByJobTitleContainingOrJobDescriptionContaining(String jobTitle, String jobDescription);

    @Query("SELECT j FROM Job j WHERE LOWER(j.jobTitle) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(j.jobDescription) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Job> searchByTitleOrDescription(@Param("keyword") String keyword);
}