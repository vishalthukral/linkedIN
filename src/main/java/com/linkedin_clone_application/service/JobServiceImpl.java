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

    public Job saveJob(Job job) {
        return jobRepository.save(job);
    }

    public Optional<Job> getJobById(int id) {
        return jobRepository.findById(id);
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public List<Job> getJobsByInterestedRoles(List<String> interestedRoles) {
        return jobRepository.findByJobTitleIn(interestedRoles);
    }

    // Search jobs by tag or description
    @Override
    public List<Job> searchJobs(String jobTitle, String jobDescription) {
        return jobRepository.findByJobTitleContainingOrJobDescriptionContaining(jobTitle, jobDescription);
    }

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