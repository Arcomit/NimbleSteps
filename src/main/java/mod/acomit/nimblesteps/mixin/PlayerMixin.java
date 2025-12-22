package mod.acomit.nimblesteps.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
*@Author: Arcomit
*@CreateTime: 2025-12-22 11:04
*@Description: 防止攻击时打断疾跑
*/
@Mixin(Player.class)
public abstract class PlayerMixin {

    @Redirect(
            method = "attack(Lnet/minecraft/world/entity/Entity;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;setSprinting(Z)V"
            )
    )
    private void preventSprintInvalid(Player instance, boolean isSprinting) {
        // 留空：不执行 setSprinting(false)，从而不打断疾跑
    }
}
