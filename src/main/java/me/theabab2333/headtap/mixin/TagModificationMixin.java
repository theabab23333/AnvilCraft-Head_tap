package me.theabab2333.headtap.mixin;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.dubhe.anvilcraft.recipe.transform.TagModification;
import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(TagModification.class)
public class TagModificationMixin {

    @Shadow
    @Final
    private String path;

    @Shadow
    @Final
    private TagModification.ModifyOperation op;

    @Shadow
    @Final
    private Tag tag;

    @Inject(method = "accept(Lnet/minecraft/nbt/Tag;)V", at = @At("HEAD"), cancellable = true)
    public void rootSupportTagAccept(Tag input, CallbackInfo ci) {
        if (this.path.isEmpty() && this.op == TagModification.ModifyOperation.SET) {
            try {
                StringReader reader = new StringReader(path);
                NbtPathArgument argument = new NbtPathArgument();
                NbtPathArgument.NbtPath nbtPath = argument.parse(reader);
                List<Tag> contract = nbtPath.get(input);
                if (contract.size() >= 2)
                    throw new IllegalArgumentException("TagModification does not allow multiple tag at path: " + path);
                if (contract.isEmpty()) return;
                Tag value = contract.getFirst();
                if (value instanceof CompoundTag compoundTag && tag instanceof CompoundTag subTag) {
                    compoundTag.merge(subTag);
                    ci.cancel();
                }
                //如果不符合“value（原来的nbt）和tag（输入的nbt）都是compound tag”的条件，那么执行正常的代码
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
