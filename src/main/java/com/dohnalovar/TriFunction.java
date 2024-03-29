package com.dohnalovar;

/**
 * Created by dohnalovar on 6/10/2019
 */
@FunctionalInterface
public interface TriFunction<T, U, S, R> {
    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @param s the third function argument
     * @return the function result
     */
    R applyAsInt(T t, U u, S s);
}
