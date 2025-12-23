package mod.acomit.nimblesteps.event.handler;

import com.mojang.blaze3d.platform.InputConstants;
import mod.acomit.nimblesteps.ServerConfig;
import mod.acomit.nimblesteps.NimbleStepsMod;
import mod.acomit.nimblesteps.attachment.NimbleStepsState;
import mod.acomit.nimblesteps.init.NsAttachmentTypes;
import mod.acomit.nimblesteps.network.UseSwimmingBoostPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-12-22 11:21
 * @Description: 水中推进
 */
@EventBusSubscriber(modid = NimbleStepsMod.MODID)
public class SwimmingBoostHandler {

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onKeyPress(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen != null) return;
        Player player = mc.player;
        Options options = mc.options;
        if (player == null) return;
        if (event.getAction() != InputConstants.PRESS || event.getKey() != options.keySprint.getKey().getValue()) return;
        if (!canSwimmingBoost(player)) return;
        applySwimmingBoost(player);
        PacketDistributor.sendToServer(new UseSwimmingBoostPacket());
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onMousePress(InputEvent.MouseButton.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen != null) return;
        Player player = mc.player;
        Options options = mc.options;
        if (player == null) return;
        if (event.getAction() != InputConstants.PRESS || event.getButton() != options.keySprint.getKey().getValue()) return;
        if (!canSwimmingBoost(player)) return;
        applySwimmingBoost(player);
        PacketDistributor.sendToServer(new UseSwimmingBoostPacket());
    }

    public static boolean canSwimmingBoost(Player player) {
        NimbleStepsState nimbleStepsState = player.getData(NsAttachmentTypes.CRAWL_ATTACHMENT);
        return ServerConfig.enableSwimmingBoost
                && nimbleStepsState.getSwimmingBoostCooldown() <= 0
                && !player.isSpectator()
                && !player.isPassenger()
                && player.isSwimming();
    }

    public static void applySwimmingBoost(Player player) {
        NimbleStepsState nimbleStepsState = player.getData(NsAttachmentTypes.CRAWL_ATTACHMENT);
        nimbleStepsState.setSwimmingBoostCooldown(ServerConfig.swimmingBoostCooldown);
        Vec3 deltaMovement = player.getDeltaMovement();
        Vec3 direction;
        if (deltaMovement.lengthSqr() < 1.0E-7) {
            direction = player.getLookAngle();
        } else {
            direction = deltaMovement.normalize();
        }
        double boost = 0.4;
        player.setDeltaMovement(deltaMovement.add(direction.scale(boost)));
    }

    @SubscribeEvent
    public static void updateCooldown(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();
        NimbleStepsState nimbleStepsState = player.getData(NsAttachmentTypes.CRAWL_ATTACHMENT);
        int swimmingBoostCooldown = nimbleStepsState.getSwimmingBoostCooldown();
        if (swimmingBoostCooldown <= 0) return;
        nimbleStepsState.setSwimmingBoostCooldown(swimmingBoostCooldown - 1);
    }
}
