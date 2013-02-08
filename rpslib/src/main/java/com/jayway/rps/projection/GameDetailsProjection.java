package com.jayway.rps.projection;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.jayway.rps.EventListener;
import com.jayway.rps.structs.Event;
import com.jayway.rps.structs.events.ChoiceMade;
import com.jayway.rps.structs.events.GameCreated;
import com.jayway.rps.structs.events.GameEventBase;
import com.jayway.rps.structs.events.GameStarted;
import com.jayway.rps.structs.events.GameWon;
import com.jayway.rps.structs.events.RoundStarted;
import com.jayway.rps.structs.events.RoundWon;

public class GameDetailsProjection implements EventListener {
	public static class Entry {
		public String player1Id;
		public String player2Id;
		public int requiredWins;
		public int player1Score;
		public int player2Score;
		public int currentRound;
		public boolean player1Dealt;
		public boolean player2Dealt;
		public String winner;
	}

	private Map<UUID, Entry> games = new HashMap<UUID, Entry>();

	@Override
	public void receive(Iterable<Event> events) {
		for (Event event : events) {
			if (event instanceof GameCreated) {
				Entry entry = new Entry();
				entry.player1Id = ((GameCreated) event).playerId;
				entry.requiredWins = ((GameCreated) event).requiredWins;
				games.put(((GameCreated) event).gameId, entry);
			} else if (event instanceof GameEventBase) {
				Entry entry = games.get(((GameEventBase) event).gameId);

				if (event instanceof RoundStarted) {
					entry.currentRound++;
					entry.player1Dealt = false;
					entry.player2Dealt = false;
				} else if (event instanceof ChoiceMade) {
					if (((ChoiceMade) event).playerId == entry.player1Id) {
						entry.player1Dealt = true;
					} else {
						entry.player2Dealt = true;
					}
				} else if (event instanceof RoundWon) {
					if (((RoundWon) event).playerWinner == entry.player1Id) {
						entry.player1Score++;
					} else {
						entry.player2Score++;
					}
				} else if (event instanceof GameWon) {
					entry.winner = ((GameWon) event).playerWinner;
				} else if (event instanceof GameStarted) {
					entry.player2Id = ((GameStarted) event).player2Id;
				}
			}
		}
	}

	public Map<UUID, Entry> getGames() {
		return games;
	}

}
