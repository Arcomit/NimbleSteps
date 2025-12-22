package mod.acomit.nimblesteps.event.handler;

import mod.acomit.nimblesteps.NimbleStepsMod;
import mod.acomit.nimblesteps.attachment.NimbleStepsState;
import mod.acomit.nimblesteps.init.NsAttachmentTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-12-21 16:15
 * @Description: TODO：数据同步？也许不需要。
 */
//@EventBusSubscriber(modid = NimbleStepsMod.MODID)
public class PlayerEventHandler {

    @SubscribeEvent
    public static void onPlayerStartTracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof Player targetPlayer && event.getEntity() instanceof ServerPlayer trackingPlayer) {
            NimbleStepsState attachment = targetPlayer.getData(NsAttachmentTypes.CRAWL_ATTACHMENT);
            //PacketDistributor.sendToPlayer(trackingPlayer, new PlayerStateSyncPacket(targetPlayer.getId(), attachment.isCrawling()));
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            NimbleStepsState attachment = player.getData(NsAttachmentTypes.CRAWL_ATTACHMENT);
            //PacketDistributor.sendToPlayer(player, new PlayerStateSyncPacket(player.getId(), attachment.isCrawling()));
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
         if (event.getEntity() instanceof ServerPlayer player) {
            NimbleStepsState attachment = player.getData(NsAttachmentTypes.CRAWL_ATTACHMENT);
            // Reset crawling state on respawn if desired, or sync if it persists (usually reset)
            attachment.setCrawling(false);
            //PacketDistributor.sendToPlayer(player, new PlayerStateSyncPacket(player.getId(), false));
        }
    }
}

