package com.jayway.rps.infrastructure;

public interface Handler<A> {

	<R> R  handle(Object target, A argument) throws Exception;
}
