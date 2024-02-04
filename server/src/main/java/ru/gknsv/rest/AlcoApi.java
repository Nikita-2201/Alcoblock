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
import ru.gknsv.model.Alco;
import ru.gknsv.service.AlcoService;
import java.util.List;
import java.util.NoSuchElementException;

@Component
@Path("/alco")
@Api("Alco Api")
public class AlcoApi {
	private final Logger logger = LoggerFactory.getLogger(AlcoApi.class);

	@Autowired
	private AlcoService alcoService;

	@GET
	@Path("{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation("Получение алкоголя (общего и своего)")
	public Response readAll(@PathParam("userId") String userId) {
		try {
			List<Alco> alco = alcoService.getAlco(userId);
			return Response.ok().entity(alco).build();
		} catch (NoSuchElementException e) {
			return Response.status(404).build();
		}
	}

	@POST
	@Path("add")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation("Добавление персонального алкоголя")
	public Response addPersonalAlco(Alco alco) {
		try {
			alco = alcoService.addPersonalAlco(alco);
			return alco == null ? Response.status(409).build() : Response.ok().entity(alco).build();
		} catch (Exception e) {
			logger.error("Alco added error: ", e);
			return Response.status(500).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path("delete/{alcoId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation("Удаление персонального алкоголя")
	public Response delete(@PathParam("alcoId") String alcoId) {
		try {
			alcoService.deletePersonalAlco(alcoId);
			return Response.ok("Successfuly").build();
		} catch (Exception e) {
			logger.error("Alco delete error: ", e);
			return Response.status(500).entity(e.getMessage()).build();
		}
	}
}
