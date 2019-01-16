package com.updateme.service.retrieve;

import com.updateme.entity.PostInfo;
import com.updateme.model.RetrievePostReq.PostFilter;
import com.updateme.model.RetrievePostRs.PostInfoResponse;
import com.updateme.model.RetrievePostRs.RetrievePostResponse;
import com.updateme.repository.PostInfoRepository;
import com.updateme.translator.PostInfoTranslator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class RetrievePostService {

    private static final Logger logger = LogManager.getLogger(RetrievePostService.class);

    private PostInfoRepository postInfoRepository;

    @Autowired
    public RetrievePostService(PostInfoRepository postInfoRepository){
        this.postInfoRepository = postInfoRepository;
    }

    public RetrievePostResponse retrievePosts(Integer id){
        RetrievePostResponse response = new RetrievePostResponse();
        logger.trace("retrievePosts():: Searching posts for Profile ID: {}", id);
        List<PostInfo> profilePosts = postInfoRepository.findByProfileId(id);
        List<PostInfoResponse> postResponse = new ArrayList<>();

        //If Id is not in database
        if(profilePosts.size() == 0  || id == null){
            logger.error("retrievePosts():: No posts found for Profile ID: {}",id );
            return response;
        }


            logger.trace("retrievePosts():: Found {} Post(s) for ID: {}",profilePosts.size(), id);
            //Order the posts by date
            Collections.sort(profilePosts, Comparator.comparing(PostInfo::getPostCreated));
            Collections.reverse(profilePosts);

            logger.trace("retrievePosts():: Sorting {} Post(s) by date and setting up response.",profilePosts.size());
            //Iterate through Database values and setup response
            profilePosts.forEach(postInfo -> {
                postResponse.add(PostInfoTranslator.toPostInfoResponse(postInfo));
            });

        response.setPosts(postResponse);
        return  response;

    }

    public RetrievePostResponse retrievePostsFromFilter(Integer id, PostFilter postFilter){
        RetrievePostResponse response = new RetrievePostResponse();
        logger.trace("retrievePostsFromFilter():: Searching posts with Filters : {} ,for Profile ID: {}", postFilter, id);
        List<PostInfo> profilePosts = postInfoRepository.findByProfileId(id);
        List<PostInfoResponse> postResponse = new ArrayList<>();

        //If Id is not in database
        if(profilePosts.size() == 0  || id == null){
            logger.error("retrievePosts():: No posts found for Profile ID: {}",id );
            return response;
        }



        //Order the posts by date
        Collections.sort(profilePosts, Comparator.comparing(PostInfo::getPostCreated));
        Collections.reverse(profilePosts);

        logger.trace("retrievePosts():: Found {} Post(s) for ID: {}, Filtering results by platform.",profilePosts.size(), id);
        //Iterate through Database values and setup response
        profilePosts.forEach(postInfo -> {
            if(postFilter.getTwitch() && postInfo.getPlatformId() == 1){
                postResponse.add(PostInfoTranslator.toPostInfoResponse(postInfo));
            }

            if(postFilter.getTwitter() && postInfo.getPlatformId() == 2){

                postResponse.add(PostInfoTranslator.toPostInfoResponse(postInfo));
            }


            if(postFilter.getFacebook() && postInfo.getPlatformId() == 3){

                postResponse.add(PostInfoTranslator.toPostInfoResponse(postInfo));
            }

            if(postFilter.getYoutube() && postInfo.getPlatformId() == 4){

                postResponse.add(PostInfoTranslator.toPostInfoResponse(postInfo));
            }
        });

        logger.trace("retrievePosts():: Posts have been filtered, sending response.",profilePosts.size(), id);

        response.setPosts(postResponse);
        return  response;

    }
}
