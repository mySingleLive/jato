package org.jato.core;

import org.jato.core.actor.JATOActor;
import org.jato.core.actor.JATOActorFactory;
import org.jato.core.actor.JATOInstanceActor;
import org.jato.core.message.MethodMessage;
import org.jetlang.channels.Channel;
import org.jetlang.channels.MemoryChannel;
import org.jetlang.fibers.Fiber;
import org.jetlang.fibers.ThreadFiber;

/**
 * [类注释]
 *
 * @author gongjun
 * @since 2016-02-25 12:36
 */
public class JATO {

    public static JATOActorFactory actorFactory;


    public static JATOActorFactory createActorFactory() {
        if (actorFactory == null) {
            actorFactory = new JATOActorFactory();
        }
        return actorFactory;
    }

    public static JATOActor self() {
        return actorFactory.self();
    }

    public static void dispose(JATOActor<?> actor) {
        actor.getFiber().dispose();
    }
}
