package com.updateme.service.youtube;

import com.updateme.entity.PostInfo;
import com.updateme.entity.Profile;
import com.updateme.model.YoutubePostInfo;
import com.updateme.service.access.YoutubeAccessService;
import com.updateme.translator.PostInfoTranslator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class YoutubePostService {

    private static final Logger logger = LogManager.getLogger(YoutubePostService.class);

    @Autowired
    private YoutubeAccessService youtubeAccessService;

    public List<PostInfo> updateYoutubePosts(Profile profile, String youtubeProfileId){

        YoutubePostInfo youtubePostInfo = youtubeAccessService.getYoutubeVideos(youtubeProfileId);

        if(youtubePostInfo != null){
            List<PostInfo> postInfoList = new ArrayList<>();
            PostInfo postInfoEntity = PostInfoTranslator.youtubeToPostInfo(youtubePostInfo, profile.getProfileId() );
            logger.trace("updateYoutubePosts():: {} newest video is  - {}", postInfoEntity);
            postInfoList.add(postInfoEntity);
            return postInfoList;
        }

        logger.error("updateYoutubePosts():: Unable to find Post for {}",profile.getName() );
        return null;
    }
}
