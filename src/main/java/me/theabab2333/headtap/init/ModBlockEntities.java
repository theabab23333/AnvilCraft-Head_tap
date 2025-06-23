package me.theabab2333.headtap.init;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import me.theabab2333.headtap.block.entity.StoneGeneratorBlockEntity;

import static me.theabab2333.headtap.HeadTap.REGISTRATE;

public class ModBlockEntities {

    public static final BlockEntityEntry<StoneGeneratorBlockEntity> STONE_GENERATOR = REGISTRATE
            .blockEntity("stone_generator", StoneGeneratorBlockEntity::new)
            .validBlock(ModBlocks.STONE_GENERATOR)
            .register();

    public static void register() {}
}
