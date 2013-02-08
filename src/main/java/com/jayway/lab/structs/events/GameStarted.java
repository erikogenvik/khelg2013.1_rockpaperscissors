package com.jayway.lab.structs.events;

import java.util.UUID;

public class GameStarted extends GameEventBase {
	public final String player1Id;
	public final String player2Id;

	public GameStarted(UUID gameId, String player1Id, String player2Id) {
		super(gameId);
		this.player1Id = player1Id;
		this.player2Id = player2Id;
	}

}
