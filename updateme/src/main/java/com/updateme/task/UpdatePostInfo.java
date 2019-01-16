package com.updateme.task;

import com.updateme.service.update.UpdatePostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Scheduled task to perform an update to all profiles and their posts
 */
@Component
public class UpdatePostInfo {

    @Autowired
    private UpdatePostService updatePostService;

   @Scheduled(fixedRate = 120000)
    public void updateProfileInfoTask(){
        System.out.println("\n\nFixed Rate Task :: Execution Time - "+ LocalDateTime.now());
        updatePostService.updateAllProfilePosts();
        System.out.println("Scheduled Task Completed.");
    }
}

