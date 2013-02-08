package com.jayway.rps.structs.events;

import java.util.UUID;

import com.jayway.rps.structs.Event;

public class GameEventBase implements Event {
	public final UUID gameId;

	public GameEventBase(UUID gameId) {
		this.gameId = gameId;
	}
}
