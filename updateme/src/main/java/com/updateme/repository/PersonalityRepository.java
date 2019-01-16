package com.updateme.repository;

import com.updateme.entity.Profile;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PersonalityRepository extends CrudRepository<Profile, String> {

    List<Profile> findAllByProfileId(Integer profileId);

}
