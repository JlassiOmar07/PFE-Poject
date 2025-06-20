package com.uib.CodeCycle.repos;

import com.uib.CodeCycle.entities.UserEntity;
import com.uib.CodeCycle.entities.UserMission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;


@RepositoryRestResource(path = "mission")
@CrossOrigin("http://localhost:4200/")
public interface MissionRepository extends JpaRepository<UserMission, Long> {

    UserMission findByIdExigence(String idExigence);

   /* @Query("""
      SELECT m
      FROM UserEntity u
      JOIN u.userMission m
      WHERE u.id = :userId
      """)
    List<UserMission> findByUserId(@Param("userId") Long userId);*/


    List<UserMission> findByUsers_Email(String email);
}
