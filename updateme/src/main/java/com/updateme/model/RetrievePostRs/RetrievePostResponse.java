package com.updateme.model.RetrievePostRs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


import java.util.List;

@JsonPropertyOrder({"posts"})
public class RetrievePostResponse {

    @JsonProperty("posts")
    private List<PostInfoResponse> posts;

    public List<PostInfoResponse> getPosts() {
        return posts;
    }

    public void setPosts(List<PostInfoResponse> posts) {
        this.posts = posts;
    }
}
