package com.jayway.rps;

import com.jayway.rps.structs.Event;

public interface EventListener {

	void receive(Iterable<Event> events);
}
