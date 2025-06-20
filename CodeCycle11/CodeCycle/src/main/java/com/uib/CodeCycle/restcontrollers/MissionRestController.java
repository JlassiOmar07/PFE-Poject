package com.uib.CodeCycle.restcontrollers;

import com.uib.CodeCycle.entities.UserMission;
import com.uib.CodeCycle.entities.UserRole;
import com.uib.CodeCycle.repos.MissionRepository;
import com.uib.CodeCycle.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mission")
@CrossOrigin("*")
public class MissionRestController {
    @Autowired
    MissionRepository missionRepository;
    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public List<UserMission> getAllMission(){
        return missionRepository.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public UserMission getMissionById(@PathVariable("id") Long id){
        return missionRepository.findById(id).get();
    }

    /**
     * Marque une mission comme terminée
     * @param id ID de la mission
     * @return La mission mise à jour
     */
    @RequestMapping(value = "/{id}/complete", method = RequestMethod.PUT)
    public UserMission completeMission(@PathVariable("id") Long id) {
           return  userService.completeMission(id);


    }


}
