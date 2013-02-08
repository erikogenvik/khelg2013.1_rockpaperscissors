package com.jayway.rps.structs.commands;

import java.util.UUID;

import com.jayway.rps.structs.Command;

public class GameCommandBase implements Command {
	public final UUID gameId;

	public GameCommandBase(UUID gameId) {
		this.gameId = gameId;
	}

	public UUID entityId() {
		return gameId;
	}
}
