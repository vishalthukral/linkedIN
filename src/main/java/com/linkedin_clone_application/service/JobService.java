package com.linkedin_clone_application.service;

import com.linkedin_clone_application.model.Job;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface JobService {
    Job saveJob(Job job);

    Optional<Job> getJobById(int id);

    List<Job> getAllJobs();

    List<Job> getJobsByInterestedRoles(List<String> interestedRoles);

    List<Job> searchJobs(String jobTitle, String jobDescription);

    List<Job> searchJobsByKeyword(String keyword);

    void deleteJobById(int id);

    Job findById(int id);

    Job updateJob(Job job);
}