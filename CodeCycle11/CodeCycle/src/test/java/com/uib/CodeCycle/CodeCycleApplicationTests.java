package com.uib.CodeCycle;

import com.uib.CodeCycle.entities.UserEntity;
import com.uib.CodeCycle.entities.UserRole;
import com.uib.CodeCycle.repos.UserEntityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CodeCycleApplicationTests {

	@Autowired
	private UserEntityRepository userEntityRepository ;

	@Test
	public void testCreateUser(){
		UserEntity user = new UserEntity();
		userEntityRepository.save(user);
	}

	@Test
	public void testFindUser(){
		UserEntity user = userEntityRepository.findById(1L).get();
		System.out.println(user);
	}

	@Test
	public void testUpdateUser(){
		UserEntity user = userEntityRepository.findById(1L).get();
		user.setEmail("ilupo097@gmail.com");
		userEntityRepository.save(user);
	}

	@Test
	public void testDeleteUser(){
		userEntityRepository.deleteById(2L);

	}

	@Test
	public void testListerTousUsers(){
		List<UserEntity> users = userEntityRepository.findAll();
		for (UserEntity user : users){
			System.out.println(user);
		}
	}

	@Test
	public void testFindByNomUser(){
		List<UserEntity> users = userEntityRepository.findByFirstName("omar");
			for (UserEntity user : users){
				System.out.println(user);
			}
	}

	@Test
	public void testFindByNomUserContains(){
		List<UserEntity> users = userEntityRepository.findByFirstNameContains("om");
		for (UserEntity user : users){
			System.out.println(user);
		}
	}

	@Test
	public void findByFirstNameLastName (){
		List<UserEntity> users = userEntityRepository.findByFirstAndLastName("zeyneb","jelassi");
		for (UserEntity u: users){
			System.out.println(u);
		}
	}

	@Test
	public void testFindByUserRole (){
		UserRole userRole = new UserRole();
		userRole.setId(1L);
		List<UserEntity> users = userEntityRepository.findByUserRoles(userRole);
		for (UserEntity u : users){
			System.out.println(u);
		}
	}

	@Test
	public void testfindByRoleIdRole(){
		List<UserEntity> users = userEntityRepository.findByUserRolesId(2L);
		for (UserEntity u : users){
			System.out.println(u);
		}
	}

}
