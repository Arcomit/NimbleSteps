package mod.acomit.nimblesteps.network;

import io.netty.buffer.ByteBuf;
import mod.acomit.nimblesteps.NimbleStepsMod;
import mod.acomit.nimblesteps.attachment.NimbleStepsState;
import mod.acomit.nimblesteps.event.handler.SwimmingBoostHandler;
import mod.acomit.nimblesteps.init.NsAttachmentTypes;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-12-22 20:05
 * @Description: 取消滑铲网络包
 */
public class CancelSlidePacket implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<CancelSlidePacket> TYPE = new CustomPacketPayload.Type<>(NimbleStepsMod.prefix("cancel_slide"));
    public static final StreamCodec<ByteBuf, CancelSlidePacket> STREAM_CODEC = StreamCodec.unit(new CancelSlidePacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(CancelSlidePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                NimbleStepsState nimbldeStepsState = player.getData(NsAttachmentTypes.CRAWL_ATTACHMENT);
                nimbldeStepsState.setSlideDuration(0);
                player.setForcedPose(null);
            }
        });
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CancelSlidePacket;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}