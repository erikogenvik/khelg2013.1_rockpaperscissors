package com.jayway.lab.structs.commands;

import java.util.UUID;

import com.jayway.lab.structs.Command;

public class GameCommandBase implements Command {
	public final UUID gameId;

	public GameCommandBase(UUID gameId) {
		this.gameId = gameId;
	}

	public UUID entityId() {
		return gameId;
	}
}
