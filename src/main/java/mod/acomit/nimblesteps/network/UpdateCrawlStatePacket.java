package mod.acomit.nimblesteps.network;

import mod.acomit.nimblesteps.NimbleStepsMod;
import mod.acomit.nimblesteps.attachment.NimbleStepsState;
import mod.acomit.nimblesteps.event.handler.RollSlideCrawlHandler;
import mod.acomit.nimblesteps.init.NsAttachmentTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Pose;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-12-21 16:15
 * @Description: 更新爬行状态网络包
 */
public record UpdateCrawlStatePacket(boolean isCrawling) implements CustomPacketPayload {
    public static final Type<UpdateCrawlStatePacket> TYPE = new Type<>(NimbleStepsMod.prefix("crawl"));
    public static final StreamCodec<FriendlyByteBuf, UpdateCrawlStatePacket> STREAM_CODEC = StreamCodec.composite(
            StreamCodec.of(FriendlyByteBuf::writeBoolean, FriendlyByteBuf::readBoolean),
            UpdateCrawlStatePacket::isCrawling,
            UpdateCrawlStatePacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(UpdateCrawlStatePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                if (!RollSlideCrawlHandler.canCrawl(player)) return;
                boolean isCrawling = packet.isCrawling();
                NimbleStepsState attachment = player.getData(NsAttachmentTypes.CRAWL_ATTACHMENT);
                attachment.setCrawling(isCrawling);
                player.setData(NsAttachmentTypes.CRAWL_ATTACHMENT, attachment);
                if (isCrawling) {
                    player.setForcedPose(Pose.SWIMMING);
                }else {
                    player.setForcedPose(null);
                }
            }
        });
    }
}
