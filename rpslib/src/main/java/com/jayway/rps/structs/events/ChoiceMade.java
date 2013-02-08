package com.jayway.rps.structs.events;

import java.util.UUID;

import com.jayway.rps.Choices;

public class ChoiceMade extends GameEventBase {
	public final int roundNumber;
	public final Choices choice;
	public final String playerId;

	public ChoiceMade(UUID gameId, int roundNumber, String playerId,
			Choices choice) {
		super(gameId);
		this.playerId = playerId;
		this.roundNumber = roundNumber;
		this.choice = choice;
	}

}
