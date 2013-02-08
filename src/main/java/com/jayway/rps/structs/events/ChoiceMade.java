package com.jayway.rps.structs.events;

import java.util.UUID;

public class ChoiceMade extends GameEventBase {
	public final int roundNumber;
	public final String choice;
	public final String playerId;

	public ChoiceMade(UUID gameId, int roundNumber, String playerId,
			String choice) {
		super(gameId);
		this.playerId = playerId;
		this.roundNumber = roundNumber;
		this.choice = choice;
	}

}
