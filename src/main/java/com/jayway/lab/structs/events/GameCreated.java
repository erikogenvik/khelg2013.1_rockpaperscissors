package com.jayway.lab.structs.events;

import java.util.UUID;

public class GameCreated extends GameEventBase {
	public final String playerId;

	public GameCreated(UUID gameId, String playerId) {
		super(gameId);
		this.playerId = playerId;
	}

}
