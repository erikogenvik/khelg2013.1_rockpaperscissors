package com.jayway.rps.infrastructure;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.jayway.rps.CommandHandler;
import com.jayway.rps.structs.Event;

public class ReflectionHandlerTest {

	static class MyHandler {

		@CommandHandler
		public List<? extends Event> handle(String c) throws Exception {
			return null;
		}

		@CommandHandler
		public List<? extends Event> handle(Integer c) throws Exception {
			return new ArrayList<Event>();
		}

	}

	@Test
	public void test() throws Exception {
		ReflectionHandler<Integer> reflectionHandler = new ReflectionHandler<Integer>(
				CommandHandler.class);
		Object handle = reflectionHandler.handle(new MyHandler(), new Integer(
				42));
		Assert.assertNotNull(handle);

	}
}
