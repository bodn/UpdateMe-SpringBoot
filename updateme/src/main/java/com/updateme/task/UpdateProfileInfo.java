package com.updateme.task;

import com.updateme.service.update.UpdateProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Scheduled task to perform an update to all profiles
 */
@Component
public class UpdateProfileInfo {

    @Autowired
    private UpdateProfileService updateProfileService;

    @Scheduled(fixedRate = 180000)
    private void updateProfileInfoTask(){
        System.out.println("Fixed Rate Task :: Execution Time - "+LocalDateTime.now());
        updateProfileService.updateAllProfilesInformation();
        System.out.println("Scheduled Task Completed.");
    }
}
