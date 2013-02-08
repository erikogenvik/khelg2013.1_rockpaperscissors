package com.jayway.rps.store;

import java.util.List;
import java.util.UUID;

import com.jayway.rps.EventListener;
import com.jayway.rps.EventStore;
import com.jayway.rps.EventStream;
import com.jayway.rps.structs.Event;

public class DelegatingEventStore implements EventStore {

	private EventStore store;
	private EventListener[] listeners;

	public DelegatingEventStore(EventStore store,
			EventListener... eventListeners) {
		this.store = store;
		this.listeners = eventListeners;
	}

	@Override
	public EventStream loadEventStream(UUID streamId) {
		return store.loadEventStream(streamId);
	}

	@Override
	public void store(UUID streamId, long version, List<Event> events) {
		store.store(streamId, version, events);
		for (EventListener listener : listeners) {
			listener.receive(events);
		}
	}

	public void getAllEvents(UUID streamId, EventListener... listeners) {
		EventStream stream = store.loadEventStream(streamId);
		for (EventListener listener : listeners) {
			listener.receive(stream);
		}
	}
}
