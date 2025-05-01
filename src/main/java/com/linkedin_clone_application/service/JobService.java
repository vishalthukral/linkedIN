package com.linkedin_clone_application.service;

import com.linkedin_clone_application.model.Job;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface JobService {
    Job saveJob(Job job);

    // Get a job by ID
    Optional<Job> getJobById(int id);

    // Get all jobs
    List<Job> getAllJobs();

    // Get jobs by company ID
//    List<Job> getJobsByCompanyId(int companyId);

    // Get jobs matching user's interested roles
    List<Job> getJobsByInterestedRoles(List<String> interestedRoles);

    // Search jobs by tag or description
    List<Job> searchJobs(String jobTitle, String jobDescription);

    // Delete a job by ID
    void deleteJobById(int id);

    Job findById(int id);
}
