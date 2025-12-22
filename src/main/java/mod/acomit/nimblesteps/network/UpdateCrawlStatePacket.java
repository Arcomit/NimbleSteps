package mod.acomit.nimblesteps.network;

import mod.acomit.nimblesteps.NimbleStepsMod;
import mod.acomit.nimblesteps.attachment.PlayerStateAttachment;
import mod.acomit.nimblesteps.init.NsAttachmentTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-12-21 16:15
 * @Description: TODO
 */
public record CrawlPacket(boolean isCrawling) implements CustomPacketPayload {
    public static final Type<CrawlPacket> TYPE = new Type<>(NimbleStepsMod.prefix("crawl"));

    public static final StreamCodec<FriendlyByteBuf, CrawlPacket> STREAM_CODEC = StreamCodec.composite(
            StreamCodec.of(FriendlyByteBuf::writeBoolean, FriendlyByteBuf::readBoolean),
            CrawlPacket::isCrawling,
            CrawlPacket::new
    );

    @Override
    @NotNull
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(CrawlPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                PlayerStateAttachment attachment = player.getData(NsAttachmentTypes.CRAWL_ATTACHMENT);
                attachment.setCrawling(packet.isCrawling());
                player.setData(NsAttachmentTypes.CRAWL_ATTACHMENT, attachment);
            }
        });
    }
}
