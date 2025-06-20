package com.uib.CodeCycle.restcontrollers;

import com.uib.CodeCycle.entities.UserEntity;
import com.uib.CodeCycle.entities.UserMission;
import com.uib.CodeCycle.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class UserRESTController {
    @Autowired
    UserService userService ;



    @RequestMapping(path = "all", method = RequestMethod.GET)
    public List<UserEntity> getAllUsers(){
        return userService.getAllUsers();
    }

    @RequestMapping(value = "/getbyid/{id}",method = RequestMethod.GET)
    public UserEntity getUserById(@PathVariable("id") Long id){
        return userService.getUser(id);
    }

    @RequestMapping(path = "/adduser",method = RequestMethod.POST)
    public UserEntity createUser(@RequestBody UserEntity user){
        return userService.saveUser(user);
    }

    @RequestMapping(path = "/addmission", method = RequestMethod.POST)
    public UserMission createMission (@RequestBody UserMission mission){
        return userService.addMission(mission);
    }

    @RequestMapping(path = "/updateuser",method = RequestMethod.PUT)
    public UserEntity updateUser (@RequestBody UserEntity user){
        return userService.updateUser(user);
    }

    @RequestMapping(path = "/updatemission", method = RequestMethod.PUT)
    public UserMission updateMission(@RequestBody UserMission mission) {
        return userService.updateMission(mission);
    }

    @RequestMapping(value = "/deluser/{id}",method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable("id") Long id){
       userService.deleteUserBuyId(id);

    }

    @RequestMapping(value = "/delMission/{id}",method = RequestMethod.DELETE)
    public void deleteMission(@PathVariable("id") Long id){
        userService.deleteUserMissionById(id);
    }

    @RequestMapping(value = "/byroles/{id}",method = RequestMethod.GET)
    public List<UserEntity> getUsersByRoleId(@PathVariable("id") Long id){
        return userService.findByUserRoleIdRole(id);
    }

    @RequestMapping(value = "/rolesByName/{name}",method = RequestMethod.GET)
    public List<UserEntity> findByNameUserContains(@PathVariable("name") String name){
        return userService.findByFirstNameContains(name);
    }

    @RequestMapping(value = "user/{email}/roles/{rolename}",method = RequestMethod.POST)
    public UserEntity addRoleToUser( @PathVariable("email") String email,
                                     @PathVariable("rolename") String rolename){
        return userService.addRoleToUser(email,rolename);
    }

    @RequestMapping(value = "user/{iduser}/mission/{idexigence}",method = RequestMethod.POST)
    public UserEntity addMissionToUser( @PathVariable("iduser") Long iduser,
                                     @PathVariable("idexigence") String idexigence){
        return userService.addMissionToUser(iduser,idexigence);
    }

    @RequestMapping(value ="/usersMissions/{id}",method = RequestMethod.GET)
    public List<UserEntity> getUsersByMissions(@PathVariable ("id") Long missionId ){
        return userService.findByUserMissionIdMission(missionId) ;
    }

    @RequestMapping(value = "/userMission/{email}", method = RequestMethod.GET)
    public List<UserMission> getAllMissionUser(@PathVariable("email") String email){
        return userService.findAllMissionOfUser(email);
    }

    @RequestMapping(value = "/user/email/{email}", method = RequestMethod.GET)
    public ResponseEntity<UserEntity> getUserByEmail(@PathVariable("email") String email) {
        UserEntity user = userService.findByEmail(email);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/checkEmail/{email}", method = RequestMethod.GET)
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable("email") String email) {
        UserEntity user = userService.findByEmail(email);
        boolean exists = (user != null);
        return ResponseEntity.ok(exists);
    }
/*
    public List<UserMission> getMissionsByUser (@PathVariable Long userId){
        return userService.getMissionsForUser(use)
    }

*/

}
