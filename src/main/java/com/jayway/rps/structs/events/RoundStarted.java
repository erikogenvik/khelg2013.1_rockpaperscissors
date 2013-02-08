package com.jayway.rps.structs.events;

import java.util.UUID;

public class RoundStarted extends GameEventBase {
	public final int roundNumber;

	public RoundStarted(UUID gameId, int roundNumber) {
		super(gameId);
		this.roundNumber = roundNumber;
	}

}
