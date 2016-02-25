package org.jato.core;

import org.jato.core.actor.JATOInstanceActor;
import org.jato.core.furture.JATOFuture;
import org.jato.core.furture.JATOFutureCallback;
import org.jato.core.message.MethodMessage;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * [类注释]
 *
 * @author gongjun
 * @since 2016-02-25 11:13
 */
public class Main {

    private int count = 0;

    public void test() {
        System.out.println("current thread: " + Thread.currentThread().getName() + " do test: " + count);
        count++;
    }

    public void test(int x) {
        System.out.println("current thread: " + Thread.currentThread().getName() + " do test: " + count);
        count += x;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        JATOActorFactory factory = JATO.createActorFactory();
        JATOInstanceActor<Main> actor = factory.startInstanceActor(new Main());
        actor.send(new MethodMessage("setCount", 0));
        actor.send(new MethodMessage("test"));
        actor.send(new MethodMessage("test"));
        actor.send(new MethodMessage("test"));
//        JATOFuture future = actor.getFuture(new MethodMessage("getCount"));
        actor.send(new MethodMessage("test"));
        actor.send(new MethodMessage("test"));
//        System.out.println(future.get());

        JATOInstanceActor<Main> actor2 = factory.startInstanceActor(new Main());
        actor2.send(new MethodMessage("setCount", 100));
        actor2.send(new MethodMessage("test"));
        actor2.send(new MethodMessage("test"));
        actor2.send(new MethodMessage("test"));
        actor2.send(new MethodMessage("test"));
        actor2.send(new MethodMessage("test"));

        JATOInstanceActor<ArrayList> actor3 = factory.startInstanceActor(new ArrayList());
        actor3.send(new MethodMessage("add", "x"));
        actor3.send(new MethodMessage("add", "y"));
        actor3.send(new MethodMessage("add", "z"));
        actor3.send(new MethodMessage("add", "111"));
        actor3.sendCallback(new MethodMessage("toString"), new JATOFutureCallback() {
            @Override
            public void onDone(Object result) {
                System.out.println(result);
            }
        });

        actor3.send(new MethodMessage("add", "222"));
        actor3.sendCallback(new MethodMessage("toString"), new JATOFutureCallback() {
            @Override
            public void onDone(Object result) {
                System.out.println(result);
            }
        });

        factory.shutdown();

//        System.out.println(future3.get());

    }
}
