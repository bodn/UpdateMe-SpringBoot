package com.updateme.service.update;

import com.updateme.entity.PostInfo;
import com.updateme.entity.Profile;
import com.updateme.model.PlatformInfo;
import com.updateme.repository.PersonalityRepository;
import com.updateme.repository.PostInfoRepository;
import com.updateme.repository.ProfilePlatformRepository;
import com.updateme.service.retrieve.RetrieveProfileService;
import com.updateme.service.twitch.TwitchProfileService;
import com.updateme.service.youtube.YoutubeProfileService;
import com.updateme.type.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

@Service
public class UpdateProfileService {

    private RetrieveProfileService retrieveProfileService;

    private YoutubeProfileService youtubeProfileService;

    private TwitchProfileService twitchProfileService;

    private PersonalityRepository personalityRepository;

    private PostInfoRepository postInfoRepository;

    @Autowired
    public UpdateProfileService(PostInfoRepository postInfoRepository,YoutubeProfileService youtubeProfileService, RetrieveProfileService retrieveProfileService, TwitchProfileService twitchProfileService, PersonalityRepository personailityRepository, ProfilePlatformRepository profilePlatformRepository) {
        this.twitchProfileService = twitchProfileService;
        this.personalityRepository = personailityRepository;
        this.postInfoRepository = postInfoRepository;
        this.retrieveProfileService = retrieveProfileService;
        this.youtubeProfileService = youtubeProfileService;
    }


    /**
     * Query Database for all profiles inside the Database, Update information on their main platform
     * @Todo Setup a database for each platform information to be stored
     * @return List of profiles to be translated
     */
    public void updateAllProfilesInformation(){

        //Find all profiles in the database
        Iterable<Profile> profiles = personalityRepository.findAll();


        //Determine platforms for each ProfileInfo
        profiles.forEach(profile -> {


            /** This is disabled as we are only updating their main platform information*/
            // Get all Platforms for the profile
            List<PlatformInfo> platformInfoList = retrieveProfileService.retrievePlatformsById(profile.getProfileId());

            String platformKey = null;
            for(PlatformInfo platform : platformInfoList){
                if(platform.getPlatform() == Platform.getPlatformById(profile.getMainPlatform()))
                    platformKey = platform.getPlatformKey();
            }

            //Iterate through the platforms and perform the required action

            Profile updatedProfile = profile;
            //for(PlatformInfo platformInfo : platforms){

                //ProfileInfo exists on twitch
                if(profile.getMainPlatform() == 1){
                    //Update Profile
                    updatedProfile = twitchProfileService.updateTwitchInfo(profile, platformKey);
                }

                //ProfileInfo exists on Twitter
                if(profile.getMainPlatform() == Platform.getId(Platform.TWITTER)){
                    //Same stuff
                }

                //Platform exists on Youtube
                if(profile.getMainPlatform() == Platform.getId(Platform.YOUTUBE)){
                    updatedProfile = youtubeProfileService.updateYoutubeInfo(profile, platformKey);
                }
           // }


            //Update ProfileInfo
            if(updatedProfile.getLastActive() == null){
                List<PostInfo> latestPost = postInfoRepository.findFirstByProfileIdOrderByPostCreatedDesc(profile.getProfileId());
                TimeZone timeZone = TimeZone.getTimeZone("UTC");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
                sdf.setTimeZone(timeZone);
                updatedProfile.setLastActive(sdf.format(latestPost.get(0).getPostCreated()));
            }
            personalityRepository.save(updatedProfile);
        });



    }

}
