package mod.acomit.nimblesteps.network;

import io.netty.buffer.ByteBuf;
import mod.acomit.nimblesteps.NimbleStepsMod;
import mod.acomit.nimblesteps.event.handler.RollSlideCrawlHandler;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-12-22 16:29
 * @Description: 使用滑铲或闪避网络包
 */
public record UseSlideOrEvadePacket(float forwardImpulse, float leftImpulse) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<UseSlideOrEvadePacket> TYPE = new CustomPacketPayload.Type<>(NimbleStepsMod.prefix("slide_and_evade"));
    public static final StreamCodec<ByteBuf, UseSlideOrEvadePacket> STREAM_CODEC = StreamCodec.of(
            (buf, packet) -> {
                buf.writeFloat(packet.forwardImpulse);
                buf.writeFloat(packet.leftImpulse);
            },
            (buf) -> new UseSlideOrEvadePacket(buf.readFloat(), buf.readFloat())
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(UseSlideOrEvadePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                if (!RollSlideCrawlHandler.canSlideOrEvade(player)) return;
                RollSlideCrawlHandler.applySlideOrEvade(player, packet.forwardImpulse, packet.leftImpulse);
                // TODO:  滑铲专属音效
                player.level().playSound(null,
                        player.getX(), player.getY(), player.getZ(),
                        SoundEvents.GENERIC_SMALL_FALL,
                        SoundSource.PLAYERS,
                        0.5f, 0.8f);

                // 避免触发服务端反作弊回拉
                if (player.connection != null) {
                    player.connection.resetPosition();
                }
            }
        });
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof UseSlideOrEvadePacket;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}