package mod.acomit.nimblesteps.event.handler;

import mod.acomit.nimblesteps.NimbleStepsMod;
import mod.acomit.nimblesteps.ServerConfig;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-12-22 14:30
 * @Description: 调整安全摔落高度
 */
@EventBusSubscriber(modid = NimbleStepsMod.MODID)
public class SafeFallHandler {

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        if (event.getEntity() instanceof Player) {
            float distance = event.getDistance();
            float safeFallHeight = (float) ServerConfig.safeFallHeight;
            event.setDistance(distance - (safeFallHeight - 3.0f));
        }
    }
}

