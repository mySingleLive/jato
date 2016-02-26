package org.jato.core.message;

/**
 * [类注释]
 *
 * @author gongjun
 * @since 2016-02-26 13:32
 */
public class ObjectMessage extends FutureMessage implements Message {

    private Object object;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
