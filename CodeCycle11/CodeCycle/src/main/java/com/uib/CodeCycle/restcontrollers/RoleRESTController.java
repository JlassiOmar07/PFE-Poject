package com.uib.CodeCycle.restcontrollers;

import com.uib.CodeCycle.entities.UserRole;
import com.uib.CodeCycle.repos.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role")
@CrossOrigin("*")
public class  RoleRESTController {
    @Autowired
    RoleRepository roleRepository;

    @RequestMapping(method = RequestMethod.GET)
    public List<UserRole> getAllRoles(){
        return roleRepository.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public UserRole getRoleById(@PathVariable("id") Long id){
        return roleRepository.findById(id).get();
    }




}
