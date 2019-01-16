package com.updateme.service.access;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.updateme.model.TwitchPostInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Service to perform all Twitch API interactions
 * v0.1 Get Followers, Stream Info, and Channel Info
 *
 * Future. Create a queue system to get more profiles than one per query
 */
@Service
public class TwitchAccessService {

    private static final Logger logger = LogManager.getLogger(TwitchAccessService.class);

    private RestTemplate restTemplate;

    @Value("${twitch.api.key}")
    private String twitchKey;

    @Value("${twitch.api.fetch.followers.url}")
    private String twitchFollowersURL;

    @Value("${twitch.api.fetch.stream.url}")
    private String twitchStreamUrl;

    @Value("${twitch.api.fetch.channel.info.url}")
    private String twitchChannelInfoUrl;

    @Autowired
    public TwitchAccessService(RestTemplateBuilder restTemplateBuilder){
        this.restTemplate = restTemplateBuilder.build();
    }


    /**
     * Service to retrive followers for the specified profile. Twitch only permits one user id for this request
     * @param twitchUserId - Profile that has a user id associated to Twitch
     * @return String containing the follower Count
     */
    public String retrieveProfileFollowers(String twitchUserId){
        String response = null;

        String requestUrl = retrieveFollowerURL(twitchUserId).toUriString();
        logger.trace("retrieveProfileFollowers:: Calling Twitch API to get profile Followers for ID: {}", twitchUserId);
        ResponseEntity<String> responseEntity = sendRequest(requestUrl);


        if(responseEntity != null) {
            if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                response = new JsonParser().parse(responseEntity.getBody())
                        .getAsJsonObject().get("total").getAsString();
                logger.trace("retrieveProfileFollowers:: Response received. Follower count: {}", response);
            } else if (responseEntity.getStatusCode().is4xxClientError()) {
                logger.error("retrieveProfileFollowers:: Too many HTTP requests to Twitch");
            }
        }

