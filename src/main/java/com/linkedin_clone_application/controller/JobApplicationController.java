package com.linkedin_clone_application.controller;

import com.linkedin_clone_application.enums.ApplicationStatus;
import com.linkedin_clone_application.model.Job;
import com.linkedin_clone_application.model.JobApplication;
import com.linkedin_clone_application.model.User;
import com.linkedin_clone_application.repository.*;
import com.linkedin_clone_application.service.CloudinaryService;
import com.linkedin_clone_application.service.CustomUserDetails;
import com.linkedin_clone_application.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
public class JobApplicationController {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/jobs/{jobId}/apply")
    public String showApplicationForm(@PathVariable("jobId") Integer jobId, Model model,
                                      RedirectAttributes redirectAttributes) {
        String email = getLoggedInUserEmail();

        User user = userRepository.findByEmail(email);

        if (email == null) {
            return "redirect:/login";  // If not logged in, redirect to login
        }
        model.addAttribute("user", user);

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid job Id:" + jobId));

        JobApplication existingApplication = jobApplicationRepository
                .findByJobAndUser(job, user);

        if (existingApplication != null) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "You have already applied to this job");
            return "redirect:/jobs/" + jobId;
        }

        JobApplication jobApplication = new JobApplication();

        model.addAttribute("user", user);
        model.addAttribute("job", job);
        model.addAttribute("jobApplication", jobApplication);
        model.addAttribute("email",email);

        return "jobApplication";
    }

    @PostMapping("/jobs/{jobId}/apply")
    public String processApplicationSubmission(
            @PathVariable("jobId") Integer jobId,
            @ModelAttribute JobApplication jobApplication,
            @RequestParam("resumeFile") MultipartFile resumeFile,
            RedirectAttributes redirectAttributes,
            @RequestParam("email") String applicantEmail) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User currentUser = customUserDetails.getUser();

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid job Id:" + jobId));

        JobApplication existingApplication = jobApplicationRepository
                .findByJobAndUser(job, currentUser);

        if (existingApplication != null) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "You have already applied to this job");
            return "redirect:/jobs/" + jobId;
        }

        try {
            String resumeUrl = cloudinaryService.uploadFile(resumeFile);

            jobApplication.setUser(currentUser);
            jobApplication.setJob(job);
            jobApplication.setResumeUrl(resumeUrl);
            jobApplication.setStatus(ApplicationStatus.PENDING);
            jobApplication.setAppliedAt(LocalDateTime.now());

            jobApplicationRepository.save(jobApplication);
            String subject = "Job Application Confirmation";
            String messageBody = "Dear " +currentUser.getFirstName()+" "+currentUser.getLastName()+" \n\nYour application has been " +
                    "received. We will get " +
                    "back to you " +
                    "soon.";
            emailService.sendEmail(applicantEmail, subject, messageBody);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Your application to " + job.getJobTitle() + " has been submitted successfully!");

            return "jobApplied";
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