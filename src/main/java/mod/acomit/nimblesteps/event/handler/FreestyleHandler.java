package mod.acomit.nimblesteps.event.handler;

import mod.acomit.nimblesteps.ServerConfig;
import mod.acomit.nimblesteps.NimbleStepsMod;
import mod.acomit.nimblesteps.network.UseFreestyleJumpPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-12-22 11:13
 * @Description: 自由泳(且能正常水中跳跃)
 */
@EventBusSubscriber(modid = NimbleStepsMod.MODID)
public class FreestyleHandler {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (!canFreestyle(player)) return;

        Vec3 motion = player.getDeltaMovement();
        if (motion.y < 0) return;
        player.setDeltaMovement(motion.x, 0, motion.z);
        if (player.level().isClientSide && player.jumping) {
            applyFreestyleJump(player);
            PacketDistributor.sendToServer(new UseFreestyleJumpPacket());
        }
    }

    public static boolean canFreestyle(Player player) {
        return ServerConfig.enableFreestyle
                && player.isSwimming()
                && !player.isUnderWater()
                && !player.isSpectator()
                && !player.isPassenger();
    }

    public static void applyFreestyleJump(Player player) {
        Vec3 motion = player.getDeltaMovement();
        if (motion.y < 0.2) {
            player.setDeltaMovement(motion.x, 0.42, motion.z);
        }
    }
}
