package com.jayway.rps.structs.commands;

import java.util.UUID;

public class CreateGame extends GameCommandBase {

	public final String playerId;

	public CreateGame(UUID gameId, String playerId) {
		super(gameId);
		this.playerId = playerId;
	}

}
