package nep.timeline.re_telegram.language.langs;

import java.util.HashMap;
import java.util.Map;

import nep.timeline.re_telegram.language.LanguageInterface;

public class English implements LanguageInterface {
    private final Map<String, String> mappings = new HashMap<>();

    @Override
    public void init()
    {
        mappings.put("antirecall.message.deleted", "deleted");
    }

    @Override
    public String resolve(String stringCode) {
        return mappings.get(stringCode);
    }
}
