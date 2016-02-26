package org.jato.core.actor;

import org.jetlang.channels.Channel;
import org.jetlang.core.Callback;
import org.jetlang.fibers.Fiber;

/**
 * [类注释]
 *
 * @author gongjun
 * @since 2016-02-26 13:21
 */
public class JATOCallbackActor<T> extends JATOAbstractActor<T> implements JATOActor<T> {

    private Callback<T> realCallback;

    public JATOCallbackActor(JATOActorFactory actorFactory, Channel<T> mailbox, Fiber fiber, Callback<T> callback) {
        super(actorFactory, mailbox, fiber);
        this.realCallback = callback;
    }

    @Override
    protected void onReceive(T message) {
        realCallback.onMessage(message);
    }


}
