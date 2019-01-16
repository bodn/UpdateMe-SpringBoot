package com.updateme.repository;

import com.updateme.entity.ProfilePlatform;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProfilePlatformRepository extends CrudRepository<ProfilePlatform, Integer> {

    List<ProfilePlatform> findAllByProfileId(Integer id);
}
