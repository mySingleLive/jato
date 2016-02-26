package org.jato.core;

import org.jato.core.actor.JATOInstanceActor;
import org.jato.core.message.MethodMessage;
import org.jetlang.channels.Channel;
import org.jetlang.channels.MemoryChannel;
import org.jetlang.fibers.Fiber;
import org.jetlang.fibers.PoolFiberFactory;
import org.jetlang.fibers.ThreadFiber;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

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

    public <T> JATOInstanceActor<T> startInstanceActor(T instance) {
        Fiber fiber = fiberFactory.create();
        fiber.start();

        Channel<MethodMessage> mailbox = new MemoryChannel<MethodMessage>();
        JATOInstanceActor<T> actor = new JATOInstanceActor<T>(instance, mailbox, fiber);
        return actor;
    }

    public void shutdown() {
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
