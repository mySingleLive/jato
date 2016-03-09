package org.jato.core.furture;

import java.util.concurrent.ExecutionException;

/**
 * [类注释]
 *
 * @author gongjun
 * @since 2016-02-25 14:10
 */
public abstract class JATOFutureCallback<T> extends JATOFuture<T> {

    public abstract void onDone(T result);

    @Override
    public void set(T result) throws InterruptedException {
        if (done) {
            return;
        }
        this.result = result;
        this.done = true;
        onDone(result);
    }

}