        return  response ;
    }



    public String retrieveStream(String twitchUserId){

        String requestUrl = retrieveStreamUrl(twitchUserId).toUriString();
        logger.trace("retrieveStream:: Calling Twitch API to get profile stream info for ID: {}", twitchUserId);
        ResponseEntity<String> responseEntity = sendRequest(requestUrl);
        String response = null;

        if(responseEntity != null) {
            if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                JsonArray twitchData = new JsonParser().parse(responseEntity.getBody())
                        .getAsJsonObject().get("data")
                        .getAsJsonArray();

                if (twitchData.size() > 0) {
                    String viewers = twitchData.get(0).getAsJsonObject().get("viewer_count").toString();

                    String startTime = twitchData.get(0).getAsJsonObject().get("started_at").toString();
                    startTime = startTime.replaceAll("T|Z", " ").trim();
                    startTime = startTime.replaceAll("\"", "").trim();
                    System.out.println(startTime);

                    response = "LIVE" + " " + viewers;

                    logger.trace("retrieveStream:: Profile is {} viewers", response);
                    return response;
                }
            }
        }
        logger.trace("retrieveStream:: Profile is not live. No stream data available.");
        return null;
    }

    /**
     * Used to obtain the url to the Image
     * @return Profile with the fields for name and ProfileURL
     */
    public String retrieveProfileImage(String twitchUserId){
        String imageUrl= null;
        String requestUrl = retrieveProfileImageUrl(twitchUserId).toUriString();
        logger.trace("retrieveProfileImage:: Calling Twitch API to get profile image URL for ID: {}", twitchUserId);

        ResponseEntity<String> responseEntity = sendRequest(requestUrl);

        if(responseEntity != null){
            if(responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                JsonArray twitchData = new JsonParser().parse(responseEntity.getBody())
                        .getAsJsonObject().get("data")
                        .getAsJsonArray();

                if (twitchData.size() > 0) {
                    imageUrl = twitchData.get(0).getAsJsonObject().get("profile_image_url").toString();
                    logger.trace("retrieveProfileImage:: Profile Image found for ID: {}", twitchUserId);
                    return imageUrl.replaceAll("\"","" );

                }

            }
        }


        logger.error("retrieveProfileImage:: Profile image was not found for ID: {}",twitchUserId);
        return null;
    }

    /**
     * Retrieve the last 12 hours of Clips for the profile for the twitch profile service to filter
     * @param twitchUserId - the twitch User we are obtaining the information for
     * @return List of Twitch Clips for the last 12 hours under the user
     */
    public List<TwitchPostInfo> retrieveProfileClips(String twitchUserId){

        String requestUrl = retrieveProfileClipsUrl(twitchUserId).toUriString();
        logger.trace("retrieveProfileClips():: Calling Twitch API {} to get clips for the past 12 hours for ID: {}",requestUrl, twitchUserId);

        List<TwitchPostInfo> twitchClipList = new ArrayList<>();
        ResponseEntity<String> responseEntity = sendRequest(requestUrl);

        if(responseEntity != null){
            if(responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null){
                JsonArray twitchData = new JsonParser().parse(responseEntity.getBody())
                        .getAsJsonObject().get("data")
                        .getAsJsonArray();

                if (twitchData.size() > 0) {


                    for (JsonElement twitchDataClip : twitchData) {
                        TwitchPostInfo twitchClip = new TwitchPostInfo();
                        //Get Title
                        twitchClip.setPostTitle(twitchDataClip.getAsJsonObject().get("title").getAsString());

                        //Get Creation Date
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
                        try {
                            twitchClip.setPostCreatedDate(sdf.parse(twitchDataClip.getAsJsonObject().get("created_at").getAsString()));

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        //Get Post URL
                        twitchClip.setPostURL(twitchDataClip.getAsJsonObject().get("embed_url").getAsString());

                        //Get Thumbnail
                        twitchClip.setPostThumbnailUrl(twitchDataClip.getAsJsonObject().get("thumbnail_url").getAsString());

                        //Get ViewerCount
                        twitchClip.setPostViewCount(twitchDataClip.getAsJsonObject().get("view_count").getAsInt());

                        //add to response
                        twitchClipList.add(twitchClip);

                    }
                }
                logger.trace("retrieveProfileClips():: {} Clips for ID: {} over the past 12 hours", twitchClipList.size() ,twitchUserId);
                return twitchClipList;
            }
        }
        logger.error("retrieveProfileClips():: No Clips found for ID: {}", twitchUserId);
        return new ArrayList<>();
    }


    /**
     * Retrieve the last 12 hours of Clips for the profile for the twitch profile service to filter
     * @param twitchUserId - the twitch User we are obtaining the information for
     * @return List of Twitch Clips for the last 12 hours under the user
     */
    public List<TwitchPostInfo> retrieveProfileVideos(String twitchUserId){

        String requestUrl = retrieveProfileVideosUrl(twitchUserId).toUriString();
        logger.trace("retrieveProfileClips():: Calling Twitch API to get newest video for ID: {}", twitchUserId);

        List<TwitchPostInfo> twitchVideoList = new ArrayList<>();
        ResponseEntity<String> responseEntity = sendRequest(requestUrl);

        if(responseEntity != null){
            if(responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null){
                JsonArray twitchData = new JsonParser().parse(responseEntity.getBody())
                        .getAsJsonObject().get("data")
                        .getAsJsonArray();

                if (twitchData.size() > 0) {
                    JsonElement twitchVideoData = twitchData.get(0);
                        TwitchPostInfo twitchClip = new TwitchPostInfo();
                        //Get Title
                        twitchClip.setPostTitle(twitchVideoData.getAsJsonObject().get("title").getAsString());

                        //Get Creation Date
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
                        try {
                            twitchClip.setPostCreatedDate(sdf.parse(twitchVideoData.getAsJsonObject().get("created_at").getAsString()));

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        //Get Post URL
                        twitchClip.setPostURL(formatTwitchVideoUrl(twitchVideoData.getAsJsonObject().get("id").getAsString()));
                        //Get Thumbnail
                        String thumbnailUrl = twitchVideoData.getAsJsonObject().get("thumbnail_url").getAsString().replace("thumb0-%{width}x%{height}.jpg", "thumb0-640x480.jpg");

                        twitchClip.setPostThumbnailUrl(thumbnailUrl);

                        //Get ViewerCount
                        twitchClip.setPostViewCount(twitchVideoData.getAsJsonObject().get("view_count").getAsInt());

                        //add to response
                        twitchVideoList.add(twitchClip);


                }
                logger.trace("retrieveProfileVideos():: {} Video for ID: {} found.", twitchVideoList.size() ,twitchUserId);
                return twitchVideoList;
            }
        }
        logger.error("retrieveProfileVideos():: No Videos found for ID: {}", twitchUserId);
        return new ArrayList<>();
    }

    private String formatTwitchVideoUrl(String id){
        return "https://player.twitch.tv/?video="+id;
    }

    private ResponseEntity<String> sendRequest (String requestUrl){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Client-ID", twitchKey);

        HttpEntity<?> entity  = new HttpEntity<>(headers);

        return restTemplate.exchange(requestUrl, HttpMethod.GET, entity, String.class);
    }

    /**
     * Method to build the request for the followerRequest on the specified users
     * @param twitchUserId - Profile Id on the twitch platform
     * @return A url to send to the request
     */
    private UriComponentsBuilder retrieveFollowerURL(String twitchUserId){
        return UriComponentsBuilder.fromHttpUrl(twitchFollowersURL)
                .queryParam("to_id", twitchUserId);

    }

    /**
     * Method to build the request for the retrieveStream on the specified users
     * @param twitchUserId - Profile Id on the twitch platform
     * @return A url to send to the request
     */
    private UriComponentsBuilder retrieveStreamUrl(String twitchUserId){
        return UriComponentsBuilder.fromHttpUrl(twitchStreamUrl)
                .queryParam("user_id", twitchUserId);

    }

    /**
     * Method to build the request for the retrieve Stream info such as username on the specified users
     * @param twitchUserId - Profile Id on the twitch platform
     * @return A url to send to the request
     */
    private UriComponentsBuilder retrieveProfileImageUrl(String twitchUserId){
        return UriComponentsBuilder.fromHttpUrl("https://api.twitch.tv/helix/users")
                .queryParam("id", twitchUserId);

    }

    /**
     * Method to build the request to retrieve Profile Clips
     * @param twitchUserId - Profile Id on the twitch platform
     * @return A url to send to the request
     */
    private UriComponentsBuilder retrieveProfileClipsUrl(String twitchUserId){
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        sdf.setTimeZone(timeZone);
        Calendar cal = Calendar.getInstance(timeZone);
        cal.setTime(new Date());
        cal.add(Calendar.HOUR, -12);
        Date twelveHoursBack = cal.getTime();

        return UriComponentsBuilder.fromHttpUrl("https://api.twitch.tv/helix/clips")
                .queryParam("broadcaster_id", twitchUserId)
                .queryParam("started_at",sdf.format(twelveHoursBack))
                .queryParam("first", "5");

    }


    /**
     * Method to build the request to retrieve Profile Videos
     * @param twitchUserId - Profile Id on the twitch platform
     * @return A url to send to the request
     */
    private UriComponentsBuilder retrieveProfileVideosUrl(String twitchUserId){
        return UriComponentsBuilder.fromHttpUrl("https://api.twitch.tv/helix/videos")
                .queryParam("user_id", twitchUserId)
                .queryParam("first", "1");

    }

}
