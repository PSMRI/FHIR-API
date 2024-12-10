package com.wipro.fhir.repo.user;

import org.springframework.data.repository.CrudRepository;

import com.wipro.fhir.data.users.User;

public interface UserRepository extends CrudRepository<User, Long> {
	
	User findByUserID(Long userID);

}
