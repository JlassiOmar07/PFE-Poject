package com.uib.CodeCycle.services;

import com.uib.CodeCycle.entities.UserEntity;
import com.uib.CodeCycle.entities.UserMission;
import com.uib.CodeCycle.entities.UserRole;
import com.uib.CodeCycle.repos.MissionRepository;
import com.uib.CodeCycle.repos.RoleRepository;
import com.uib.CodeCycle.repos.UserEntityRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserEntityRepository userEntityRepository;

    @Autowired
    RoleRepository roleRepository ;



    @Autowired
    MissionRepository missionRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    // Ajouter cette méthode privée pour valider le format de l'email
    private void validateEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (email == null || !email.matches(emailRegex)) {
            throw new IllegalArgumentException("Format d'email invalide: " + email);
        }
    }

    @Override
    public UserEntity saveUser(UserEntity user) {
        // Valider le format de l'email
        validateEmail(user.getEmail());
        
        UserEntity existingUser = userEntityRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            throw new IllegalArgumentException("Un utilisateur avec l'email '" + user.getEmail() + "' existe déjà");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userEntityRepository.save(user);
    }




    @Override
    @Transactional
    public UserEntity saveUserWithRole(UserEntity user, String roleName) {
        // Valider le format de l'email
        validateEmail(user.getEmail());
        
        // Vérifier si l'email existe déjà
        UserEntity existingUser = userEntityRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            throw new IllegalArgumentException("Un utilisateur avec l'email '" + user.getEmail() + "' existe déjà");
        }

        // 1) Encoder le mot de passe
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        // 2) Récupérer le rôle en base, ou lever une exception si introuvable
        UserRole role = roleRepository
                .findByName(roleName);

        // 3) Ajouter le rôle à l'utilisateur (la collection userRoles doit être initialisée dans l'entité)
        user.getUserRoles().add(role);

        // 4) Sauvegarder l'utilisateur et retourner l'entité persistée
        return userEntityRepository.save(user);
    }

    @Override
    public UserEntity updateUser(UserEntity user) {
        // Récupérer l'utilisateur existant avec ses rôles
        UserEntity existingUser = userEntityRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + user.getId()));
        
        // Valider le format de l'email si l'email a été modifié
        if (user.getEmail() != null && !user.getEmail().equals(existingUser.getEmail())) {
            validateEmail(user.getEmail());
            
            // Vérifier si le nouvel email existe déjà pour un autre utilisateur
            UserEntity userWithSameEmail = userEntityRepository.findByEmail(user.getEmail());
            if (userWithSameEmail != null && !userWithSameEmail.getId().equals(user.getId())) {
                throw new IllegalArgumentException("Un utilisateur avec l'email '" + user.getEmail() + "' existe déjà");
            }
        }
        
        // Préserver les collections existantes si elles ne sont pas fournies
        if (user.getUserRoles() == null || user.getUserRoles().isEmpty()) {
            user.setUserRoles(existingUser.getUserRoles());
        }
        if (user.getUserMission() == null || user.getUserMission().isEmpty()) {
            user.setUserMission(existingUser.getUserMission());
        }
        // Si le mot de passe est vide ou null, conserver l'ancien
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(existingUser.getPassword());
        } else {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        return userEntityRepository.save(user);
    }




    @Override
    public UserMission updateMission(UserMission mission) {
        // Récupérer la mission existante
        UserMission existingMission = missionRepository.findById(mission.getId())
            .orElseThrow(() -> new EntityNotFoundException("Mission not found with id: " + mission.getId()));
        
        // Préserver la liste des utilisateurs si elle n'est pas fournie
        if (mission.getUsers() == null || mission.getUsers().isEmpty()) {
            mission.setUsers(existingMission.getUsers());
        }
        
        // Mettre à jour et sauvegarder
        return missionRepository.save(mission);
    }


    @Override
    public void deleteUserBuyId(Long id) {
        UserEntity user = userEntityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User introuvable : " + id));
        user.getUserRoles().clear();
        user.getUserMission().clear();
        userEntityRepository.delete(user);
    }

    @Override
    @Transactional
    public void deleteUserMissionById(Long id) {
        UserMission mission = missionRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Mission not found with id: " + id));
        
        // Supprimer les références à cette mission dans tous les utilisateurs
        List<UserEntity> users = userEntityRepository.findByUserMission_Id(id);
        for (UserEntity user : users) {
            user.getUserMission().removeIf(m -> m.getId().equals(id));
            userEntityRepository.save(user);
        }
        
        // Maintenant supprimer la mission
        missionRepository.delete(mission);
    }


    @Override
    public UserEntity getUser(Long id) {
        return userEntityRepository.findById(id).get();
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return userEntityRepository.findAll();
    }

    @Override
    public UserRole addRole(UserRole role) {
        return roleRepository.save(role);
    }

    @Override
    public UserMission addMission(UserMission mission) {
        return missionRepository.save(mission);
    }

    @Override
    public UserEntity addRoleToUser(String email, String rolename) {

        UserEntity usr = userEntityRepository.findByEmail(email);
        UserRole role = roleRepository.findByName(rolename);

        usr.getUserRoles().add(role);

        userEntityRepository.save(usr);

        return usr;
    }

    @Override
    public UserEntity addMissionToUser(Long idUser, String idexigence) {
        UserEntity usr = userEntityRepository.getById(idUser);
        UserMission mission = missionRepository.findByIdExigence(idexigence);

        usr.getUserMission().add(mission);

        userEntityRepository.save(usr);

        return usr;
    }

    @Override
    public UserEntity findUserByLastName(String lastName) {
        return userEntityRepository.findByLastName(lastName);
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userEntityRepository.findByEmail(email);
    }

    @Override
    public List<UserEntity> findByFirstName(String nom) {
        return userEntityRepository.findByFirstName(nom);
    }

    @Override
    public List<UserEntity> findByFirstNameContains(String nom) {
        return userEntityRepository.findByFirstNameContains(nom);
    }

    @Override
    public List<UserEntity> findByFirstNameLastName(String nom, String prenom) {
        return userEntityRepository.findByFirstAndLastName(nom,prenom);
    }

    @Override
    public List<UserEntity> findByUserRole(UserRole userRole) {
        return userEntityRepository.findByUserRoles(userRole);
    }

    @Override
    public List<UserEntity> findByUserRoleIdRole(Long id) {
        return userEntityRepository.findByUserRolesId(id);
    }



   @Override
    public List<UserEntity> findByUserMissionIdMission(Long missionId) {
        System.out.println("Recherche d'utilisateurs pour la mission ID: " + missionId);
        List<UserEntity> users = userEntityRepository.findByUserMission_Id(missionId);
        System.out.println("Nombre d'utilisateurs trouvés: " + users.size());
        return users;
    }

    @Override
    public List<UserMission>  findAllMissionOfUser(String email){
        return missionRepository.findByUsers_Email(email);
    }

    @Override
    @Transactional
    public UserMission completeMission(Long missionId) {
        UserMission mission = missionRepository.findById(missionId)
            .orElseThrow(() -> new EntityNotFoundException("Mission not found with id: " + missionId));
        
        mission.setCompleted(true);
        return missionRepository.save(mission);
    }

}
