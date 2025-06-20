package com.uib.CodeCycle.repos;

import com.uib.CodeCycle.entities.UserEntity;
import com.uib.CodeCycle.entities.UserMission;
import com.uib.CodeCycle.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findByFirstName (String firstName);
    UserEntity findByLastName(String lastName);
    UserEntity findByEmail(String email);
    List<UserEntity> findByFirstNameContains (String firstName);

    /*@Query("select u from UserEntity u where u.firstName like %?1 and u.lastName like %?2")
    List<UserEntity> findByFirstNameLastName (String nom, String prenom);*/

/*    List<UserEntity> findByFirstNameLastName (@Param("nom") String nom, @Param("prenom") String prenom);
*/

    @Query("""
            select u
            from UserEntity u
            where u.firstName like concat('%', :nom, '%')
            and u.lastName  like concat('%', :prenom, '%') 
            """)
    List<UserEntity> findByFirstAndLastName(@Param("nom") String nom,
                                            @Param("prenom") String prenom);



    List<UserEntity> findByUserRoles (UserRole userRole);

    List<UserEntity> findByUserRolesId(Long roleId);


    List<UserEntity> findByUserMission_Id(Long missionId);






}


