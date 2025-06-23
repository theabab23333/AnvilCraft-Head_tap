package me.theabab2333.headtap.util;

import me.theabab2333.headtap.init.ModBlocks;
import me.theabab2333.headtap.init.ModItems;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Items;

public class ModInteractionMap {

    public static final CauldronInteraction.InteractionMap RESIN_FLUID = CauldronInteraction.newInteractionMap("resin_fluid");

    public static void initInteractionMap() {
        var meltGemInteractionMap = RESIN_FLUID.map();
        meltGemInteractionMap.put(
            Items.BUCKET,
            (state, level, pos, player, hand, stack) -> CauldronInteraction.fillBucket(
                state,
                level,
                pos,
                player,
                hand,
                stack,
                ModItems.RESIN_FLUID_BUCKET.asStack(),
                s -> true,
                SoundEvents.BUCKET_FILL
            )
        );
        var emptyInteractionMap = CauldronInteraction.EMPTY.map();
        emptyInteractionMap.put(
            ModItems.RESIN_FLUID_BUCKET.get(),
            (state, level, pos, player, hand, stack) -> CauldronInteraction.emptyBucket(
                level,
                pos,
                player,
                hand,
                stack,
                ModBlocks.RESIN_FLUID_CAULDRON.getDefaultState(),
                SoundEvents.BUCKET_EMPTY
            )
        );
    }
}
