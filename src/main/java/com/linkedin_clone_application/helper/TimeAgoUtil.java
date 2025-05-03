package com.linkedin_clone_application.helper;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimeAgoUtil {

    public static String toTimeAgo(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();

        Duration duration = Duration.between(createdAt, now);

        long seconds = duration.getSeconds();

        if (seconds < 60) {
            return "just now";
        } else if (seconds < 3600) {
            long minutes = seconds / 60;
            return minutes + (minutes == 1 ? " minute ago" : " minutes ago");
        } else if (seconds < 86400) {
            long hours = seconds / 3600;
            return hours + (hours == 1 ? " hour ago" : " hours ago");
        } else if (seconds < 604800) {
            long days = seconds / 86400;
            return days + (days == 1 ? " day ago" : " days ago");
        } else {
            return createdAt.toLocalDate().toString(); // fallback to plain date
        }
    }
}