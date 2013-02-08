package com.jayway.rps.structs.commands;

import java.util.UUID;

public class MakeChoice extends GameCommandBase {

	public final String choice;
	public final String playerId;

	public MakeChoice(UUID gameId, String playerId, String choice) {
		super(gameId);
		this.playerId = playerId;
		this.choice = choice;
	}
}
