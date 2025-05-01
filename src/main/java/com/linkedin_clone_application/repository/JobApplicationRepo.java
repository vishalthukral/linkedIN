package com.linkedin_clone_application.repository;

import com.linkedin_clone_application.model.Job;
import com.linkedin_clone_application.model.JobApplication;
import com.linkedin_clone_application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplicationRepo extends JpaRepository<JobApplication, Integer> {
    JobApplication findByJobAndUser(Job job, User user);
    List<JobApplication> findByUser(User user);
    List<JobApplication> findByJob(Job job);
}