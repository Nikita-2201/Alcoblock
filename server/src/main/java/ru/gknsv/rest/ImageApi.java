package ru.gknsv.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.gknsv.model.Image;
import ru.gknsv.service.ImageService;
import java.util.NoSuchElementException;

@Component
@Path("/images")
@Api("Image Api")
public class ImageApi {

    @Autowired
    private ImageService imageService;

    @GET
    @Path("/{imageId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Чтение картинок")
    public Response read(@PathParam("imageId") String id){
        try{
            Image image = imageService.getImageForIcon(id);
            return Response.ok().entity(image).build();
        } catch (NoSuchElementException e){
            return Response.status(404).build();
        }
    }
}
