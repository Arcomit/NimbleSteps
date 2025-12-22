package mod.arcomit.omnisprint.mixin;

import net.minecraft.client.player.Input;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Input.class)
public class InputMixin {

    @Shadow public float leftImpulse;
    @Shadow public float forwardImpulse;

    @Inject(method = "hasForwardImpulse", at = @At("HEAD"), cancellable = true)
    private void injectIsUsingItem(CallbackInfoReturnable<Boolean> cir) {
            cir.setReturnValue(Math.abs(this.forwardImpulse) > 1e-5 || Math.abs(this.leftImpulse) > 1e-5);
    }
}
