package org.jato.core.message;

import org.jato.core.furture.JATOFuture;

/**
 * [类注释]
 *
 * @author gongjun
 * @since 2016-02-25 13:16
 */
public class FutureMessage implements Message {

    protected JATOFuture furture;

    public FutureMessage() {
    }

    public FutureMessage(JATOFuture furture) {
        this.furture = furture;
    }

    public void setFurture(JATOFuture furture) {
        this.furture = furture;
    }

    public JATOFuture getFuture() {
        return furture;
    }

}
