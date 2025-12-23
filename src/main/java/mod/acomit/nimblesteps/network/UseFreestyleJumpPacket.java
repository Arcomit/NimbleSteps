package mod.acomit.nimblesteps.network;

import io.netty.buffer.ByteBuf;
import mod.acomit.nimblesteps.NimbleStepsMod;
import mod.acomit.nimblesteps.event.handler.FreestyleHandler;
import mod.acomit.nimblesteps.event.handler.SwimmingBoostHandler;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-12-23 17:34
 * @Description: TODO
 */
public class UseFreestyleJumpPacket implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<UseFreestyleJumpPacket> TYPE = new CustomPacketPayload.Type<>(NimbleStepsMod.prefix("freestyle_jump"));
    public static final StreamCodec<ByteBuf, UseFreestyleJumpPacket> STREAM_CODEC = StreamCodec.unit(new UseFreestyleJumpPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(UseFreestyleJumpPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                if (!FreestyleHandler.canFreestyle(player)) return;
                FreestyleHandler.applyFreestyleJump(player);
                // 避免触发服务端反作弊回拉
                if (player.connection != null) {
                    player.connection.resetPosition();
                }
            }
        });
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof UseFreestyleJumpPacket;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}