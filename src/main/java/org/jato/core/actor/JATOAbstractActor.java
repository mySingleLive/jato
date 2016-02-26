package org.jato.core.actor;

import org.jato.core.furture.JATOFutureCallback;
import org.jato.core.message.MethodMessage;
import org.jetlang.channels.Channel;
import org.jetlang.core.Callback;
import org.jetlang.fibers.Fiber;

/**
 * [类注释]
 *
 * @author gongjun
 * @since 2016-02-25 11:28
 */
public abstract class JATOAbstractActor<T> implements JATOActor<T> {

    protected Channel<T> mailbox;

    protected Fiber fiber;

    protected Callback<T> callback;

    public JATOAbstractActor(Channel<T> mailbox, Fiber fiber, Callback<T> callback) {
        this.mailbox = mailbox;
        this.fiber = fiber;
        this.callback = callback;
        mailbox.subscribe(fiber, callback);
    }

    public JATOAbstractActor(Channel<T> mailbox, Fiber fiber) {
        this.mailbox = mailbox;
        this.fiber = fiber;
        final JATOAbstractActor<T> self = this;
        this.callback = new Callback<T>() {
            public void onMessage(T t) {
                self.onReceive(t);
            }
        };
        mailbox.subscribe(fiber, callback);
    }


    public Channel<T> getMailbox() {
        return mailbox;
    }

    public Fiber getFiber() {
        return fiber;
    }

    public void send(T message) {
        mailbox.publish(message);
    }

    public void sendMethod(String name) {
        send((T) new MethodMessage(name));
    }

    public void sendMethod(String name, Object... args) {
        send((T) new MethodMessage(name, args));
    }

    public Callback<T> getCallback() {
        return callback;
    }

    protected abstract void onReceive(T message);
}
