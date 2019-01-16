![](https://i.imgur.com/PLO7QFp.png)
# UpdateMe-SpringBoot
Spring Boot back end component of the UpdateMe system.<br/>
<br/>
UpdateMe is an application to deliver you the activity of your favourite Youtuber/Livestreamer from different social media platforms
under one simple interface. Gone are the days of bouncing between different applications just to get caught up.

*Check out the [UpdateMe-ReactNative](https://github.com/bodn/UpdateMe-ReactNative) front end component*

#### Libraries Used
Spring Boot Framework, Google Gson, Log4j2, MySQL

## Endpoints
### Retrieve Profiles
* `/v1/profiles/all` - Retrieves all profiles stored in the database
* `/v1/profiles/{id}`- Retrieves the general information for profile with profile_id = {id}

### Retrieve Posts
* `/v1/profiles/{id}/posts` - Retrieves all posts for the profile {id}
* `/v1/profiles/{id}/posts/filter?{parameters}` - retrieves posts from platforms specified in the {parameters}
    * An example of a valid call would be `/v1/profiles/{id}/posts/filter?youtube=true&twitch=false` - this would return posts only from YouTube for the profile of {id}
    
    
## TO DO
- [ ] Remove unused dependencies
- [ ] Integrate with Twitter API
- [ ] Implement Webhooks to replace polling the external APIs


## Contact
*if you are interested in looking at the architectural and dynamic views of the system please contact me :) *



