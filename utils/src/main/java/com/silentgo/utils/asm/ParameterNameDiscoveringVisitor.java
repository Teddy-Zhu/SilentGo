package com.silentgo.utils.asm;

import java.lang.reflect.Member;
import java.util.Map;

public class ParameterNameDiscoveringVisitor extends ClassVisitor {

    private static final String STATIC_CLASS_INIT = "<clinit>";

    private final Class<?> clazz;

    private final Map<Member, String[]> memberMap;

    public ParameterNameDiscoveringVisitor(Class<?> clazz, Map<Member, String[]> memberMap) {
        super(Opcodes.ASM5);
        this.clazz = clazz;
        this.memberMap = memberMap;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        // exclude synthetic + bridged && static class initialization
        if (!isSyntheticOrBridged(access) && !STATIC_CLASS_INIT.equals(name)) {
            return new LocalVariableTableVisitor(clazz, memberMap, name, desc, isStatic(access));
        }
        return null;
    }

    private static boolean isSyntheticOrBridged(int access) {
        return (((access & Opcodes.ACC_SYNTHETIC) | (access & Opcodes.ACC_BRIDGE)) > 0);
    }

    private static boolean isStatic(int access) {
        return ((access & Opcodes.ACC_STATIC) > 0);
    }
}
