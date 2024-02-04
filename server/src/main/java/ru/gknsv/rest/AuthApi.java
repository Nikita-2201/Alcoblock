package ru.gknsv.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.gknsv.model.User;
import ru.gknsv.service.UserService;

@Component
@Path("/auth/")
@Api("Auth Api")
public class AuthApi {
	private final Logger logger = LoggerFactory.getLogger(UserApi.class);

	@Autowired
	private UserService userService;

	@POST
	@Path("signup")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation("Регистрация")
	public Response signup(User user) {
		try {
			user = userService.updateUser(user);
			if (user != null) {
				user.setPassword(null);
			}
			return user == null ? Response.status(409).build() : Response.ok().entity(user).build();
		} catch (Exception e) {
			logger.error("User update error: ", e);
			return Response.status(500).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path("signin")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation("Авторизация")
	public Response signin(User user) {
		user = userService.login(user);
		if (user != null) {
			user.setPassword(null);
		}
		return user == null ? Response.status(401).build() : Response.ok().entity(user).build();
	}
}
