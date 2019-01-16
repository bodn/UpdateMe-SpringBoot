package com.updateme.service.retrieve;

import com.updateme.entity.Profile;
import com.updateme.entity.ProfilePlatform;
import com.updateme.model.PlatformInfo;
import com.updateme.model.RetrieveProfileRs.ProfileInfo;
import com.updateme.repository.PersonalityRepository;
import com.updateme.repository.ProfilePlatformRepository;
import com.updateme.translator.RetrieveProfileTranslator;
import com.updateme.type.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RetrieveProfileService {

    private static final Logger logger = LogManager.getLogger(RetrieveProfileService.class);

    private PersonalityRepository personalityRepository;

    private ProfilePlatformRepository profilePlatformRepository;

    @Autowired
    public RetrieveProfileService(PersonalityRepository personailityRepository, ProfilePlatformRepository profilePlatformRepository) {
        this.personalityRepository = personailityRepository;
        this.profilePlatformRepository = profilePlatformRepository;
    }


    /**
     * Query Database for all profiles inside the Database, Update information
     * @return List of profiles to be translated
     */
    public List<ProfileInfo> retrieveAllProfiles(){
        //Find all profiles in the database
        Iterable<Profile> profiles = personalityRepository.findAll();
        logger.trace("retrieveAllProfiles():: Retrieved profiles from Profile Database.");

        //List to hold all of our up-to-date info
        List<ProfileInfo> response = new ArrayList<>();

        logger.trace("retrieveAllProfiles():: Setting Operating platforms for each profile.");
        //Determine platforms for each ProfileInfo
        profiles.forEach(profile -> {

            // Get all Platforms for the profile
            List<PlatformInfo> platforms = retrievePlatformsById(profile.getProfileId());



            //Setup response
            ProfileInfo profileType = RetrieveProfileTranslator.toProfileInfo(profile);
            profileType.setPlatforms(RetrieveProfileTranslator.toPlatformsList(platforms));

            logger.trace("retrieveAllProfiles:: {} operates on {}",profile.getName(), profileType.getPlatforms());
            response.add(profileType);
        });



        logger.trace("retrieveAllProfiles:: Response is set.");
        return response;

    }

    /**
     * Query Database for profiles with specified Id
     * @return List of profiles to be translated
     */
    public List<ProfileInfo> retrieveProfile(Integer id) {


        //Find all profiles in the database
        List<Profile> profileList = personalityRepository.findAllByProfileId(id);
        logger.trace("retrieveProfile():: Retrieved profile with ID: {} from Profile Database.", id);

        //List to hold all of our up-to-date info
        List<ProfileInfo> response = new ArrayList<>();

        //Determine platforms for each ProfileInfo
        if (!profileList.isEmpty()) {
            Profile profile = profileList.get(0);


            // Get all Platforms for the profile
            List<PlatformInfo> platforms = retrievePlatformsById(profile.getProfileId());


            //Setup response
            ProfileInfo profileType = RetrieveProfileTranslator.toProfileInfo(profile);
            profileType.setPlatforms(RetrieveProfileTranslator.toPlatformsList(platforms));

            logger.trace("retrieveProfile():: {} operates on {}", profile.getName(), profileType.getPlatforms());
            response.add(profileType);


            logger.trace("retrieveProfile():: Response is set.");

        } else {
            logger.error("retrieveProfile():: ID: {} was not found in the database", id);
        }

        return response;
    }

    /**
     * Retrieve the platforms the User is on by filtering with the ProfileId
     * @param id - ProfileId to filter the DB results
     * @return Array of the platforms the Celebrity posts on
     *
     * Future to cache response
     */
    public List<PlatformInfo> retrievePlatformsById(Integer id) {

        if(id != null){

            //List of results to be displayed
            List<PlatformInfo> responseList = new ArrayList<>();

            //Query Profile_Platform repository
            Iterable<ProfilePlatform> platforms = profilePlatformRepository.findAll();

            //Iterate through response
            platforms.forEach(plat ->{
                if(plat.getProfileId().equals(id)){

                    PlatformInfo platformInfo = new PlatformInfo();
                    platformInfo.setPlatform(Platform.getPlatformById(plat.getPlatformId()));
                    platformInfo.setPlatformKey(plat.getPlatformKey());

                    responseList.add(platformInfo);
                }
            });

            return responseList;
        }

        return null;

    }
}
