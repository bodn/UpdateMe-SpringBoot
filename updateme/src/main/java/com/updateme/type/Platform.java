package com.updateme.type;

/**
 * Simple enum to map DB platform Id's to corresponding Strings
 */
public enum Platform {

    TWITCH  ("Twitch",1),
    TWITTER ("Twitter", 2),
    FACEBOOK("Facebook",3),
    YOUTUBE ("Youtube",4);

    private final String platformName;
    private final int platformId;


    Platform(String platformName, int platformId) {
        this.platformId = platformId;
        this.platformName=platformName;
    }


    /**
     * Get the Platform name by the Id
     * @param id
     * @return
     */
    public static String getById(int id) {
        for(Platform e : values()) {
            if(e.platformId == id){
                return e.toString();
            }
        }
        return null;
    }

    public static Platform getPlatformById(int id) {
        for(Platform e : values()) {
            if(e.platformId == id){
                return e;
            }
        }
        return null;
    }

    public static int getId(Platform platform) {
        return platform.platformId;
    }

    public String toString(){
        return platformName;
    }
}
