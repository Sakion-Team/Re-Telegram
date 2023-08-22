package nep.timeline.re_telegram.language;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import nep.timeline.re_telegram.language.langs.English;
import nep.timeline.re_telegram.language.langs.ja_JP;
import nep.timeline.re_telegram.language.langs.zh_CN;
import nep.timeline.re_telegram.language.langs.zh_TW;

public class Language {
    private static boolean init = false;

    private static final List<LanguageInterface> interfaces = Arrays.asList(new English(),
            new ja_JP(),
            new zh_CN(),
            new zh_TW()
    );

    public static void init()
    {
        if (init)
            return;

        interfaces.forEach(LanguageInterface::init);
        init = true;
    }

    public static String resolve(Locale locale, String stringCode)
    {
        String code = locale.getLanguage() + "_" + locale.getCountry();
        return switch (code) {
            case "ja_JP" -> LanguageList.Japanese.getLanguageInterface().resolve(stringCode);
            case "zh_CN" -> LanguageList.SChinese.getLanguageInterface().resolve(stringCode);
            case "zh_TW" -> LanguageList.TChinese.getLanguageInterface().resolve(stringCode);
            default -> LanguageList.English.getLanguageInterface().resolve(stringCode);
        };
    }

    public enum LanguageList
    {
        English(interfaces.get(0)),
        Japanese(interfaces.get(1)),
        SChinese(interfaces.get(2)),
        TChinese(interfaces.get(3));

        private final LanguageInterface languageInterface;

        LanguageList(LanguageInterface languageInterface)
        {
            this.languageInterface = languageInterface;
        }

        public LanguageInterface getLanguageInterface() {
            if (!init)
                init();
            return this.languageInterface;
        }
    }
}
