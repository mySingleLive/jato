package org.jato.core.actor;

import com.esotericsoftware.reflectasm.MethodAccess;
import org.jato.core.Main;
import org.jato.core.furture.JATOFuture;
import org.jato.core.message.MethodMessage;
import org.jetlang.channels.Channel;
import org.jetlang.fibers.Fiber;
import sun.plugin.javascript.navig.Array;

import java.awt.peer.ListPeer;
import java.util.*;

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

    private Map<String, List<Object[]>> methodNameMap;

    public JATOInstanceActor(JATOActorFactory actorFactory, T instance, Channel<MethodMessage> mailbox, Fiber fiber) {
        super(actorFactory, mailbox, fiber);
        this.instance = instance;
        this.clazz = (Class<T>) instance.getClass();
        methodAccess = MethodAccess.get(clazz);
        methodNameMap = buildMethodNameMap(methodAccess);
    }

    public Class<T> getClazz() {
        return clazz;
    }


    public T getInstance() {
        return instance;
    }


    private Map<String, List<Object[]>> buildMethodNameMap(MethodAccess methodAccess) {
        Map<String, List<Object[]>>  map = new HashMap<String, List<Object[]>>();

        String[] methodNames = methodAccess.getMethodNames();
        Class[][] parameterTypes = methodAccess.getParameterTypes();
        for (int i = 0; i < methodNames.length; i++) {
            String name = methodNames[i];
            Class[] pTypes = parameterTypes[i];
            List<Object[]> paramIndexList = map.get(name);
            if (paramIndexList == null) {
                paramIndexList = new ArrayList<Object[]>();
                map.put(name, paramIndexList);
            }
            Object[] paramIndex = new Object[2];
            List<Class[]> paramTypes = (List<Class[]>) paramIndex[0];
            if (parameterTypes == null) {
                paramTypes = new ArrayList<Class[]>();
                paramIndex[0] = paramTypes;
            }

            paramIndexList.add(paramIndex);


        }
        return map;
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

/*
        Object[] paramIndex = methodNameMap.get(methodName);
        if (paramIndex != null) {
            List<Class[]> parameterTypes = (List<Class[]>) paramIndex[0];
            for (int i = 0, n = parameterTypes.size(); i < n; i++)
                if (matchTypes(paramTypes, parameterTypes.get(i))) return i;
        }
        throw new IllegalArgumentException("Unable to find public method: " + methodName + " " + String.valueOf(parameterTypes));
*/
    }



    @Override
    protected void onReceive(MethodMessage message) {
        String methodName = message.getName();
        Object result = null;
        Object[] args = message.getArgs();
        Class[] clzzes = message.getArgTypes();
        int index = getIndex(methodName, clzzes);
        try {
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
        } catch (Throwable t) {
            System.out.println("index: " + index + ", args: " + Arrays.toString(args));
            t.printStackTrace();
        }
    }

}
