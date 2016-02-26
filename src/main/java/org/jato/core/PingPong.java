package org.jato.core;

import org.jato.core.actor.JATOActor;
import org.jato.core.actor.JATOInstanceActor;

/**
 * [类注释]
 *
 * @author gongjun
 * @since 2016-02-26 14:10
 */
public class PingPong {

    private class Ping {

        private JATOActor pong;


    }

    public void ping(int n, JATOInstanceActor pong) {
        if (n == 0) {
            pong.sendMethod("finished");
        }
        else {

        }
    }

    public void pong(JATOInstanceActor ping) {
        System.out.println();
    }

    public void finished() {
        System.out.println("pong finished");
    }
}
