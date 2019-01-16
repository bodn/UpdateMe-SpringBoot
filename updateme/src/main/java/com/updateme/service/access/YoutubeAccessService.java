package com.updateme.service.access;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.updateme.model.YoutubePostInfo;
import com.updateme.model.YoutubeProfileInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Service
public class YoutubeAccessService {

    private static final Logger logger = LogManager.getLogger(YoutubeAccessService.class);

    private RestTemplate restTemplate;

    @Autowired
    public YoutubeAccessService(RestTemplateBuilder restTemplateBuilder){
        this.restTemplate = restTemplateBuilder.build();
    }

    @Value("${youtube.api.key}")
    private String youtubeKey;

    @Value("${youtube.api.fetch.profile.information}")
    private String youtubeProfileInformationUrl;

    public YoutubeProfileInfo getYoutubeProfile(String youtubeProfileId){
        logger.trace("getYoutubeProfile():: Getting youtube profile information for {} from {}",youtubeProfileId);

        YoutubeProfileInfo profileInfo = new YoutubeProfileInfo();
        ResponseEntity<String> responseEntity = sendRequest(retrieveYoutubeProfileURL(youtubeProfileId));

        if(responseEntity != null) {
            if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                JsonArray youtubeData = new JsonParser().parse(responseEntity.getBody())
                        .getAsJsonObject().get("items")
                        .getAsJsonArray();

                if(youtubeData.size() > 0){
                    String subscriberCount = youtubeData.get(0).getAsJsonObject().get("statistics")
                                                .getAsJsonObject().get("subscriberCount").getAsString();

                    String youtubeImageUrl=youtubeData.get(0).getAsJsonObject()
                                            .get("snippet").getAsJsonObject().get("thumbnails")
                                            .getAsJsonObject().get("high").getAsJsonObject().get("url").getAsString();

                    profileInfo.setProfileImage(youtubeImageUrl);
                    profileInfo.setSubscriberCount(subscriberCount);
                }


            } else if (responseEntity.getStatusCode().is4xxClientError()) {
                logger.error("retrieveProfileFollowers:: Error reaching out to Youtube ({})",responseEntity.getStatusCodeValue());
            }
        }

        logger.trace("getYoutubeProfile():: Profile Info found {}",profileInfo );
        return profileInfo;
    }


    /**
     * Call youtube API to retrieve newest video for the specified ID
     * @param youtubeProfileId - the users ID in youtube
     * @return YoutubePostInfo containing information of the top video
     */
    public YoutubePostInfo getYoutubeVideos(String youtubeProfileId){
        YoutubePostInfo youtubePostInfo = new YoutubePostInfo();

        logger.info("getYoutubeVideos():: Calling Youtube API to get newest Video Information");

        ResponseEntity<String> responseEntity = sendRequest(retrieveYoutubePostUrl(youtubeProfileId));

        if(responseEntity != null) {
            if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                JsonArray youtubeData = new JsonParser().parse(responseEntity.getBody())
                        .getAsJsonObject().get("items")
                        .getAsJsonArray();

                if(youtubeData.size() > 0){
                    String videoId = youtubeData.get(0).getAsJsonObject().get("id")
                            .getAsJsonObject().get("videoId").getAsString();

                    String youtubeThumbnail=youtubeData.get(0).getAsJsonObject()
                            .get("snippet").getAsJsonObject().get("thumbnails")
                            .getAsJsonObject().get("high").getAsJsonObject().get("url").getAsString();

                    String videoTitle =youtubeData.get(0).getAsJsonObject()
                            .get("snippet").getAsJsonObject().get("title").getAsString();

                    //Get Creation Date
                    TimeZone timeZone = TimeZone.getTimeZone("UTC");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    sdf.setTimeZone(timeZone);

                    try {
                        youtubePostInfo.setPostCreatedDate(sdf.parse(youtubeData.get(0).getAsJsonObject()
                                .get("snippet").getAsJsonObject().get("publishedAt").getAsString()));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    youtubePostInfo.setPostThumbnailUrl(youtubeThumbnail);
                    youtubePostInfo.setPostTitle(videoTitle);
                    youtubePostInfo.setPostURL("https://www.youtube.com/embed/"+videoId);


                    ResponseEntity<String> viewsResponseEntity = sendRequest(retrieveYoutubeVideoStats(videoId));

                    if(viewsResponseEntity != null) {
                        if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                            JsonArray videoData = new JsonParser().parse(viewsResponseEntity.getBody())
                                    .getAsJsonObject().get("items")
                                    .getAsJsonArray();

                            if (videoData.size() > 0) {
                               Integer viewCount = (videoData.get(0).getAsJsonObject()
                                        .get("statistics").getAsJsonObject().get("viewCount").getAsInt());

                                youtubePostInfo.setPostViewCount(viewCount);
                            }
                        }
                    }
                }


            } else if (responseEntity.getStatusCode().is4xxClientError()) {
                logger.error("getYoutubeVideos():: Error reaching out to Youtube ({})",responseEntity.getStatusCodeValue());
            }
        }
        logger.info("getYoutubeVideos():: Has gotten the Video details - {}", youtubePostInfo );
        return  youtubePostInfo;

    }




    private ResponseEntity<String> sendRequest (UriComponentsBuilder requestUrl){
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> entity  = new HttpEntity<>(headers);

        requestUrl.queryParam("key",youtubeKey);
        logger.info("{} is sent", requestUrl.toUriString());

        return restTemplate.exchange(requestUrl.toUriString(), HttpMethod.GET, entity, String.class);
    }


    /**
     * Method to build the request for the YoutubeProfile Info for the specified users
     * @param youtubeUserId - Profile Id on the Youtube platform
     * @return A url to send to the request
     */
    private UriComponentsBuilder retrieveYoutubeProfileURL(String youtubeUserId){
        return UriComponentsBuilder.fromHttpUrl("https://www.googleapis.com/youtube/v3/channels/")
                .queryParam("part", "snippet,statistics")
                .queryParam("id", youtubeUserId);


    }

    /**
     * Method to build the request for the YoutubePost Info for the specified users
     * @param youtubeUserId - Profile Id on the Youtube platform
     * @return A url to send to the request
     */
    private UriComponentsBuilder retrieveYoutubePostUrl(String youtubeUserId){
        return UriComponentsBuilder.fromHttpUrl("https://www.googleapis.com/youtube/v3/search")
                .queryParam("part", "snippet,id")
                .queryParam("channelId", youtubeUserId)
                .queryParam("maxResults","1")
                .queryParam("order","date");


    }

    private UriComponentsBuilder retrieveYoutubeVideoStats(String youtubeVideoId){
        return UriComponentsBuilder.fromHttpUrl("https://www.googleapis.com/youtube/v3/videos")
                .queryParam("part", "statistics")
                .queryParam("id", youtubeVideoId);


    }
}
