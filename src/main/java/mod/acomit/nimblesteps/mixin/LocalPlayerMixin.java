package mod.arcomit.omnisprint.mixin;

import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {

    @Shadow public Input input;

    @Shadow public abstract boolean isUnderWater();

    @Inject(method = "hasEnoughImpulseToStartSprinting", at = @At("HEAD"), cancellable = true)
    private void injectIsUsingItem(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(this.isUnderWater() ? this.input.hasForwardImpulse() : Math.abs(this.input.forwardImpulse) >= 0.8 || Math.abs(this.input.leftImpulse) >= 0.8);
    }
}

