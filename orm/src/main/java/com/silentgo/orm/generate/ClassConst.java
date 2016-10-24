package com.silentgo.orm.generate;

/**
 * Project : parent
 * Package : com.silentgo.orm.generate
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/27.
 */
public class ClassConst {
    private ClassConst() {
    }

    public static final String newline = "$n";
    public static final String tab = "$t";
    public static final String string = "$s";
    public static final String _package = "package $s;$n$n";
    public static final String _annotaion = "@$s$n";
    public static final String _classbody = "public class $s {$n$n$s$n}$n$n";
    public static final String _interfaceclassbody_extend = "public interface $s extends $s {$n$n$s$n}$n$n";
    public static final String _interfaceclassbody_extend_t = "public interface $s extends $s<$s> {$n$n$s$n}$n$n";
    public static final String _classbody_extend = "public class $s extends $s {$n$n$s$n}$n$n";
    public static final String _classbody_implements = "public class $s implements $s {$n$n$s$n}$n$n";
    public static final String _field_null = "$tpublic $s $s;$n$n";
    public static final String _field = "$tpublic $s $s = $s ;$n$n";

    public static final String _field_string = "$tpublic $s $s = \"$s\" ;";

    public static final String _importOne = "import $s;$n";

    public static final String _setter =
            "$tpublic void set$s($s $s) {$n" +
                    "$t$tthis.$s = $s;" +
                    "$n$t}%n%n";

    public static final String _getter =
            "$tpublic $s get$s() {$n" +
                    "$t$treturn $s;" +
                    "$n$t}%n%n";

}
