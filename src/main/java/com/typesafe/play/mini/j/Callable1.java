package com.typesafe.mini.j;

import java.util.concurrent.Callable;

/**
 * produces a callable that takes an argument in its call method
 * http://www.programmingforums.org/post187357-4.html
 */
public abstract class Callable1<TIn, TOut> {

    /**
     * provides the action for the callable. 
     * @param argument that's trasnformed into TOut
     * @return a TOut class
     */
    protected abstract TOut operateOn(TIn arg) throws Exception;
    
    /**
     * produces the callable 
     * @param argument that's trasnformed into TOut
     * @return a TOut class
     */
    public final Callable<TOut> pass(TIn arg) {
       final TIn foo = arg;  
       return  new Callable<TOut>() {
            public TOut call() throws Exception {
                return operateOn(foo);
            }
        };
    }
}