package org.jato.core.furture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * [类注释]
 *
 * @author gongjun
 * @since 2016-02-25 13:22
 */
public class JATOFuture<T> implements Future<T> {

    protected T result;

    protected boolean done = false;

    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    public boolean isCancelled() {
        return false;
    }

    public boolean isDone() {
        return done;
    }

    public synchronized T get() throws InterruptedException, ExecutionException {
        while (!isDone()) {
            wait();
        }
        return result;
    }

    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }

    public synchronized void set(T result) throws InterruptedException {
        if (done) {
            return;
        }
        this.result = result;
        this.done = true;
    }

}
