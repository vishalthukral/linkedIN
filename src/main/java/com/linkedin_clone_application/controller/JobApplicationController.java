package com.linkedin_clone_application.controller;

import com.linkedin_clone_application.enums.ApplicationStatus;
import com.linkedin_clone_application.model.Job;
import com.linkedin_clone_application.model.JobApplication;
import com.linkedin_clone_application.model.User;
import com.linkedin_clone_application.repository.*;
import com.linkedin_clone_application.service.CloudinaryService;
import com.linkedin_clone_application.service.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
public class JobApplicationController {

    @Autowired
    private JobRepo jobRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JobApplicationRepo jobApplicationRepo;

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping("/jobs/{jobId}/apply")
    public String showApplicationForm(@PathVariable("jobId") Integer jobId, Model model) {
        String email=getLoggedInUserEmail();

        User user = userRepo.findByEmail(email);

        if (email == null) {
            return "redirect:/login";  // If not logged in, redirect to login
        }
        model.addAttribute("user", user);

        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid job Id:" + jobId));

        JobApplication existingApplication = jobApplicationRepo
                .findByJobAndUser(job, user);

        if (existingApplication != null) {
            return "redirect:/jobs/" + jobId + "?alreadyApplied=true";
        }

        JobApplication jobApplication = new JobApplication();

        model.addAttribute("user", user);
        model.addAttribute("job", job);
        model.addAttribute("jobApplication", jobApplication);

        return "jobApplication";
    }

    @PostMapping("/jobs/{jobId}/apply")
    public String processApplicationSubmission(
            @PathVariable("jobId") Integer jobId,
            @ModelAttribute JobApplication jobApplication,
            @RequestParam("resumeFile") MultipartFile resumeFile,
            RedirectAttributes redirectAttributes) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User currentUser = customUserDetails.getUser();

        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid job Id:" + jobId));

        JobApplication existingApplication = jobApplicationRepo
                .findByJobAndUser(job, currentUser);

        if (existingApplication != null) {
            redirectAttributes.addFlashAttribute("errorMessage", "You have already applied to this job");
            return "redirect:/jobs/" + jobId;
        }

        try {
            String resumeUrl = cloudinaryService.uploadFile(resumeFile);

            jobApplication.setUser(currentUser);
            jobApplication.setJob(job);
            jobApplication.setResumeUrl(resumeUrl);
            jobApplication.setStatus(ApplicationStatus.PENDING);
            jobApplication.setAppliedAt(LocalDateTime.now());

            jobApplicationRepo.save(jobApplication);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Your application to " + job.getJobTitle() + " has been submitted successfully!");

            return "jobapplied";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error submitting application: " + e.getMessage());
            return "redirect:/jobs/" + jobId + "/apply";
        }
    }
    private String getLoggedInUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) auth.getPrincipal()).getUsername(); // this returns email
        }
        return null;
    }
}