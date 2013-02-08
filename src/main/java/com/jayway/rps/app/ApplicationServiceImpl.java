package com.jayway.rps.app;

import java.util.List;

import com.jayway.rps.ApplicationService;
import com.jayway.rps.CommandHandler;
import com.jayway.rps.EventHandler;
import com.jayway.rps.EventStore;
import com.jayway.rps.EventStream;
import com.jayway.rps.infrastructure.ReflectionHandler;
import com.jayway.rps.structs.Command;
import com.jayway.rps.structs.Event;

public class ApplicationServiceImpl implements ApplicationService {

	private EventStore eventStore;
	private Class<?> aggregateType;
	private ReflectionHandler<Command> commandHandler;
	private ReflectionHandler<Event> eventHandler;

	public ApplicationServiceImpl(EventStore eventStore, Class<?> aggregateType) {
		this.eventStore = eventStore;
		this.aggregateType = aggregateType;
		this.commandHandler = new ReflectionHandler<Command>(
				CommandHandler.class);
		this.eventHandler = new ReflectionHandler<Event>(EventHandler.class);
	}

	public void handle(Command command) {

		EventStream stream = eventStore.loadEventStream(command.entityId());
		try {
			Object aggregate = aggregateType.newInstance();

			if (stream != null) {
				for (Event event : stream) {
					eventHandler.handle(aggregate, event);
				}
			}

			List<Event> newEvents = commandHandler.handle(aggregate, command);

			for (Event event : newEvents) {
				eventHandler.handle(aggregate, event);
			}

			long version = stream == null ? 0 : stream.version();
			eventStore.store(command.entityId(), version, newEvents);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
}
