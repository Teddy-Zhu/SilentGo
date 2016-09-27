package com.silentgo.utils.asm;
/***
 * ASM: a very small and fast Java bytecode manipulation framework
 * Copyright (c) 2000-2011 INRIA, France Telecom
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holders nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

public abstract class MethodVisitor {

    /**
     * The ASM API version implemented by this visitor. The value of this field
     * must be one of {@link Opcodes#ASM4}.
     */
    protected final int api;

    /**
     * The method visitor to which this visitor must delegate method calls. May
     * be null.
     */
    protected MethodVisitor mv;


    public MethodVisitor(final int api) {
        this(api, null);
    }


    public MethodVisitor(final int api, final MethodVisitor mv) {
        if (api != Opcodes.ASM4) {
            throw new IllegalArgumentException();
        }
        this.api = api;
        this.mv = mv;
    }

    public AnnotationVisitor visitAnnotationDefault() {
        if (mv != null) {
            return mv.visitAnnotationDefault();
        }
        return null;
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        if (mv != null) {
            return mv.visitAnnotation(desc, visible);
        }
        return null;
    }


    public AnnotationVisitor visitParameterAnnotation(int parameter,
                                                      String desc, boolean visible) {
        if (mv != null) {
            return mv.visitParameterAnnotation(parameter, desc, visible);
        }
        return null;
    }


    public void visitAttribute(Attribute attr) {
        if (mv != null) {
            mv.visitAttribute(attr);
        }
    }


    public void visitCode() {
        if (mv != null) {
            mv.visitCode();
        }
    }

    public void visitFrame(int type, int nLocal, Object[] local, int nStack,
                           Object[] stack) {
        if (mv != null) {
            mv.visitFrame(type, nLocal, local, nStack, stack);
        }
    }

    public void visitInsn(int opcode) {
        if (mv != null) {
            mv.visitInsn(opcode);
        }
    }


    public void visitIntInsn(int opcode, int operand) {
        if (mv != null) {
            mv.visitIntInsn(opcode, operand);
        }
    }

    /**
     * Visits a local variable instruction. A local variable instruction is an
     * instruction that loads or stores the value of a local variable.
     *
     * @param opcode
     *            the opcode of the local variable instruction to be visited.
     *            This opcode is either ILOAD, LLOAD, FLOAD, DLOAD, ALOAD,
     *            ISTORE, LSTORE, FSTORE, DSTORE, ASTORE or RET.
     * @param var
     *            the operand of the instruction to be visited. This operand is
     *            the index of a local variable.
     */
    public void visitVarInsn(int opcode, int var) {
        if (mv != null) {
            mv.visitVarInsn(opcode, var);
        }
    }

    /**
     * Visits a type instruction. A type instruction is an instruction that
     * takes the internal name of a class as parameter.
     *
     * @param opcode
     *            the opcode of the type instruction to be visited. This opcode
     *            is either NEW, ANEWARRAY, CHECKCAST or INSTANCEOF.
     * @param type
     *            the operand of the instruction to be visited. This operand
     *            must be the internal name of an object or array class (see
     *            {@link Type#getInternalName() getInternalName}).
     */
    public void visitTypeInsn(int opcode, String type) {
        if (mv != null) {
            mv.visitTypeInsn(opcode, type);
        }
    }

    /**
     * Visits a field instruction. A field instruction is an instruction that
     * loads or stores the value of a field of an object.
     *
     * @param opcode
     *            the opcode of the type instruction to be visited. This opcode
     *            is either GETSTATIC, PUTSTATIC, GETFIELD or PUTFIELD.
     * @param owner
     *            the internal name of the field's owner class (see
     *            {@link Type#getInternalName() getInternalName}).
     * @param name
     *            the field's name.
     * @param desc
     *            the field's descriptor (see {@link Type Type}).
     */
    public void visitFieldInsn(int opcode, String owner, String name,
                               String desc) {
        if (mv != null) {
            mv.visitFieldInsn(opcode, owner, name, desc);
        }
    }

    /**
     * Visits a method instruction. A method instruction is an instruction that
     * invokes a method.
     *
     * @param opcode
     *            the opcode of the type instruction to be visited. This opcode
     *            is either INVOKEVIRTUAL, INVOKESPECIAL, INVOKESTATIC or
     *            INVOKEINTERFACE.
     * @param owner
     *            the internal name of the method's owner class (see
     *            {@link Type#getInternalName() getInternalName}).
     * @param name
     *            the method's name.
     * @param desc
     *            the method's descriptor (see {@link Type Type}).
     */
    public void visitMethodInsn(int opcode, String owner, String name,
                                String desc) {
        if (mv != null) {
            mv.visitMethodInsn(opcode, owner, name, desc);
        }
    }

    /**
     * Visits an invokedynamic instruction.
     *
     * @param name
     *            the method's name.
     * @param desc
     *            the method's descriptor (see {@link Type Type}).
     * @param bsm
     *            the bootstrap method.
     * @param bsmArgs
     *            the bootstrap method constant arguments. Each argument must be
     *            an {@link Integer}, {@link Float}, {@link Long},
     *            {@link Double}, {@link String}, {@link Type} or {@link Handle}
     *            value. This method is allowed to modify the content of the
     *            array so a caller should expect that this array may change.
     */
    public void visitInvokeDynamicInsn(String name, String desc, Handle bsm,
                                       Object... bsmArgs) {
        if (mv != null) {
            mv.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
        }
    }

    /**
     * Visits a jump instruction. A jump instruction is an instruction that may
     * jump to another instruction.
     *
     * @param opcode
     *            the opcode of the type instruction to be visited. This opcode
     *            is either IFEQ, IFNE, IFLT, IFGE, IFGT, IFLE, IF_ICMPEQ,
     *            IF_ICMPNE, IF_ICMPLT, IF_ICMPGE, IF_ICMPGT, IF_ICMPLE,
     *            IF_ACMPEQ, IF_ACMPNE, GOTO, JSR, IFNULL or IFNONNULL.
     * @param label
     *            the operand of the instruction to be visited. This operand is
     *            a label that designates the instruction to which the jump
     *            instruction may jump.
     */
    public void visitJumpInsn(int opcode, Label label) {
        if (mv != null) {
            mv.visitJumpInsn(opcode, label);
        }
    }

    /**
     * Visits a label. A label designates the instruction that will be visited
     * just after it.
     *
     * @param label
     *            a {@link Label Label} object.
     */
    public void visitLabel(Label label) {
        if (mv != null) {
            mv.visitLabel(label);
        }
    }

    // -------------------------------------------------------------------------
    // Special instructions
    // -------------------------------------------------------------------------

    /**
     * Visits a LDC instruction. Note that new constant types may be added in
     * future versions of the Java Virtual Machine. To easily detect new
     * constant types, implementations of this method should check for
     * unexpected constant types, like this:
     *
     * <pre class="code">
     * if (cst instanceof Integer) {
     *     // ...
     * } else if (cst instanceof Float) {
     *     // ...
     * } else if (cst instanceof Long) {
     *     // ...
     * } else if (cst instanceof Double) {
     *     // ...
     * } else if (cst instanceof String) {
     *     // ...
     * } else if (cst instanceof Type) {
     *     int sort = ((Type) cst).getSort();
     *     if (sort == Type.OBJECT) {
     *         // ...
     *     } else if (sort == Type.ARRAY) {
     *         // ...
     *     } else if (sort == Type.METHOD) {
     *         // ...
     *     } else {
     *         // throw an exception
     *     }
     * } else if (cst instanceof Handle) {
     *     // ...
     * } else {
     *     // throw an exception
     * }
     * </pre>
     *
     * @param cst
     *            the constant to be loaded on the stack. This parameter must be
     *            a non null {@link Integer}, a {@link Float}, a {@link Long}, a
     *            {@link Double}, a {@link String}, a {@link Type} of OBJECT or
     *            ARRAY sort for <tt>.class</tt> constants, for classes whose
     *            version is 49.0, a {@link Type} of METHOD sort or a
     *            {@link Handle} for MethodType and MethodHandle constants, for
     *            classes whose version is 51.0.
     */
    public void visitLdcInsn(Object cst) {
        if (mv != null) {
            mv.visitLdcInsn(cst);
        }
    }

    /**
     * Visits an IINC instruction.
     *
     * @param var
     *            index of the local variable to be incremented.
     * @param increment
     *            amount to increment the local variable by.
     */
    public void visitIincInsn(int var, int increment) {
        if (mv != null) {
            mv.visitIincInsn(var, increment);
        }
    }

    /**
     * Visits a TABLESWITCH instruction.
     *
     * @param min
     *            the minimum key value.
     * @param max
     *            the maximum key value.
     * @param dflt
     *            beginning of the default handler block.
     * @param labels
     *            beginnings of the handler blocks. <tt>labels[i]</tt> is the
     *            beginning of the handler block for the <tt>min + i</tt> key.
     */
    public void visitTableSwitchInsn(int min, int max, Label dflt,
                                     Label... labels) {
        if (mv != null) {
            mv.visitTableSwitchInsn(min, max, dflt, labels);
        }
    }

    /**
     * Visits a LOOKUPSWITCH instruction.
     *
     * @param dflt
     *            beginning of the default handler block.
     * @param keys
     *            the values of the keys.
     * @param labels
     *            beginnings of the handler blocks. <tt>labels[i]</tt> is the
     *            beginning of the handler block for the <tt>keys[i]</tt> key.
     */
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        if (mv != null) {
            mv.visitLookupSwitchInsn(dflt, keys, labels);
        }
    }

    /**
     * Visits a MULTIANEWARRAY instruction.
     *
     * @param desc
     *            an array type descriptor (see {@link Type Type}).
     * @param dims
     *            number of dimensions of the array to allocate.
     */
    public void visitMultiANewArrayInsn(String desc, int dims) {
        if (mv != null) {
            mv.visitMultiANewArrayInsn(desc, dims);
        }
    }

    // -------------------------------------------------------------------------
    // Exceptions table entries, debug information, max stack and max locals
    // -------------------------------------------------------------------------

    /**
     * Visits a try catch block.
     *
     * @param start
     *            beginning of the exception handler's scope (inclusive).
     * @param end
     *            end of the exception handler's scope (exclusive).
     * @param handler
     *            beginning of the exception handler's code.
     * @param type
     *            internal name of the type of exceptions handled by the
     *            handler, or <tt>null</tt> to catch any exceptions (for
     *            "finally" blocks).
     * @throws IllegalArgumentException
     *             if one of the labels has already been visited by this visitor
     *             (by the {@link #visitLabel visitLabel} method).
     */
    public void visitTryCatchBlock(Label start, Label end, Label handler,
                                   String type) {
        if (mv != null) {
            mv.visitTryCatchBlock(start, end, handler, type);
        }
    }

    /**
     * Visits a local variable declaration.
     *
     * @param name
     *            the name of a local variable.
     * @param desc
     *            the type descriptor of this local variable.
     * @param signature
     *            the type signature of this local variable. May be
     *            <tt>null</tt> if the local variable type does not use generic
     *            types.
     * @param start
     *            the first instruction corresponding to the scope of this local
     *            variable (inclusive).
     * @param end
     *            the last instruction corresponding to the scope of this local
     *            variable (exclusive).
     * @param index
     *            the local variable's index.
     * @throws IllegalArgumentException
     *             if one of the labels has not already been visited by this
     *             visitor (by the {@link #visitLabel visitLabel} method).
     */
    public void visitLocalVariable(String name, String desc, String signature,
                                   Label start, Label end, int index) {
        if (mv != null) {
            mv.visitLocalVariable(name, desc, signature, start, end, index);
        }
    }

    /**
     * Visits a line number declaration.
     *
     * @param line
     *            a line number. This number refers to the source file from
     *            which the class was compiled.
     * @param start
     *            the first instruction corresponding to this line number.
     * @throws IllegalArgumentException
     *             if <tt>start</tt> has not already been visited by this
     *             visitor (by the {@link #visitLabel visitLabel} method).
     */
    public void visitLineNumber(int line, Label start) {
        if (mv != null) {
            mv.visitLineNumber(line, start);
        }
    }

    /**
     * Visits the maximum stack size and the maximum number of local variables
     * of the method.
     *
     * @param maxStack
     *            maximum stack size of the method.
     * @param maxLocals
     *            maximum number of local variables for the method.
     */
    public void visitMaxs(int maxStack, int maxLocals) {
        if (mv != null) {
            mv.visitMaxs(maxStack, maxLocals);
        }
    }

    /**
     * Visits the end of the method. This method, which is the last one to be
     * called, is used to inform the visitor that all the annotations and
     * attributes of the method have been visited.
     */
    public void visitEnd() {
        if (mv != null) {
            mv.visitEnd();
        }
    }
}
