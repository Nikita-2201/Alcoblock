package ru.gknsv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gknsv.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

	User findUserByLogin(String login);
	User findUserById(String id);
	void deleteUserById(String id);
}
