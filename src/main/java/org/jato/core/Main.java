package org.jato.core;

import org.jato.core.actor.JATOActor;
import org.jato.core.actor.JATOActorFactory;
import org.jato.core.actor.JATOCallbackActor;
import org.jato.core.actor.JATOInstanceActor;
import org.jato.core.furture.JATOFutureCallback;
import org.jato.core.message.MethodMessage;
import org.jetlang.core.Callback;

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

    private JATOActor actor;

    public void test() {
        System.out.println("current thread: " + Thread.currentThread().getName() + " do test: " + count);
        count++;
    }

    public void test(int x) {
        System.out.println("current thread: " + Thread.currentThread().getName() + " do test: " + count);
        count += x;
    }

    public void other() {
        System.out.println("current thread: " + Thread.currentThread().getName() + " do other: " + count);
        JATO.self().send(new MethodMessage("test"));
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        final JATOActorFactory factory = JATO.createActorFactory();
        JATOInstanceActor<Main> actor = factory.startInstanceActor(new Main());
        actor.sendMethod("setCount", 0);
        actor.sendMethod("test");
        actor.sendMethod("test");
        actor.sendMethod("test");
        actor.sendMethod("test");
        actor.sendMethod("test");
        actor.sendMethod("other");
//        System.out.println(future.get());
        factory.startInstanceActor(System.out).sendMethod("println", "haha");
        System.out.println("xxxxx");

        final JATOCallbackActor callbackActor = factory.startCallbackActor(new Callback<Object>() {
            public void onMessage(Object message) {
                if (message.equals("ok")) {
                    System.out.println("do ok: " + Thread.currentThread().getName());
                }
                else if (message.equals("done")) {
                    System.out.println( "do done: " + Thread.currentThread().getName());
                }
            }
        });

        JATOCallbackActor callbackActor2 = factory.startCallbackActor(new Callback<Object>() {
            public void onMessage(Object message) {
                if (message.equals("start")) {
                    System.out.println("do start: " + Thread.currentThread().getName());
                    callbackActor.send("ok");
                    callbackActor.send("done");
                }
                else if (message.equals("other")) {
                    System.out.println("do other: " + Thread.currentThread().getName());
                }
            }
        });

        callbackActor2.send("start");


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
