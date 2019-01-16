package com.updateme.controller;

import com.updateme.model.RetrievePostReq.PostFilter;
import com.updateme.model.RetrievePostRs.RetrievePostResponse;
import com.updateme.service.retrieve.RetrievePostService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;


@Controller
public class RetrievePostsController {

    private static final Logger logger = LogManager.getLogger(RetrievePostsController.class);

    private RetrievePostService retrievePostService;

    @Autowired
    public RetrievePostsController (RetrievePostService retrievePostService){
        this.retrievePostService= retrievePostService;
    }


    /**
     * Endpoint to return the Data for specified profile id
     * **ERROR RESPONSE****
     * @return JSON Data for the Posts  in the Database for the id
     */
    @RequestMapping(value = "/profile/{id}/posts", method = RequestMethod.GET)
    public ResponseEntity<RetrievePostResponse> retrievePosts (@PathVariable("id") String id){

        logger.info("retrievePosts():: /profile/{}/posts endpoint has been requested.",id);
        RetrievePostResponse response = new RetrievePostResponse();
        response.setPosts(new ArrayList<>());

        if ("null".equals(id) || id == null) {
            logger.error("retrievePosts():: Id parameter is set to null. Returning an empty list");
            response.setPosts(new ArrayList<>());
        } else {
            response = retrievePostService.retrievePosts(new Integer(id));
            logger.info("retrievePosts():: Response set for ID: {}.",id);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Endpoint to return the Data for specified profile id for the platforms filtered
     * **ERROR RESPONSE****
     * @return JSON Data for the Posts  in the Database for the id
     */
    @RequestMapping(value = "/profile/{id}/posts/filter", method = RequestMethod.GET)
    public ResponseEntity<RetrievePostResponse> retrievePostsWithFilter (@PathVariable("id") String id , PostFilter postFilter){

        logger.info("retrievePostsWithFilter():: /profile/{}/posts? with filters endpoint has been requested.",id);

        RetrievePostResponse response = new RetrievePostResponse();
        response.setPosts(new ArrayList<>());
        if ("null".equals(id) || id == null) {
            logger.error("retrievePostsWithFilter():: Id parameter is set to null.");
            response.setPosts(new ArrayList<>());
        } else {
            response = retrievePostService.retrievePostsFromFilter(new Integer(id), postFilter);
            logger.info("retrievePostsWithFilter():: Response set for ID: {}.",id);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}


