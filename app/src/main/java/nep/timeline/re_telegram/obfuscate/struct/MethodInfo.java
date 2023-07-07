package nep.timeline.re_telegram.obfuscate.struct;

public class MethodInfo {
    private final String className;
    private final String original;
    private final String resolved;

    public MethodInfo(String className, String original, String resolved)
    {
        this.className = className;
        this.original = original;
        this.resolved = resolved;
    }

    public String getClassName() {
        return this.className;
    }

    public String getOriginal() {
        return this.original;
    }

    public String getResolved() {
        return this.resolved;
    }
}
