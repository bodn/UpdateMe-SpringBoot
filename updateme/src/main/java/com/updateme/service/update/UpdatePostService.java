package com.updateme.service.update;

import com.updateme.entity.PostInfo;
import com.updateme.entity.Profile;
import com.updateme.model.PlatformInfo;
import com.updateme.repository.PersonalityRepository;
import com.updateme.repository.PostInfoRepository;
import com.updateme.service.retrieve.RetrieveProfileService;
import com.updateme.service.twitch.TwitchPostService;
import com.updateme.service.youtube.YoutubePostService;
import com.updateme.type.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
    @TODO Best do one query to retrieve all the posts for the ID, create a method to seperate the dblist into platform
    do the filter of new posts, update views and create the new DB list.. Could be costly however.
    
    @TODO Create a paging system example. (after a month User will have 100+ Posts) Endpoint will filter how many posts per page
    Maybe intuitve answer would be
        Take an additional parameter as page number / assume 25 per page.
        OR MAP 25 posts per page and
        HashMap<pageNumber, List<PostInfo> postsOnThatPage>
        ELSE
        Iterate through the list and seperate by pages
*/
@Service
public class UpdatePostService {

    private static final Logger logger = LogManager.getLogger(UpdatePostService.class);

    private RetrieveProfileService retrieveProfileService;

    private TwitchPostService twitchPostService;

    private YoutubePostService youtubePostService;

    private PersonalityRepository personalityRepository;

    private PostInfoRepository postInfoRepository;

    @Autowired
    public UpdatePostService(YoutubePostService youtubePostService ,PostInfoRepository postInfoRepository, RetrieveProfileService retrieveProfileService,TwitchPostService twitchPostService, PersonalityRepository personailityRepository) {
        this.twitchPostService = twitchPostService;
        this.personalityRepository = personailityRepository;
        this.retrieveProfileService = retrieveProfileService;
        this.postInfoRepository = postInfoRepository;
        this.youtubePostService=youtubePostService;
    }


    /**
     * Query Database for all profiles inside the Database, Update information
     * @Todo Reduce number of queries
     * @return List of profiles to be translated
     */
    public void updateAllProfilePosts(){

        //Find all profiles in the database
        Iterable<Profile> profiles = personalityRepository.findAll();

        //Find all Posts in Database
        Iterable<PostInfo> postInfoList = postInfoRepository.findAll();


        //Determine platforms for each ProfileInfo
        profiles.forEach(profile -> {


            // Get all Platforms for the profile
            List<PlatformInfo> platforms = retrieveProfileService.retrievePlatformsById(profile.getProfileId());



            //Iterate through the platforms and perform the required action

            for(PlatformInfo platformInfo : platforms){

                //ProfileInfo exists on twitch
                if(platformInfo.getPlatform() == Platform.TWITCH){
                    //Query Database
                    List<PostInfo> dbTwitchPostList = postInfoRepository.findByProfileIdAndPlatformId(profile.getProfileId(),1 );


                    //Update Twitch Clips
                    List<PostInfo> twitchClipsList = twitchPostService.updateTwitchClips(profile, platformInfo.getPlatformKey());
                    dbTwitchPostList.addAll(filterPosts(dbTwitchPostList, twitchClipsList));
                    logger.trace("updateAllProfilePosts():: Database List Updated for new clips.", twitchClipsList.size() );


                    //Update Twitch Videos
                    logger.trace("updateAllProfilePosts():: Updating Twitch Videos for {}",profile.getName() );
                    List<PostInfo> twitchVideoList = twitchPostService.updateTwitchVideos(profile, platformInfo.getPlatformKey());
                    dbTwitchPostList.addAll(filterPosts(dbTwitchPostList, twitchVideoList));

                    postInfoRepository.saveAll(dbTwitchPostList);
                }

                //ProfileInfo exists on Twitter
                if(platformInfo.getPlatform() == Platform.YOUTUBE){

                    //Query Database
                    List<PostInfo> dbYoutubeList = postInfoRepository.findByProfileIdAndPlatformId(profile.getProfileId(),Platform.getId(Platform.YOUTUBE) );
                    List<PostInfo> youtubeLatestVideo = youtubePostService.updateYoutubePosts(profile, platformInfo.getPlatformKey());
                    dbYoutubeList.addAll(filterPosts(dbYoutubeList,youtubeLatestVideo ));

                    postInfoRepository.saveAll(dbYoutubeList);
                }

                //Platform exists on Facebook
                if(platformInfo.getPlatform() == Platform.FACEBOOK){

                }
            }

            //Update Last Active field for the profiles.
            List<PostInfo> allPosts = postInfoRepository.findByProfileId(profile.getProfileId());
            List<Date> activeDates = new ArrayList<>();
            allPosts.stream().map(PostInfo::getPostCreated).forEachOrdered(activeDates::add);

            /** @TODO Fix the last active updater
            */
            if(profile.getLastActive() == null || !profile.getLastActive().contains("LIVE")){
                TimeZone timeZone = TimeZone.getTimeZone("UTC");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
                sdf.setTimeZone(timeZone);
                profile.setLastActive(sdf.format(Collections.max(activeDates)));
            }

            personalityRepository.save(profile);
        });

    }

    /**
     * Method used to determine if the Found posts are new or already in the database
     * @// TODO: 2018-11-08 Update the views if they are higher
     * @param dbList List of posts for the platform in the database
     * @param socialPostList List of Posts from the platform response
     * @return a filtered list of new posts to be added to the database
     */
    private List<PostInfo> filterPosts(List<PostInfo> dbList, List<PostInfo> socialPostList){
        List<PostInfo> filteredList = new ArrayList<>();
        socialPostList.forEach(foundPost->{
            boolean newPost = true;
            for(PostInfo dbPost : dbList){
                if(foundPost.equals(dbPost) || foundPost.getPostUrl().isEmpty()){
                    logger.trace("filterPosts():: foundPost Found same post");
                    newPost = false;
                    //Update the views
                    dbPost.setPostViews(foundPost.getPostViews());
                    break;
                }
            }

            if(newPost){
                filteredList.add(foundPost);
                logger.trace("filterPosts():: foundPost: {} is unique adding to list", foundPost.getPostTitle());
            }

        });

        return filteredList;
    }


}
