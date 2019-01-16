package com.updateme.controller;

import com.updateme.model.RetrieveProfileRs.ProfileInfo;
import com.updateme.model.RetrieveProfileRs.RetrieveProfileResponse;
import com.updateme.service.access.YoutubeAccessService;
import com.updateme.service.retrieve.RetrieveProfileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
// Import log4j classes.
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.List;

@RestController
public class RetrieveProfileController {


    private RetrieveProfileService retrieveProfileService;

    @Autowired
    private YoutubeAccessService youtubeAccessService;


    private static final Logger logger = LogManager.getLogger(RetrieveProfileController.class);

    @Autowired
    public RetrieveProfileController(RetrieveProfileService retrieveProfileService){
        this.retrieveProfileService = retrieveProfileService;
    }


    /**
     * Endpoint to return the Data for all profiles in the service
     * **FUTURE ADD PARAMETERS TO LOAD CERTAIN AREAS
     * @return JSON Data for the Profiles in the Database
     */
    @RequestMapping(value = "/profiles/all", method = RequestMethod.GET)
    public ResponseEntity<RetrieveProfileResponse> retrieveAllProfiles(){
        logger.info("retrieveAllProfiles:: /profiles/all endpoint has been requested.");
        RetrieveProfileResponse response = new RetrieveProfileResponse();

        List<ProfileInfo> profiles = retrieveProfileService.retrieveAllProfiles();
        logger.info("retrieveAllProfiles:: Response is set, returning all profiles information.");
        response.setProfiles(profiles);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * Endpoint to return the Data for specified profile id
     * @Todo **ERROR RESPONSE****
     * @return JSON Data for the Profiles in the Database
     */
    @RequestMapping(value = "/profiles/{id}", method = RequestMethod.GET)
    public ResponseEntity<RetrieveProfileResponse> retrieveProfile (@PathVariable("id") Integer id){
        logger.info("retrieveProfile:: /profiles/{} endpoint has been requested.",id);
        RetrieveProfileResponse response = new RetrieveProfileResponse();
        if(id != null){


            List<ProfileInfo> profiles = retrieveProfileService.retrieveProfile(id);

            response.setProfiles(profiles);

        }
        else {
            logger.error("retrieveProfile():: Id parameter is set to null.");
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @RequestMapping(value = "/ping")
    public ResponseEntity<String> ping(){
        return new ResponseEntity<>("Hello world!", HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/test")
    public ResponseEntity<String> test(){
        youtubeAccessService.getYoutubeVideos("UC-lHJZR3Gqxm24_Vd_AJ5Yw");
        return new ResponseEntity<>("test success!", HttpStatus.OK);
    }
}
