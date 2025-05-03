    package com.linkedin_clone_application.model;


    import com.linkedin_clone_application.enums.EmploymentType;
    import com.linkedin_clone_application.enums.WorkPlaceType;
    import jakarta.persistence.*;

    import java.time.LocalDateTime;

    @Entity
    @Table(name = "jobs")
    public class Job{

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;

        private String company;
        private String jobTitle;
        private String location;
        private String jobDescription;

        @Enumerated(EnumType.STRING)
        private EmploymentType employmentType;

        @Enumerated(EnumType.STRING)
        private WorkPlaceType workPlaceType;



        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getJobTitle() {
            return jobTitle;
        }

        public void setJobTitle(String jobTitle) {
            this.jobTitle = jobTitle;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getJobDescription() {
            return jobDescription;
        }

        public void setJobDescription(String jobDescription) {
            this.jobDescription = jobDescription;
        }

        public EmploymentType getEmploymentType() {
            return employmentType;
        }

        public void setEmploymentType(EmploymentType employmentType) {
            this.employmentType = employmentType;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public WorkPlaceType getWorkPlaceType() {
            return workPlaceType;
        }

        public void setWorkPlaceType(WorkPlaceType workPlaceType) {
            this.workPlaceType = workPlaceType;
        }


    }