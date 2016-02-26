package org.jato.core.actor;

import org.jato.core.furture.JATOFuture;
import org.jato.core.furture.JATOFutureCallback;
import org.jato.core.message.FutureMessage;
import org.jato.core.message.MethodMessage;
import org.jato.core.message.ObjectMessage;
import org.jetlang.channels.Channel;
import org.jetlang.core.Callback;
import org.jetlang.fibers.Fiber;

import java.util.concurrent.ExecutionException;

/**
 * [类注释]
 *
 * @author gongjun
 * @since 2016-02-25 11:28
 */
public abstract class JATOAbstractActor<T> implements JATOActor<T> {

    protected Channel<T> mailbox;

    protected Fiber fiber;

    protected JATOActorFactory actorFactory;

    protected Callback<T> callback;

/*
    JATOAbstractActor(JATOActorFactory actorFactory, Channel<T> mailbox, Fiber fiber, Callback<T> callback) {
        this.actorFactory = actorFactory;
        this.mailbox = mailbox;
        this.fiber = fiber;
        this.callback = callback;
        mailbox.subscribe(fiber, callback);
    }
*/

    public JATOAbstractActor(JATOActorFactory actorFactory, Channel<T> mailbox, Fiber fiber) {
        this.actorFactory = actorFactory;
        this.mailbox = mailbox;
        this.fiber = fiber;
        final JATOAbstractActor<T> self = this;
        this.callback = new Callback<T>() {
            public void onMessage(T t) {
                getActorFactory().addInstanceActorToCache(self, self);
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

    public JATOActorFactory getActorFactory() {
        return actorFactory;
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

    public JATOFuture getFuture(T message) {
        JATOFuture future = new JATOFuture();
        if (message instanceof FutureMessage) {
            ((FutureMessage) message).setFurture(future);
            send(message);
        }
        else {
            ObjectMessage objectMessage = new ObjectMessage();
            objectMessage.setObject(message);
            objectMessage.setFurture(future);
//            send(objectMessage);
        }

        return future;
    }


    public Object get(T message) {
        try {
            return getFuture(message).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void sendCallback(T message, JATOFutureCallback callback) {
        if (message instanceof FutureMessage) {
            ((FutureMessage) message).setFurture(callback);
        }
        mailbox.publish(message);
    }


    public Callback<T> getCallback() {
        return callback;
    }

    protected abstract void onReceive(T message);
}
