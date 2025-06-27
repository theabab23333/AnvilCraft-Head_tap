package me.theabab2333.headtap.init;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import me.theabab2333.headtap.block.entity.ResinExtractorBlockEntity;
import me.theabab2333.headtap.block.entity.StoneGeneratorBlockEntity;

import static me.theabab2333.headtap.HeadTap.REGISTRATE;

public class ModBlockEntities {

    public static final BlockEntityEntry<StoneGeneratorBlockEntity> STONE_GENERATOR = REGISTRATE
            .blockEntity("stone_generator", StoneGeneratorBlockEntity::new)
            .validBlock(ModBlocks.STONE_GENERATOR)
            .register();

    public static final BlockEntityEntry<ResinExtractorBlockEntity> RESIN_EXTRACTOR = REGISTRATE
            .blockEntity("resin_extractor", ResinExtractorBlockEntity::new)
            .validBlock(ModBlocks.RESIN_EXTRACTOR)
            .register();

    public static void register() {}
}
