package com.jayway.rps.game;

import java.net.URI;

import com.jayway.rps.projection.GameDetailsProjection.Entry;

public class GameDTO {
	
	public Entry details;
	
	public URI join;
	public URI choice;
	public String me;

}
