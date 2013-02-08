package com.jayway.rps.infrastructure;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class ReflectionHandler<A> implements Handler<A> {
    private final Class<? extends Annotation> handlerAnnotation;

    public ReflectionHandler(Class<? extends Annotation> handlerAnnotation) {
        this.handlerAnnotation = handlerAnnotation;
    }

    @Override
    public <R> R handle(Object target, A argument) throws Exception {
        Method targetMethod = null;
        for(Method method : target.getClass().getMethods()) {
            if(method.isAnnotationPresent(handlerAnnotation)) {
                Class<?>[] parameterTypes = method.getParameterTypes();

                if(parameterTypes.length == 1 && parameterTypes[0] == argument.getClass()) {
                    targetMethod = method;
                    break;
                }
            }
        }
        return (R) targetMethod.invoke(target, argument);
    }
}