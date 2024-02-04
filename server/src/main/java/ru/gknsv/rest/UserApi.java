package ru.gknsv.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.gknsv.model.User;
import ru.gknsv.service.UserService;
import java.util.NoSuchElementException;

@Component
@Path("/user")
@Api("User Api")
public class UserApi {

	private final Logger logger = LoggerFactory.getLogger(UserApi.class);

	@Autowired
	private UserService userService;

	@POST
	@Path("update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation("Редактирование пользователя")
	public Response update(User user) {
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

	@GET
	@Path("{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation("Чтение пользователя")
	public Response read(@PathParam("userId") String userId) {
		try {
			User user = userService.getUserById(userId);
			user.setPassword(null);
			return Response.ok().entity(user).build();
		} catch (NoSuchElementException e) {
			return Response.status(404).build();
		}
	}

	@GET
	@Path("delete/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation("Удаление пользователя")
	public Response delete(@PathParam("userId") String userId) {
		try {
			userService.deleteUser(userId);
			return Response.ok().build();
		} catch (Exception e) {
			logger.error("User delete error: ", e);
			return Response.status(500).entity(e.getMessage()).build();
		}
	}
}
