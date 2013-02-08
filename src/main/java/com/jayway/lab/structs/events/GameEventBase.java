package com.jayway.lab.structs.events;

import java.util.UUID;

import com.jayway.lab.structs.Event;

public class GameEventBase implements Event {
	public final UUID gameId;

	public GameEventBase(UUID gameId) {
		this.gameId = gameId;
	}
}
