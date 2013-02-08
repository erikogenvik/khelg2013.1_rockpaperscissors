package com.jayway.rps.structs.events;

import java.util.UUID;

public class RoundStarted extends GameEventBase {

	public RoundStarted(UUID gameId) {
		super(gameId);
	}

}
