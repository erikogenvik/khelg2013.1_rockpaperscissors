package com.jayway.rps.structs.events;

import java.util.UUID;

public class GameCreated extends GameEventBase {
	public final String playerId;
	public final int requiredWins;

	public GameCreated(UUID gameId, String playerId, int requiredWins) {
		super(gameId);
		this.playerId = playerId;
		this.requiredWins = requiredWins;
	}

}
