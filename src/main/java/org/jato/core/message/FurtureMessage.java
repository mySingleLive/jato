package org.jato.core.message;

import org.jato.core.furture.JATOFuture;

/**
 * [类注释]
 *
 * @author gongjun
 * @since 2016-02-25 13:16
 */
public class FurtureMessage implements Message {

    protected JATOFuture furture;

    public FurtureMessage() {
    }

    public FurtureMessage(JATOFuture furture) {
        this.furture = furture;
    }

    public void setFurture(JATOFuture furture) {
        this.furture = furture;
    }

    public JATOFuture getFuture() {
        return furture;
    }

}
