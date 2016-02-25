package org.jato.core;

import org.jato.core.actor.JATOActor;
import org.jato.core.actor.JATOInstanceActor;
import org.jato.core.message.MethodMessage;
import org.jetlang.channels.Channel;
import org.jetlang.channels.MemoryChannel;
import org.jetlang.fibers.Fiber;
import org.jetlang.fibers.PoolFiberFactory;
import org.jetlang.fibers.ThreadFiber;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * [类注释]
 *
 * @author gongjun
 * @since 2016-02-25 12:36
 */
public class JATO {

    public static JATOActorFactory createActorFactory() {
        return new JATOActorFactory();
    }

    public static <T> JATOInstanceActor<T> startInstanceActor(T instance) {
//        ExecutorService threds = Executors.newCachedThreadPool();
//        PoolFiberFactory fiberFactory = new PoolFiberFactory(threds);
        Fiber fiber = new ThreadFiber();
        fiber.start();

        Channel<MethodMessage> mailbox = new MemoryChannel<MethodMessage>();
        JATOInstanceActor<T> actor = new JATOInstanceActor<T>(instance, mailbox, fiber);
        return actor;
    }

    public static void dispose(JATOActor<?> actor) {
        actor.getFiber().dispose();
    }
}
