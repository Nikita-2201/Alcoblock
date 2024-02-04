package ru.gknsv.service;

import ru.gknsv.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.gknsv.model.User;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	@Transactional
	public User updateUser(final User user) {
		if (checkDuplicates(user)) {
			encodePassword(user);
			User saved = userRepository.save(user);
			return saved;
		} else return null;
	}

	@Transactional
	public void deleteUser(String userId) {
		if (getUserById(userId) != null) {
			userRepository.deleteUserById(userId);
		}
	}

	public User getUserById(String userId) {
		return userRepository.findUserById(userId);
	}

	public User getUserByLogin(String login) {
		return userRepository.findUserByLogin(login);
	}

	public User login(final User user) {
		User userFromDB = decodePassword(user);
		return userFromDB != null ? userFromDB : null;
	}

	private boolean checkDuplicates(final User user) {
		String userId = user.getId();
		String userLogin = user.getLogin();
		if (userId == null) {
			if (userRepository.findUserByLogin(userLogin) == null) {
				return true;
			}
		} else {
			if (userRepository.findUserById(userId) == userRepository.findUserByLogin(userLogin) || userRepository.findUserByLogin(userLogin) == null) {
				return true;
			}
		}
		return false;
	}

	private void encodePassword(User user) {
		if (StringUtils.isNotBlank(user.getId()) && StringUtils.isBlank(user.getPassword())) {
			userRepository.findById(user.getId()).ifPresent(oldUser -> user.setPassword(oldUser.getPassword()));
		} else {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			user.setPassword(encoder.encode(user.getPassword()));
		}
	}

	private User decodePassword(final User user) {
		User userFromDB = getUserByLogin(user.getLogin());
		if (userFromDB != null) {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			if (encoder.matches(user.getPassword(), userFromDB.getPassword())){
				return userFromDB;
			}
		}
		return null;
	}
}
