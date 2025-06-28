package me.theabab2333.headtap.data.lang;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import org.jetbrains.annotations.NotNull;

public class OtherLang {
    public static void init(@NotNull RegistrateLangProvider provider) {
        provider.add("message.headtap.golem_craftbow.golem_fail", "Don't have enough materials to craft a golem.");
        provider.add("modmenu.nameTranslation.headtap", "AnvilCraft: HeadTap");
    }
}
