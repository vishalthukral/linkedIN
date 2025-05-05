package com.linkedin_clone_application.service;

import com.linkedin_clone_application.model.Job;
import com.linkedin_clone_application.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JobServiceImpl implements JobService{

    @Autowired
    private JobRepository jobRepository;

    // Create or update a job
    public Job saveJob(Job job) {
        return jobRepository.save(job);
    }

    // Get a job by ID
    public Optional<Job> getJobById(int id) {
        return jobRepository.findById(id);
    }

    // Get all jobs
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    // Get jobs by company ID
//    public List<Job> getJobsByCompanyId(int companyId) {
//        return jobRepository.findByCompanyId(companyId);
//    }

    // Get jobs matching user's interested roles
    public List<Job> getJobsByInterestedRoles(List<String> interestedRoles) {
        return jobRepository.findByJobTitleIn(interestedRoles);
    }

    // Search jobs by tag or description
    @Override
    public List<Job> searchJobs(String jobTitle, String jobDescription) {
        return jobRepository.findByJobTitleContainingOrJobDescriptionContaining(jobTitle, jobDescription);
    }

    // Delete a job by ID
    public void deleteJobById(int id) {
        jobRepository.deleteById(id);
    }

    @Override
    public Job findById(int id) {
        return jobRepository.findById(id).get();
    }

    @Override
    @Transactional
    public Job updateJob(Job job) {
        Job existingJob = jobRepository.findById(job.getId()).get();
        existingJob.setJobTitle(job.getJobTitle());
        existingJob.setJobDescription(job.getJobDescription());
        existingJob.setCompany(job.getCompany());
        existingJob.setWorkPlaceType(job.getWorkPlaceType());
        existingJob.setLocation(job.getLocation());
        existingJob.setEmploymentType(job.getEmploymentType());
        existingJob.setJobDescription(job.getJobDescription());
        existingJob.setUpdatedAt(LocalDateTime.now());
        return jobRepository.save(existingJob);
    }
    @Override
    public List<Job> searchJobsByKeyword(String keyword) {
        return jobRepository.searchByTitleOrDescription(keyword);
    }
}