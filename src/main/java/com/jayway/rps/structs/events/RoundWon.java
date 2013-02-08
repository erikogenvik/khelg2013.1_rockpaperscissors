package com.jayway.rps.structs.events;

import java.util.UUID;

public class RoundWon extends GameEventBase {
	public final int roundNumber;
	public final String playerWinner;

	public RoundWon(UUID gameId, int roundNumber, String playerWinner) {
		super(gameId);
		this.roundNumber = roundNumber;
		this.playerWinner = playerWinner;
	}

}
