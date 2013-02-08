package com.jayway.lab.structs.events;

import java.util.UUID;

public class RoundTied extends GameEventBase {
	public final int roundNumber;

	public RoundTied(UUID gameId, int roundNumber) {
		super(gameId);
		this.roundNumber = roundNumber;
	}

}
