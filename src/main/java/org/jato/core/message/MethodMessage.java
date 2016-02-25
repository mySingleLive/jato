package org.jato.core.message;


/**
 * [类注释]
 *
 * @author gongjun
 * @since 2016-02-25 11:15
 */
public class MethodMessage extends FurtureMessage implements Message {

    private String name;

    private Object[] args;

    public MethodMessage(String name) {
        this.name = name;
        this.args = new Object[0];
    }

    public MethodMessage(String name, Object... args) {
        this.name = name;
        this.args = args;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Class[] getArgTypes() {
        Class[] clzzes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            Object obj = args[i];
            if (obj == null) {
                clzzes[i] = Object.class;
                continue;
            }
            clzzes[i] = obj.getClass();
        }
        return clzzes;
    }

}
