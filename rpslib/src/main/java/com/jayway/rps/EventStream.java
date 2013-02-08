package com.jayway.rps;

import com.jayway.rps.structs.Event;

public interface EventStream extends Iterable<Event> {
	long version();

}