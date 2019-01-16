package com.updateme.service.twitch;

import com.updateme.entity.Profile;
import com.updateme.service.access.TwitchAccessService;
import com.updateme.type.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TwitchProfileService {

    private static final Logger logger = LogManager.getLogger(TwitchProfileService.class);

    @Autowired
    private TwitchAccessService twitchAccessService;


    public Profile updateTwitchInfo(Profile profile, String twitchKey){

        //Get Follower Count
        logger.trace("updateTwitchInfo():: Updating Follower count for {} with Twitch ID {}",profile.getName(), twitchKey);
        String followerCount = twitchAccessService.retrieveProfileFollowers(twitchKey);
        profile.setFollowerCount(followerCount);

        //Check if live now set last modified too
        logger.trace("updateTwitchInfo():: Checking if {} is live...",profile.getName());
        if (Platform.getPlatformById(profile.getMainPlatform()) == Platform.TWITCH) {
            String liveInfo = twitchAccessService.retrieveStream(twitchKey);
            if(liveInfo != null) {
                logger.debug("updateTwitchInfo():: {} is LIVE! setting lastActive field to {}", profile.getName(), liveInfo);
                profile.setLastActive(liveInfo);
            }
            else{
                logger.trace("updateTwitchInfo():: {} is not live.",profile.getName());
                profile.setLastActive(null);
            }

        }




        //Update Profile image
        logger.trace("updateTwitchInfo():: Updating {} profile image...",profile.getName());
        profile.setProfileImage(twitchAccessService.retrieveProfileImage(twitchKey));



        return profile;
    }

    public void updateTwitchActivity(){

    }
}
