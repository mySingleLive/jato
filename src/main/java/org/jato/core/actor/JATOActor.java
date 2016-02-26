package org.jato.core.actor;

import org.jato.core.furture.JATOFuture;
import org.jato.core.furture.JATOFutureCallback;
import org.jetlang.channels.Channel;
import org.jetlang.core.Callback;
import org.jetlang.fibers.Fiber;

/**
 * [类注释]
 *
 * @author gongjun
 * @since 2016-02-25 11:13
 */
public interface JATOActor<T> {

    Channel<T> getMailbox();

    Fiber getFiber();

    Callback<T> getCallback();

    void send(T message);

    void sendCallback(T message, JATOFutureCallback callback);

    JATOFuture getFuture(T message);

    Object get(T message);
}
