package com.updateme.model.RetrieveProfileRs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({"profiles"})
public class RetrieveProfileResponse {
    @JsonProperty("profiles")
    private List<ProfileInfo> profiles;

    public List<ProfileInfo> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<ProfileInfo> profiles) {
        this.profiles = profiles;
    }
}
