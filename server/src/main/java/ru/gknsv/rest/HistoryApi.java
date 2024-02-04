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
import ru.gknsv.model.History;
import ru.gknsv.service.HistoryService;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Component
@Path("/history")
@Api("History Api")
public class HistoryApi {
    public static final Logger LOG = LoggerFactory.getLogger(HistoryApi.class);

    @Autowired
    HistoryService historyService;

    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Чтение истории")
    public Response read(@PathParam("userId") String userId) {
        try {
            List history = historyService.getHistory(userId);
            LOG.info("API: Список историй {}", history);
            return Response.ok().entity(history).build();
        } catch (NoSuchElementException e) {
            return Response.status(404).build();
        }
    }

    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Добавление истории")
    public Response addHistory(History newHistory) {
        try {
            long calculator;

            Date date = new Date();
            newHistory.setDate(date);
            History lastHistory = historyService.getLastHistory(newHistory.getUserId());

            if (lastHistory == null) {
                calculator = 0;
            } else {
                calculator = historyService.calculationDate(lastHistory, date);
            }

            lastHistory = historyService.addHistory(newHistory);
            LOG.info("API: Информация о истории {}", lastHistory);

            return lastHistory == null ? Response.status(409).build() : Response.ok().entity(calculator).build();
        } catch (NoSuchElementException e) {
            return Response.status(404).build();
        }
    }
}
