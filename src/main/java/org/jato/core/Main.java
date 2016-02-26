package org.jato.core;

import org.jato.core.actor.JATOInstanceActor;
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
        actor.sendMethod("setCount", 0);
        actor.sendMethod("test");
        actor.sendMethod("test");
        actor.sendMethod("test");
        actor.sendMethod("test");
        actor.sendMethod("test");
//        System.out.println(future.get());
        factory.startInstanceActor(System.out).sendMethod("println", "haha");
        System.out.println("xxxxx");

        JATOInstanceActor<Main> actor2 = factory.startInstanceActor(new Main());
        actor2.sendMethod("setCount", 100);
        actor2.sendMethod("test");
        actor2.sendMethod("test");
        actor2.sendMethod("test");
        actor2.sendMethod("test");
        actor2.sendMethod("test");

        JATOInstanceActor<ArrayList> actor3 = factory.startInstanceActor(new ArrayList());
        for (int i = 0; i < 10; i++) {
            actor3.sendMethod("add", i * 2);
        }
        actor3.sendCallback(new MethodMessage("toString"), new JATOFutureCallback() {
            @Override
            public void onDone(Object result) {
                System.out.println("Thread: " + Thread.currentThread().getName() + " " + result);
            }
        });

        actor3.sendMethod("add", "222");
        System.out.println("Thread: " + Thread.currentThread().getName() + " " + actor3.get(new MethodMessage("toString")));
        factory.shutdown();

//        System.out.println(future3.get());

    }
}
