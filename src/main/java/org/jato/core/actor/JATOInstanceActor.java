package org.jato.core.actor;

import com.esotericsoftware.reflectasm.MethodAccess;
import org.jato.core.furture.JATOFuture;
import org.jato.core.message.MethodMessage;
import org.jetlang.channels.Channel;
import org.jetlang.fibers.Fiber;

import java.util.Arrays;

/**
 * [类注释]
 *
 * @author gongjun
 * @since 2016-02-25 11:27
 */
public class JATOInstanceActor<T> extends JATOAbstractActor<MethodMessage> implements JATOActor<MethodMessage> {

    private Class<T> clazz;

    private T instance;

    private MethodAccess methodAccess;

    public JATOInstanceActor(JATOActorFactory actorFactory, T instance, Channel<MethodMessage> mailbox, Fiber fiber) {
        super(actorFactory, mailbox, fiber);
        this.instance = instance;
        this.clazz = (Class<T>) instance.getClass();
        methodAccess = MethodAccess.get(clazz);
    }

    public Class<T> getClazz() {
        return clazz;
    }


    public T getInstance() {
        return instance;
    }




    /**
     * 类型匹配
     * @param argType
     * @param paramType
     * @return
     */
    public static boolean isMatchType(Class argType, Class paramType) {
        if (argType.equals(paramType)) {
            return true;
        }
        if (paramType.isArray() != argType.isArray()) {
            return false;
        }
        if (paramType.isAssignableFrom(argType)) {
            return true;
        }
        if (byte.class.isAssignableFrom(paramType) && Byte.class.isAssignableFrom(argType)) {
            return true;
        }
        if (char.class.isAssignableFrom(paramType) && Character.class.isAssignableFrom(argType)) {
            return true;
        }
        if (short.class.isAssignableFrom(paramType) && Short.class.isAssignableFrom(argType)) {
            return true;
        }
        if (int.class.isAssignableFrom(paramType) && Integer.class.isAssignableFrom(argType)) {
            return true;
        }
        if (long.class.isAssignableFrom(paramType) && Long.class.isAssignableFrom(argType)) {
            return true;
        }
        if (float.class.isAssignableFrom(paramType) && Float.class.isAssignableFrom(argType)) {
            return true;
        }
        if (double.class.isAssignableFrom(paramType) && Double.class.isAssignableFrom(argType)) {
            return true;
        }
        return false;
    }

    private boolean matchTypes(Class[] argTypes, Class[] paramTypes) {
        boolean result = true;
        if (argTypes.length < paramTypes.length) {
            return false;
        }
        for (int i = 0; i < paramTypes.length; i++) {
            Class paramType = paramTypes[i];
            Class argType = argTypes[i];

            if (!isMatchType(argType, paramType)) {
                return false;
            }
        }
        return result;
    }

    private int getIndex (String methodName, Class... paramTypes) {
        String[] methodNames = methodAccess.getMethodNames();
        Class[][] parameterTypes = methodAccess.getParameterTypes();
        for (int i = 0, n = methodNames.length; i < n; i++)
            if (methodNames[i].equals(methodName) && matchTypes(paramTypes, parameterTypes[i])) return i;
        throw new IllegalArgumentException("Unable to find public method: " + methodName + " " + Arrays.toString(parameterTypes));
    }



    @Override
    protected void onReceive(MethodMessage message) {
        String methodName = message.getName();
        Object result = null;
        Object[] args = message.getArgs();
        Class[] clzzes = message.getArgTypes();
        int index = getIndex(methodName, clzzes);
        result = methodAccess.invoke(instance, index, args);
        JATOFuture future = message.getFuture();
        if (future == null) {
            return;
        }
        try {
            future.set(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
