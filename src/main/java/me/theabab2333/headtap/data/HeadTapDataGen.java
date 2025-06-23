package me.theabab2333.headtap.data;

import com.tterrag.registrate.providers.ProviderType;

import me.theabab2333.headtap.HeadTap;
import me.theabab2333.headtap.data.lang.LangHandler;
import me.theabab2333.headtap.data.recipe.RecipeHandler;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

import static me.theabab2333.headtap.HeadTap.REGISTRATE;

@EventBusSubscriber(modid = HeadTap.MODID, bus = EventBusSubscriber.Bus.MOD)
public class HeadTapDataGen {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        PackOutput packOutput = generator.getPackOutput();

        //这里暂时还用不到，但是函数本身需要放在这里（不然之后改起来麻烦）
    }

    public static void init() {
        REGISTRATE.addDataGenerator(ProviderType.RECIPE, RecipeHandler::init);
        REGISTRATE.addDataGenerator(ProviderType.LANG, LangHandler::init);
    }
}
