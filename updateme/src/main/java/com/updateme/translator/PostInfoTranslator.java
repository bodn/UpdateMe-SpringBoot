package com.updateme.translator;

import com.updateme.entity.PostInfo;
import com.updateme.model.RetrievePostRs.PostInfoResponse;
import com.updateme.model.TwitchPostInfo;
import com.updateme.model.YoutubePostInfo;
import com.updateme.type.Platform;


public class PostInfoTranslator {

    public static PostInfo twitchToPostInfo(TwitchPostInfo twitchPostInfo, Integer profileId){
        PostInfo postInfo = new PostInfo();
        //Platform
        postInfo.setPlatformId(new Integer(1));

        //Date
        postInfo.setPostCreated(twitchPostInfo.getPostCreatedDate());

        //Thumbnail
        postInfo.setPostThumbnail(twitchPostInfo.getPostThumbnailUrl());

        //Title
        postInfo.setPostTitle(twitchPostInfo.getPostTitle());

        //URL
        postInfo.setPostUrl(twitchPostInfo.getPostURL());

        //View Count
        postInfo.setPostViews(twitchPostInfo.getPostViewCount().toString());

        //ProfileID
        postInfo.setProfileId(profileId);

        return postInfo;
    }

    public static PostInfoResponse toPostInfoResponse(PostInfo postInfo){
        PostInfoResponse postInfoResponse = new PostInfoResponse();
        //Platform
        postInfoResponse.setPlatformId(postInfo.getPlatformId());

        //Date
        postInfoResponse.setPostCreated(postInfo.getPostCreated());

        //Thumbnail
        postInfoResponse.setPostThumbnail(postInfo.getPostThumbnail());

        //Title
        postInfoResponse.setPostTitle(postInfo.getPostTitle());

        //URL
        postInfoResponse.setPostUrl(postInfo.getPostUrl());

        //View Count
        postInfoResponse.setPostViews(postInfo.getPostViews());

        //ProfileID
        postInfoResponse.setProfileId(postInfo.getProfileId());

        //PostId
        postInfoResponse.setPostId(postInfo.getPostId());
        return postInfoResponse;
    }

    public static PostInfo youtubeToPostInfo(YoutubePostInfo youtubePostInfo, Integer profileId){
        PostInfo postInfo = new PostInfo();
        //Platform == Youtube
        postInfo.setPlatformId(Platform.getId(Platform.YOUTUBE));

        //Date
        postInfo.setPostCreated(youtubePostInfo.getPostCreatedDate());

        //Thumbnail
        postInfo.setPostThumbnail(youtubePostInfo.getPostThumbnailUrl());

        //Title
        postInfo.setPostTitle(youtubePostInfo.getPostTitle());

        //URL
        postInfo.setPostUrl(youtubePostInfo.getPostURL());

        //View Count
        postInfo.setPostViews(youtubePostInfo.getPostViewCount().toString());

        //ProfileID
        postInfo.setProfileId(profileId);

        return postInfo;
    }
}
