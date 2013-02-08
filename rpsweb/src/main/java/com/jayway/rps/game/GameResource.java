package com.jayway.rps.game;

import java.util.UUID;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.jayway.rps.ApplicationService;
import com.jayway.rps.Choices;
import com.jayway.rps.projection.GameDetailsProjection;
import com.jayway.rps.projection.GameDetailsProjection.Entry;
import com.jayway.rps.structs.commands.CreateGame;
import com.jayway.rps.structs.commands.JoinGame;
import com.jayway.rps.structs.commands.MakeChoice;

@Path("/games")
@Component
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class GameResource {

	@Autowired
	ApplicationService appService;

	@Autowired
	GameDetailsProjection gameDetails;

	@POST
	public Response createGame(@FormParam("firstTo") int firstTo,
			@Context UriInfo uriInfo) {
		if (firstTo == 0) {
			throw new IllegalArgumentException();
		}
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		UUID id = UUID.randomUUID();
		appService
				.handle(new CreateGame(id, authentication.getName(), firstTo));
		return Response.created(
				uriInfo.getAbsolutePathBuilder().path(id.toString()).build())
				.build();
	}

	@POST
	@Path("{id}/opponent")
	public Response joinGame(@PathParam("id") UUID id, @Context UriInfo uriInfo) {
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();

		appService.handle(new JoinGame(id, authentication.getName()));
			return Response.created(
				uriInfo.getBaseUriBuilder().path(GameResource.class)
						.path(id.toString()).build()).build();
	}

	@POST
	@Path("{id}/choice")
	public Response choice(@PathParam("id") UUID id,
			@FormParam("choice") Choices choice, @Context UriInfo uriInfo) {

		if (choice == null) {
			throw new IllegalArgumentException();
		}
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();

		appService.handle(new MakeChoice(id, authentication.getName(), choice));
		return Response.ok(
				uriInfo.getBaseUriBuilder().path(GameResource.class)
						.path(id.toString()).build()).build();
	}

	@GET
	@Path("{id}")
	public Response getGame(@PathParam("id") UUID id, @Context UriInfo uriInfo) {
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		Entry entry = gameDetails.getGames().get(id);
		if (entry == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

		GameDTO dto = new GameDTO();
		dto.details = entry;
		if (entry.player2Id == null) {
			dto.join = uriInfo.getAbsolutePathBuilder().path("opponent")
					.build();
		} else if (entry.winner == null) {
			dto.choice = uriInfo.getAbsolutePathBuilder().path("choice")
					.build();
		}
		dto.me = authentication.getName();
		return Response.ok().entity(dto).build();
	}
}
