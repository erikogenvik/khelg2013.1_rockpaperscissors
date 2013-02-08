package com.jayway.rps.app;

import java.util.Map;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import com.jayway.rps.Choices;
import com.jayway.rps.EventStream;
import com.jayway.rps.aggregates.Game;
import com.jayway.rps.projection.GameDetailsProjection;
import com.jayway.rps.projection.GameDetailsProjection.Entry;
import com.jayway.rps.projection.OpenGameProjection;
import com.jayway.rps.store.DelegatingEventStore;
import com.jayway.rps.store.InMemoryEventStore;
import com.jayway.rps.structs.Event;
import com.jayway.rps.structs.commands.CreateGame;
import com.jayway.rps.structs.commands.JoinGame;
import com.jayway.rps.structs.commands.MakeChoice;
import com.jayway.rps.structs.events.GameWon;

public class ApplicationServiceImplTest {

	@Test
	public void test() {

		InMemoryEventStore eventStore = new InMemoryEventStore();

		ApplicationServiceImpl app = new ApplicationServiceImpl(eventStore,
				Game.class);

		UUID id = UUID.randomUUID();

		app.handle(new CreateGame(id, "1", 2));

		app.handle(new JoinGame(id, "2"));

		app.handle(new MakeChoice(id, "1", Choices.ROCK));
		app.handle(new MakeChoice(id, "2", Choices.SCISSOR));
		app.handle(new MakeChoice(id, "1", Choices.ROCK));
		app.handle(new MakeChoice(id, "2", Choices.SCISSOR));

		EventStream stream = eventStore.loadEventStream(id);
		boolean foundWin = false;
		for (Event event : stream) {
			if (event instanceof GameWon) {
				Assert.assertEquals("1", ((GameWon) event).playerWinner);
				foundWin = true;
			}
		}

		Assert.assertTrue(foundWin);
	}

	@Test
	public void testProjection() {
		OpenGameProjection openGames = new OpenGameProjection();
		GameDetailsProjection gameDetails = new GameDetailsProjection();
		DelegatingEventStore eventStore = new DelegatingEventStore(
				new InMemoryEventStore(), openGames, gameDetails);

		ApplicationServiceImpl app = new ApplicationServiceImpl(eventStore,
				Game.class);
		UUID id = UUID.randomUUID();

		app.handle(new CreateGame(id, "1", 2));
		Assert.assertEquals(1, openGames.getGames().size());

		app.handle(new JoinGame(id, "2"));
		Assert.assertEquals(0, openGames.getGames().size());

		app.handle(new CreateGame(UUID.randomUUID(), "1", 2));
		Assert.assertEquals(1, openGames.getGames().size());

		app.handle(new MakeChoice(id, "1", Choices.ROCK));
		app.handle(new MakeChoice(id, "2", Choices.SCISSOR));
		app.handle(new MakeChoice(id, "1", Choices.ROCK));

		{
			Map<UUID, Entry> games = gameDetails.getGames();
			Assert.assertEquals(2, games.get(id).currentRound);
			Assert.assertFalse(games.get(id).player2Dealt);
			Assert.assertTrue(games.get(id).player1Dealt);
		}

		app.handle(new MakeChoice(id, "2", Choices.SCISSOR));

	}

}
