package com.jayway.rps.structs.commands;

import java.util.UUID;

import com.jayway.rps.Choices;

public class MakeChoice extends GameCommandBase {

	public final Choices choice;
	public final String playerId;

	public MakeChoice(UUID gameId, String playerId, Choices choice) {
		super(gameId);
		this.playerId = playerId;
		this.choice = choice;
	}
}
