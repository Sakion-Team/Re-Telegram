package nep.timeline.re_telegram.obfuscate.struct;

public class ClassInfo {
    private final String original;
    private final String resolved;

    public ClassInfo(String original, String resolved)
    {
        this.original = original;
        this.resolved = resolved;
    }

    public String getOriginal() {
        return this.original;
    }

    public String getResolved() {
        return this.resolved;
    }
}
