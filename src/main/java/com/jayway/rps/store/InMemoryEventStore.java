package com.jayway.rps.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.jayway.rps.EventStore;
import com.jayway.rps.EventStream;
import com.jayway.rps.structs.Event;

public class InMemoryEventStore implements EventStore {

	class Entry {
		public long version;
		public List<Event> events;
	}

	Map<UUID, Entry> eventMap = new HashMap<UUID, Entry>();

	public EventStream loadEventStream(UUID streamId) {
		if (eventMap.containsKey(streamId)) {
			final Entry entry = eventMap.get(streamId);
			return new EventStream() {

				public Iterator<Event> iterator() {
					return entry.events.iterator();
				}

				public long version() {
					return entry.version;
				}

			};
		}
		return null;
	}

	public void store(UUID streamId, long version, List<Event> events) {
		if (eventMap.containsKey(streamId)) {
			Entry entry = eventMap.get(streamId);
			if (entry.version != version) {
				throw new RuntimeException("Wrong version!");
			}
			entry.events.addAll(events);
			entry.version++;

		} else {
			Entry entry = new Entry();
			entry.events = new ArrayList<Event>(events);
			entry.version = version;
			eventMap.put(streamId, entry);
		}
	}
}
