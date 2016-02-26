package org.jato.core.actor;

import com.sun.org.apache.xpath.internal.functions.FuncRound;
import org.jato.core.actor.JATOActor;
import org.jato.core.actor.JATOCallbackActor;
import org.jato.core.actor.JATOInstanceActor;
import org.jato.core.message.FutureMessage;
import org.jato.core.message.MethodMessage;
import org.jato.core.message.ObjectMessage;
import org.jetlang.channels.Channel;
import org.jetlang.channels.MemoryChannel;
import org.jetlang.core.Callback;
import org.jetlang.fibers.Fiber;
import org.jetlang.fibers.PoolFiberFactory;
import org.jetlang.fibers.ThreadFiber;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author gongjun
 * @since 2016-02-25 15:31
 */
public class JATOActorFactory {

    private ExecutorService executorService = Executors.newCachedThreadPool();
    private PoolFiberFactory fiberFactory = new PoolFiberFactory(executorService);
    private Map<Thread, JATOActor> instanceActorCache = new HashMap<Thread, JATOActor>();

    private Fiber startFiber() {
        Fiber fiber = fiberFactory.create();
        fiber.start();
        return fiber;
    }

    void addInstanceActorToCache(Thread thread, JATOActor actor) {
        instanceActorCache.put(thread, actor);
    }

    public <T> JATOCallbackActor<T> startCallbackActor(Callback<T> callback) {
        Fiber fiber = startFiber();
        Channel<T> mailbox = new MemoryChannel<T>();
        JATOCallbackActor<T> actor = new JATOCallbackActor<T>(this, mailbox, fiber, callback);
        return actor;

    }

    public <T> JATOInstanceActor<T> startInstanceActor(T instance) {
        Fiber fiber = startFiber();
        Channel<MethodMessage> mailbox = new MemoryChannel<MethodMessage>();
        JATOInstanceActor<T> actor = new JATOInstanceActor<T>(this, instance, mailbox, fiber);
        return actor;
    }

    public JATOActor actor(Object object) {
        return instanceActorCache.get(object);
    }

    public void shutdown() {
        instanceActorCache.clear();
        fiberFactory.dispose();
        try {
            executorService.shutdown(); // Disable new tasks from being submitted

            // Wait a while for existing tasks to terminate
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow(); // Cancel currently executing tasks
            }

        } catch (RejectedExecutionException ex) {
            // (Re-)Cancel if current thread also interrupted
            executorService.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            executorService.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }

}
