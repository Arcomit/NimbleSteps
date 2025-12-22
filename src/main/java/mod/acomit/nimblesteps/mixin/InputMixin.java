package mod.acomit.nimblesteps.mixin;

import mod.acomit.nimblesteps.ServerConfig;
import net.minecraft.client.player.Input;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-12-20 16:24
 * @Description: 用于实现全方向冲刺
 */
@Mixin(Input.class)
public abstract class InputMixin {
    @Shadow public float leftImpulse;
    @Shadow public float forwardImpulse;

    @Inject(method = "hasForwardImpulse", at = @At("HEAD"), cancellable = true)
    private void injectHasForwardImpulse(CallbackInfoReturnable<Boolean> cir) {
        if (ServerConfig.enableOmniSprint) {
            cir.setReturnValue(Math.abs(this.forwardImpulse) > 1e-5 || Math.abs(this.leftImpulse) > 1e-5);
        }
    }
}
