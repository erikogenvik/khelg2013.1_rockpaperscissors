package com.jayway.rps;

public interface Handler<A> {
	<R> R handle(Object target, A argument) throws Exception;
}