package com.updateme.service.youtube;

import com.updateme.entity.Profile;
import com.updateme.model.YoutubeProfileInfo;
import com.updateme.service.access.YoutubeAccessService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YoutubeProfileService {

    private static final Logger logger = LogManager.getLogger(YoutubeProfileService.class);

    @Autowired
    private YoutubeAccessService youtubeAccessService;


    /**
     * Service used to obtain Subscribers and URL to profile Image from the Youtube Platform
     * @param userProfile - The current profile information in the database
     * @param youtubeKey - The ID for the profile in Youtube's System
     * @return userProfile Updated to current profile Information
     */
    public Profile updateYoutubeInfo(Profile userProfile, String youtubeKey){

        logger.info("updateYoutubeInfo():: Updating {} information from Youtube.",userProfile.getName() );
        YoutubeProfileInfo youtubeProfileInfo = youtubeAccessService.getYoutubeProfile(youtubeKey);
        if(userProfile != null && youtubeProfileInfo != null){
            userProfile.setProfileImage(youtubeProfileInfo.getProfileImage());
            userProfile.setFollowerCount(youtubeProfileInfo.getSubscriberCount());
            logger.info("updateYoutubeInfo():: {}'s information has been updated to {}",userProfile.getName(),youtubeProfileInfo );
        }
        else{
            logger.error("updateYoutubeInfo():: Error accessing YoutubeAPI for {}", userProfile.getName() );
        }


        return userProfile;
    }
}
