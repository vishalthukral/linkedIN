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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    // Show the job application form
    @GetMapping("/jobs/{jobId}/apply")
    public String showApplicationForm(@PathVariable("jobId") Integer jobId, Model model) {
        // Get the current logged-in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User currentUser = customUserDetails.getUser(); // Access the full User object

// Access firstName and lastName
        String firstName = currentUser.getFirstName();
        String lastName = currentUser.getLastName();

// Now you can use currentUser or firstName, lastName as needed
        model.addAttribute("user", currentUser);

        // Get the job
        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid job Id:" + jobId));

        // Check if user already applied to this job
        JobApplication existingApplication = jobApplicationRepo
                .findByJobAndUser(job, currentUser);

        if (existingApplication != null) {
            return "redirect:/jobs/" + jobId + "?alreadyApplied=true";
        }

        // Create new job application object
        JobApplication jobApplication = new JobApplication();

        // Add objects to model
        model.addAttribute("user", currentUser);
        model.addAttribute("job", job);
        model.addAttribute("jobApplication", jobApplication);

        return "jobApplication";
    }

    // Process the job application submission
    @PostMapping("/jobs/{jobId}/apply")
    public String processApplicationSubmission(
            @PathVariable("jobId") Integer jobId,
            @ModelAttribute JobApplication jobApplication,
            @RequestParam("resumeFile") MultipartFile resumeFile,
            RedirectAttributes redirectAttributes) {

        // Get the current logged-in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User currentUser = customUserDetails.getUser();

        // Get the job
        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid job Id:" + jobId));

        // Check if user already applied
        JobApplication existingApplication = jobApplicationRepo
                .findByJobAndUser(job, currentUser);

        if (existingApplication != null) {
            redirectAttributes.addFlashAttribute("errorMessage", "You have already applied to this job");
            return "redirect:/jobs/" + jobId;
        }

        try {
            // Upload resume file to Cloudinary and get URL
            String resumeUrl = cloudinaryService.uploadFile(resumeFile);

            // Set job application properties
            jobApplication.setUser(currentUser);
            jobApplication.setJob(job);
            jobApplication.setResumeUrl(resumeUrl);
            jobApplication.setStatus(ApplicationStatus.PENDING);
            jobApplication.setAppliedAt(LocalDateTime.now());

            // Save the job application
            jobApplicationRepo.save(jobApplication);

            // Add success message
            redirectAttributes.addFlashAttribute("successMessage",
                    "Your application to " + job.getJobTitle() + " has been submitted successfully!");

            return "redirect:/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error submitting application: " + e.getMessage());
            return "redirect:/jobs/" + jobId + "/apply";
        }
    }
}