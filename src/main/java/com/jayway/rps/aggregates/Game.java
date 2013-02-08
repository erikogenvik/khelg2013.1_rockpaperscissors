package com.jayway.rps.aggregates;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.jayway.rps.CommandHandler;
import com.jayway.rps.EventHandler;
import com.jayway.rps.structs.Event;
import com.jayway.rps.structs.commands.CreateGame;
import com.jayway.rps.structs.commands.JoinGame;
import com.jayway.rps.structs.commands.MakeChoice;
import com.jayway.rps.structs.events.ChoiceMade;
import com.jayway.rps.structs.events.GameCreated;
import com.jayway.rps.structs.events.GameStarted;
import com.jayway.rps.structs.events.RoundStarted;

public class Game {

	private UUID id;
	private String player1Id;
	private String player2Id;
	private int roundNumber;

	private int player1Wins;
	private int player2Wins;

	private String player1Choice;
	private String player2Choice;

	@EventHandler
	public void handle(GameCreated e) throws Exception {
		id = e.gameId;
		player1Id = e.playerId;
	}

	@CommandHandler
	public List<? extends Event> handle(CreateGame c) throws Exception {

		if (id == null) {
			return Arrays
					.asList(new GameCreated(UUID.randomUUID(), c.playerId));
		}

		return Arrays.asList();
	}

	@EventHandler
	public void handle(GameStarted e) throws Exception {
		player2Id = e.player2Id;
	}

	@EventHandler
	public void handle(RoundStarted e) throws Exception {
		roundNumber = e.roundNumber;
	}

	@CommandHandler
	public List<? extends Event> handle(JoinGame c) throws Exception {

		if (id == null) {
			return Arrays.asList();
		}

		if (player2Id == null) {
			return Arrays.asList(new GameStarted(id, player1Id, player2Id),
					new RoundStarted(id, 1));
		}

		return Arrays.asList();
	}

	@EventHandler
	public void handle(ChoiceMade e) throws Exception {
		if (e.playerId.equals(player1Id)) {
			player1Choice = e.choice;
		} else {
			player2Choice = e.choice;
		}

		if (player1Choice != null && player2Choice != null) {

		}
	}

	@CommandHandler
	public List<? extends Event> handle(MakeChoice c) throws Exception {

		if (id == null) {
			return Arrays.asList();
		}

		if (player2Id == null) {
			return Arrays.asList();
		}

		return Arrays.asList(new ChoiceMade(id, roundNumber, c.playerId,
				c.choice));
	}

}
