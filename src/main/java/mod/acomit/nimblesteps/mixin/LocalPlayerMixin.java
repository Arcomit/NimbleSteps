package mod.acomit.nimblesteps.mixin;

import mod.acomit.nimblesteps.ServerConfig;
import mod.acomit.nimblesteps.attachment.NimbleStepsState;
import mod.acomit.nimblesteps.init.NsAttachmentTypes;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
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
@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends LivingEntity {

    protected LocalPlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow public Input input;

    @Shadow @Override
    public abstract boolean isUnderWater();

    @Inject(method = "hasEnoughImpulseToStartSprinting", at = @At("HEAD"), cancellable = true)
    private void injectHasEnoughImpulseToStartSprinting(CallbackInfoReturnable<Boolean> cir) {
        if (ServerConfig.enableOmniSprint) {
            cir.setReturnValue(this.isUnderWater() ? this.input.hasForwardImpulse() : Math.abs(this.input.forwardImpulse) >= 0.8 || Math.abs(this.input.leftImpulse) >= 0.8);
        }
    }

    @Inject(method = "isMovingSlowly", at = @At("HEAD"), cancellable = true)
    public void injectIsMovingSlowly(CallbackInfoReturnable<Boolean> cir) {
        NimbleStepsState nimbleStepsState = this.getData(NsAttachmentTypes.CRAWL_ATTACHMENT);
        if (nimbleStepsState.getSlideDuration() > 0) {
            cir.setReturnValue(false);
        }
    }
}

