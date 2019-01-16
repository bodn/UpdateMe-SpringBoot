package com.updateme.model.RetrievePostReq;


/**
 * POJO to hold the request parameters for /profiles/{}/posts?
 */
public class PostFilter {

    private boolean twitter;

    private boolean twitch;

    private boolean youtube;

    private boolean facebook;

    public boolean getTwitter() {
        return twitter;
    }

    public boolean getTwitch() {
        return twitch;
    }

    public boolean getYoutube() {
        return youtube;
    }

    public boolean getFacebook() {
        return facebook;
    }

    public void setTwitter(boolean twitter) {
        this.twitter = twitter;
    }

    public void setTwitch(boolean twitch) {
        this.twitch = twitch;
    }

    public void setYoutube(boolean youtube) {
        this.youtube = youtube;
    }

    public void setFacebook(boolean facebook) {
        this.facebook = facebook;
    }

    @Override
    public String toString() {
        return "PostFilter{" +
                "twitter=" + twitter +
                ", twitch=" + twitch +
                ", youtube=" + youtube +
                ", facebook=" + facebook +
                '}';
    }
}
