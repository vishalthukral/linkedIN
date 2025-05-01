package com.linkedin_clone_application.controller;

import com.linkedin_clone_application.model.Job;
import com.linkedin_clone_application.model.User;
import com.linkedin_clone_application.service.JobService;
import com.linkedin_clone_application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;
    private final UserService userService;

    JobController(JobService jobService, UserService userService){
        this.jobService=jobService;
        this.userService=userService;
    }

    // Show all jobs
    @GetMapping
    public String viewAllJobs(Model model) {
        List<Job> jobs = jobService.getAllJobs();
        model.addAttribute("jobs", jobs);
        return "jobslist"; // Thymeleaf template: jobs/list.html
    }

    // Show job creation form
    @GetMapping("/create")
    public String showCreateJobForm(Model model) {
        model.addAttribute("job", new Job());
        return "createjob"; // Thymeleaf template: jobs/createjob.html
    }

    // Process job creation form
    @PostMapping("/create")
    public String createJob(@ModelAttribute("job") Job job) {
        String email = getLoggedInUserEmail();
        if (email!=null){
            User user=userService.findByEmail(email);
            job.setUser(user);
        }
        job.setCreatedAt(LocalDateTime.now());
        job.setUpdatedAt(LocalDateTime.now());
        jobService.saveJob(job);
        return "redirect:/jobs";
    }

    // Show job edit form
    @GetMapping("/edit/{id}")
    public String showEditJobForm(@PathVariable int id, Model model) {
        Job job = jobService.getJobById(id).orElseThrow(() -> new IllegalArgumentException("Invalid job ID:" + id));
        model.addAttribute("job", job);
        return "jobs/edit"; // Thymeleaf template: jobs/edit.html
    }

    // Process job edit form
    @PostMapping("/edit/{id}")
    public String editJob(@PathVariable int id, @ModelAttribute("job") Job job) {
        job.setId(id); // Ensure the ID remains consistent
        jobService.saveJob(job);
        return "redirect:/jobs";
    }

    // Delete a job
    @GetMapping("/delete/{id}")
    public String deleteJob(@PathVariable int id) {
        jobService.deleteJobById(id);
        return "redirect:/jobs";
    }

    @GetMapping("/{id}")
    public String viewFullJob(@PathVariable int id, Model model){
        Job job = jobService.findById(id); // Implement findById in your service layer
        model.addAttribute("job", job);
        return "viewfulljob";
    }

    private String getLoggedInUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) auth.getPrincipal()).getUsername(); // this returns email
        }
        return null;
    }
}
