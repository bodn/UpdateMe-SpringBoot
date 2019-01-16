package com.updateme.translator;

import com.updateme.entity.Profile;
import com.updateme.model.PlatformInfo;
import com.updateme.model.RetrieveProfileRs.ProfileInfo;
import com.updateme.type.Platform;
import com.updateme.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class RetrieveProfileTranslator {

    public static ProfileInfo toProfileInfo( Profile profile){

                ProfileInfo profileType = new ProfileInfo();
                profileType.setTitle(profile.getTitle());
                profileType.setFollowerCount(StringUtils.toFormatedNumber(profile.getFollowerCount()));
                profileType.setName(profile.getName());
                profileType.setmPlatform(Platform.getById(profile.getMainPlatform()));
                profileType.setId(profile.getProfileId());
                profileType.setProfileImage(profile.getProfileImage());
                profileType.setLastModified(profile.getLastActive());


        return profileType;
    }

    public static List<String> toPlatformsList(List<PlatformInfo> platformInfoList){
        if(platformInfoList != null){

            List<String> platforms = new ArrayList<>();

            platformInfoList.forEach(platformInfo -> {
                platforms.add(platformInfo.getPlatform().toString());
            });

            return platforms;
        }

        return null;
    }

}
