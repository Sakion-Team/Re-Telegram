package nep.timeline.re_telegram.language.langs;

import java.util.HashMap;
import java.util.Map;

import nep.timeline.re_telegram.language.LanguageInterface;

public class zh_CN implements LanguageInterface {
    private final Map<String, String> mappings = new HashMap<>();

    public void init()
    {
        mappings.put("antirecall.message.deleted", "\u5df2\u5220\u9664");
    }

    @Override
    public String resolve(String stringCode) {
        return mappings.get(stringCode);
    }
}
