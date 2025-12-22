package mod.acomit.nimblesteps.event;

import mod.acomit.nimblesteps.NimbleStepsMod;
import mod.acomit.nimblesteps.attachment.PlayerStateAttachment;
import mod.acomit.nimblesteps.init.NsAttachmentTypes;
import mod.acomit.nimblesteps.network.PlayerStateSyncPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = NimbleStepsMod.MODID)
public class PlayerEventHandler {

    @SubscribeEvent
    public static void onPlayerStartTracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof Player targetPlayer && event.getEntity() instanceof ServerPlayer trackingPlayer) {
            PlayerStateAttachment attachment = targetPlayer.getData(NsAttachmentTypes.CRAWL_ATTACHMENT);
            PacketDistributor.sendToPlayer(trackingPlayer, new PlayerStateSyncPacket(targetPlayer.getId(), attachment.isCrawling()));
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            PlayerStateAttachment attachment = player.getData(NsAttachmentTypes.CRAWL_ATTACHMENT);
            PacketDistributor.sendToPlayer(player, new PlayerStateSyncPacket(player.getId(), attachment.isCrawling()));
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
         if (event.getEntity() instanceof ServerPlayer player) {
            PlayerStateAttachment attachment = player.getData(NsAttachmentTypes.CRAWL_ATTACHMENT);
            // Reset crawling state on respawn if desired, or sync if it persists (usually reset)
            attachment.setCrawling(false);
            PacketDistributor.sendToPlayer(player, new PlayerStateSyncPacket(player.getId(), false));
        }
    }
}

