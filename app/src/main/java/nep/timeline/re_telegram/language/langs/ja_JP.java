package nep.timeline.re_telegram.language.langs;

import java.util.HashMap;
import java.util.Map;

import nep.timeline.re_telegram.language.LanguageInterface;

public class ja_JP implements LanguageInterface {
    private final Map<String, String> mappings = new HashMap<>();

    @Override
    public void init()
    {
        mappings.put("antirecall.message.deleted", "\u524a\u9664\u3055\u308c\u307e\u3057\u305f");
    }

    @Override
    public String resolve(String stringCode) {
        return mappings.get(stringCode);
    }
}
