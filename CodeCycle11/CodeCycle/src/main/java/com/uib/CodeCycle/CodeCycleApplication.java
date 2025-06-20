package com.uib.CodeCycle;

import com.uib.CodeCycle.entities.UserEntity;
import com.uib.CodeCycle.entities.UserMission;
import com.uib.CodeCycle.entities.UserRole;
import com.uib.CodeCycle.services.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.stereotype.Repository;


@SpringBootApplication
public class CodeCycleApplication implements CommandLineRunner  {

	@Autowired
	private RepositoryRestConfiguration repositoryRestConfiguration ;

	public static void main(String[] args) {
		SpringApplication.run(CodeCycleApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		repositoryRestConfiguration.exposeIdsFor(UserEntity.class);
		repositoryRestConfiguration.exposeIdsFor(UserRole.class);
		repositoryRestConfiguration.exposeIdsFor(UserMission.class);

	}
/*
	@PostConstruct
	void init_users(){
		userService.addRole(new UserRole(null,"ADMIN","Responsable1"));
		userService.addRole(new UserRole(null,"USER","Responsable2"));

		userService.saveUser(new UserEntity(null,"amine","admin","omar@gmail","123",true,null));
		userService.saveUser(new UserEntity(null,"nadhem","kefi","kefi@gmail","123",true,null));
		userService.saveUser(new UserEntity(null,"yassine","gebsi","gebsi@gmail","123",true,null));

		userService.addRoleToUser("admin", "ADMIN");
		userService.addRoleToUser("admin", "USER");
		userService.addRoleToUser("kefi", "USER");
		userService.addRoleToUser("gebsi", "USER");
	}

*/

}
