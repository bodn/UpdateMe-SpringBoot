package com.updateme.service.twitch;

import com.updateme.entity.PostInfo;
import com.updateme.entity.Profile;
import com.updateme.model.TwitchPostInfo;
import com.updateme.service.access.TwitchAccessService;
import com.updateme.translator.PostInfoTranslator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TwitchPostService {


    private static final Logger logger = LogManager.getLogger(TwitchPostService.class);

    @Autowired
    private TwitchAccessService twitchAccessService;


    /**
     * Call Twitch to retrieve the List of Clips for the last 12 hours and filter them to be added to the database
     * @param profile
     * @param twitchKey
     */
    public List<PostInfo> updateTwitchClips(Profile profile, String twitchKey){

        List<PostInfo> filteredClipList = new ArrayList<>();

        List<TwitchPostInfo> twitchClipsList = twitchAccessService.retrieveProfileClips(twitchKey);

        logger.trace("updateTwitchClips():: Analyzing {} Clips for the Profile: {}",twitchClipsList.size(),profile.getName() );

        twitchClipsList.forEach(twitchClip -> {

            if(twitchClip.getPostViewCount() > 500){

                filteredClipList.add(PostInfoTranslator.twitchToPostInfo(twitchClip, profile.getProfileId()));
            }



        });

        logger.trace("updateTwitchClips():: There is {} Trending twitch clips for {}",filteredClipList.size(), profile.getName() );
        return filteredClipList;
    }

    /**
     * Call Twitch to retrieve the List of Videos that the profile posted
     * @param profile - the profile information of the person we are searching for
     * @param twitchKey - the profiles twitch identification key
     */
    public List<PostInfo> updateTwitchVideos(Profile profile, String twitchKey){

        List<PostInfo> translatedTwitchVideoInfoList = new ArrayList<>();

        logger.trace("updateTwitchVideos():: Calling Twitch API to find new videos for {}", profile.getName());
        List<TwitchPostInfo> twitchVideoList = twitchAccessService.retrieveProfileVideos(twitchKey);

        logger.trace("updateTwitchVideos():: Retrieved {} Videos for {}",translatedTwitchVideoInfoList.size(),profile.getName() );

        twitchVideoList.forEach(twitchVideo -> {
            if(!twitchVideo.getPostThumbnailUrl().equals("")){
                translatedTwitchVideoInfoList.add(PostInfoTranslator.twitchToPostInfo(twitchVideo, profile.getProfileId()));
            }
        });

        logger.trace("updateTwitchVideos():: There is {} Twitch video for {}",translatedTwitchVideoInfoList.size(), profile.getName() );
        return translatedTwitchVideoInfoList;
    }
}
