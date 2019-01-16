package com.updateme.repository;

import com.updateme.entity.PostInfo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostInfoRepository extends CrudRepository<PostInfo, Integer> {

    List<PostInfo> findByProfileIdAndPlatformId(Integer profileId, Integer platformId);

    List<PostInfo> findByProfileId(Integer profileId);

    List<PostInfo> findFirstByProfileIdOrderByPostCreatedDesc(Integer profileId);

}
