package mod.acomit.nimblesteps.event.handler;

import mod.acomit.nimblesteps.ServerConfig;
import mod.acomit.nimblesteps.NimbleStepsMod;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-12-22 11:13
 * @Description: 自由泳
 */
@EventBusSubscriber(modid = NimbleStepsMod.MODID)
public class FreestyleHandler {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (!canFreestyle(player)) return;
        Vec3 deltaMovement = player.getDeltaMovement();
        player.setDeltaMovement(deltaMovement.x, 0, deltaMovement.z);
    }

    private static boolean canFreestyle(Player player) {
        return ServerConfig.enableFreestyle
                && player.isSwimming()
                && !player.isUnderWater()
                && !player.isSpectator()
                && !player.isPassenger();
    }
}
