package com.jayway.lab.structs.commands;

import java.util.UUID;

public class JoinGame extends GameCommandBase {
	public final String playerId;

	public JoinGame(UUID gameId, String playerId) {
		super(gameId);
		this.playerId = playerId;
	}

}
