package com.jayway.rps.aggregates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.jayway.rps.Choices;
import com.jayway.rps.CommandHandler;
import com.jayway.rps.EventHandler;
import com.jayway.rps.Result;
import com.jayway.rps.structs.Event;
import com.jayway.rps.structs.commands.CreateGame;
import com.jayway.rps.structs.commands.JoinGame;
import com.jayway.rps.structs.commands.MakeChoice;
import com.jayway.rps.structs.events.ChoiceMade;
import com.jayway.rps.structs.events.GameCreated;
import com.jayway.rps.structs.events.GameStarted;
import com.jayway.rps.structs.events.GameWon;
import com.jayway.rps.structs.events.RoundStarted;
import com.jayway.rps.structs.events.RoundTied;
import com.jayway.rps.structs.events.RoundWon;

public class Game {

	private UUID id;
	private String player1Id;
	private String player2Id;
	private int roundNumber;
	private int requiredRounds;

	private int player1Wins;
	private int player2Wins;

	private Choices player1Choice;
	private Choices player2Choice;

	@EventHandler
	public void handle(GameCreated e) throws Exception {
		id = e.gameId;
		player1Id = e.playerId;
		player1Wins = 0;
		requiredRounds = e.requiredWins;
	}

	@CommandHandler
	public List<? extends Event> handle(CreateGame c) throws Exception {

		if (id == null) {
			return Arrays.asList(new GameCreated(UUID.randomUUID(), c.playerId,
					c.requiredRounds));
		}

		return Arrays.asList();
	}

	@EventHandler
	public void handle(GameStarted e) throws Exception {
		player2Id = e.player2Id;
		player2Wins = 0;
	}

	@EventHandler
	public void handle(RoundStarted e) throws Exception {
		roundNumber++;
		player1Choice = null;
		player2Choice = null;
	}

	@CommandHandler
	public List<? extends Event> handle(JoinGame c) throws Exception {

		if (id == null) {
			return Arrays.asList();
		}

		if (player2Id == null) {
			return Arrays.asList(new GameStarted(id, player1Id, player2Id),
					new RoundStarted(id));
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
	}

	@CommandHandler
	public List<? extends Event> handle(MakeChoice c) throws Exception {

		if (id == null) {
			return Arrays.asList();
		}

		if (player2Id == null) {
			return Arrays.asList();
		}

		Choices p1Choice = player1Choice;
		Choices p2Choice = player2Choice;

		if (c.playerId.equals(player1Id)) {
			p1Choice = c.choice;
		} else {
			p2Choice = c.choice;
		}

		List<Event> events = new ArrayList<Event>();
		events.add(new ChoiceMade(id, roundNumber, c.playerId, c.choice));

		if (p1Choice != null && p2Choice != null) {
			Result result = compareChoices(p1Choice, p2Choice);
			if (result == Result.WIN) {
				roundWon(events, player1Id);
			} else if (result == Result.TIE) {
				events.add(new RoundTied(id, roundNumber));
				events.add(new RoundStarted(id));
			} else {
				roundWon(events, player2Id);
			}
		}

		return events;
	}

	private void roundWon(List<Event> events, String playerId) {
		events.add(new RoundWon(id, roundNumber, playerId));

		if (player1Wins == requiredRounds - 1 && player1Id.equals(playerId)) {
			events.add(new GameWon(id, player1Id));
		} else if (player2Wins == requiredRounds - 1
				&& player2Id.equals(playerId)) {
			events.add(new GameWon(id, player2Id));
		} else {
			events.add(new RoundStarted(id));
		}

	}

	static Result compareChoices(Choices choice1, Choices choice2) {
		if (choice1.equals(choice2))
			return Result.TIE;
		if (choice1 == Choices.ROCK && choice2 == Choices.SCISSOR) {
			return Result.WIN;
		}
		return Result.LOSE;
	}

}
