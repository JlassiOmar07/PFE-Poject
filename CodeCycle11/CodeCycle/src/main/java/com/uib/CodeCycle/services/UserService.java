package com.uib.CodeCycle.services;

import com.uib.CodeCycle.entities.UserEntity;
import com.uib.CodeCycle.entities.UserMission;
import com.uib.CodeCycle.entities.UserRole;

import java.util.List;

public interface UserService {
    UserEntity saveUser(UserEntity user);
    UserEntity saveUserWithRole(UserEntity user, String roleName);
    UserEntity updateUser(UserEntity user);

   // UserMission updateMission(UserMission);

   // UserMission updateMission(UserMission mission);

    UserMission updateMission(UserMission mission);

    void deleteUserBuyId(Long id);

    void deleteUserMissionById(Long id);
    UserEntity getUser(Long id);
    List<UserEntity> getAllUsers();
    //nouveau
    UserRole addRole (UserRole role);

    UserMission addMission (UserMission mission);
    UserEntity addRoleToUser (String email , String role);

    UserEntity addMissionToUser (Long idUser , String idexigence);
    //****

    UserEntity findUserByLastName (String lastName);
    UserEntity findByEmail(String email);

    List<UserEntity> findByFirstName(String nom);
    List<UserEntity> findByFirstNameContains(String nom);
    List<UserEntity> findByFirstNameLastName (String nom, String prenom);
    List<UserEntity> findByUserRole (UserRole userRole);
    List<UserEntity> findByUserRoleIdRole(Long id);


    List<UserEntity> findByUserMissionIdMission(Long missionId);

    List<UserMission> findAllMissionOfUser(String email);

    /**
     * Marque une mission comme terminée
     * @param missionId ID de la mission
     * @return La mission mise à jour
     */
    UserMission completeMission(Long missionId);

}
