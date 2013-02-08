package com.jayway.rps.projection;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.jayway.rps.EventListener;
import com.jayway.rps.structs.Event;
import com.jayway.rps.structs.events.GameCreated;
import com.jayway.rps.structs.events.GameStarted;

public class OpenGameProjection implements EventListener {

	class Entry {
		public String playerId;
		public int requiredWins;
	}

	private Map<UUID, Entry> openGames = new HashMap<UUID, Entry>();

	@Override
	public void receive(Iterable<Event> events) {
		for (Event event : events) {
			if (event instanceof GameCreated) {
				Entry entry = new Entry();
				entry.playerId = ((GameCreated) event).playerId;
				entry.requiredWins = ((GameCreated) event).requiredWins;
				openGames.put(((GameCreated) event).gameId, entry);
			} else if (event instanceof GameStarted) {
				openGames.remove(((GameStarted) event).gameId);
			}
		}
	}

	public Map<UUID, Entry> getGames() {
		return openGames;
	}
}