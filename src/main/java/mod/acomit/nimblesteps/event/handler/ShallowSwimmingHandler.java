package mod.acomit.nimblesteps.event.handler;

import mod.acomit.nimblesteps.ServerConfig;
import mod.acomit.nimblesteps.NimbleStepsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-12-21 20:04
 * @Description: 浅水游泳
 */
@EventBusSubscriber(modid = NimbleStepsMod.MODID)
public class ShallowSwimmingHandler {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (!canShallowSwim(player)) return;

        if (player instanceof LocalPlayer localPlayer) {
            if ((Minecraft.getInstance().options.keySprint.isDown() || player.isSprinting()) && localPlayer.hasEnoughFoodToStartSprinting()) {
                localPlayer.setSprinting(true);
                localPlayer.setSwimming(true);
            }
            if (!localPlayer.input.hasForwardImpulse()) {
                localPlayer.setSprinting(false);
                localPlayer.setSwimming(false);
            }
        }else {
            // 服务端逻辑，客户端的疾跑会自动同步到服务端。
            if (player.isSprinting()) {
                player.setSwimming(true);
            }else {
                player.setSwimming(false);
            }
        }
    }

    private static boolean canShallowSwim(Player player) {
        return ServerConfig.enableShallowSwimming
                && player.isInWater()
                && !player.isSpectator()
                && !player.isPassenger();
    }

}
