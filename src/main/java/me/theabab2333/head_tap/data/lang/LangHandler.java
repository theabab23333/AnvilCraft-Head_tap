package me.theabab2333.head_tap.data.lang;

import com.tterrag.registrate.providers.RegistrateLangProvider;

public class LangHandler {
    public static void init(RegistrateLangProvider provider) {
        JeiLang.init(provider);
        ItemTooltipLang.init(provider);
    }
}
