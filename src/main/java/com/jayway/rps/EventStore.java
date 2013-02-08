package com.jayway.rps;

import java.util.List;
import java.util.UUID;

import com.jayway.rps.structs.Event;

public interface EventStore {
	EventStream loadEventStream(UUID streamId);
	void store(UUID streamId, long version, List<Event> events);
}

