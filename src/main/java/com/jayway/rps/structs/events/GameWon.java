package com.jayway.rps.structs.events;

import java.util.UUID;

public class GameWon extends GameEventBase {
	public final String playerWinner;

	public GameWon(UUID gameId, String playerWinner) {
		super(gameId);
		this.playerWinner = playerWinner;
	}

}
