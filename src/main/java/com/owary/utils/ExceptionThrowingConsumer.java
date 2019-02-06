package com.owary.utils;

import java.util.function.Consumer;

@FunctionalInterface
public interface ExceptionThrowingConsumer<T> extends Consumer<T> {

    /*
     * Explanation to self.
     * Since 'acceptThrows' is the exposed method, it works like 'accept' but also throws an Exception
     * However, in Consumer interface, for a lambda expression, the called method is 'accept',
     * so it is run, if exception occurred, it is rethrown as RuntimeException
     * */

    @Override
    default void accept(T t){
        try{
            acceptThrows(t);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    void acceptThrows(T t) throws Exception;
}
