package me.theabab2333.headtap.data.lang;

import com.tterrag.registrate.providers.RegistrateLangProvider;

public class ItemTooltipLang {
    public static void init(RegistrateLangProvider provider) {
        provider.add("item.head_tap.amethyst_hammer.tooltip", "Fragile crystal, deals more damage.");
        provider.add("item.head_tap.golem_craftbow.tooltip", "Right click to heal nearby Iron Golems with Iron Ingots. \n Shift + Right click to summon golems with material blocks.");
        provider.add("item.head_tap.golem_craftbow.golem_fail", "Don't have enough materials to craft a golem.");
    }
}
