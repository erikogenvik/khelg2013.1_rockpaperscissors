package com.jayway.rps.structs.commands;

import java.util.UUID;

public class CreateGame extends GameCommandBase {

	public final String playerId;
	public final int requiredRounds;

	public CreateGame(UUID gameId, String playerId, int requiredRounds) {
		super(gameId);
		this.playerId = playerId;
		this.requiredRounds = requiredRounds;
	}

}
