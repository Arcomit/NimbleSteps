package mod.acomit.nimblesteps.network;

import mod.acomit.nimblesteps.NimbleStepsMod;
import io.netty.buffer.ByteBuf;
import mod.acomit.nimblesteps.ServerConfig;
import mod.acomit.nimblesteps.attachment.NimbleStepsState;
import mod.acomit.nimblesteps.event.handler.SwimmingBoostHandler;
import mod.acomit.nimblesteps.init.NsAttachmentTypes;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-12-22 14:03
 * @Description: 水中推进的网络包
 */
public class UseSwimmingBoostPacket implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<UseSwimmingBoostPacket> TYPE = new CustomPacketPayload.Type<>(NimbleStepsMod.prefix("swimming_boost"));
    public static final StreamCodec<ByteBuf, UseSwimmingBoostPacket> STREAM_CODEC = StreamCodec.unit(new UseSwimmingBoostPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(UseSwimmingBoostPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                if (!SwimmingBoostHandler.canSwimmingBoost(player)) return;
                SwimmingBoostHandler.applySwimmingBoost(player);
                player.level().playSound(null,
                        player.getX(), player.getY(), player.getZ(),
                        SoundEvents.AMBIENT_UNDERWATER_ENTER,
                        SoundSource.PLAYERS,
                        0.9f, 0.8f);
                // 避免触发服务端反作弊回拉
                if (player.connection != null) {
                    player.connection.resetPosition();
                }
            }
        });
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof UseSwimmingBoostPacket;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
