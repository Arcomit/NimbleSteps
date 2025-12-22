package mod.acomit.nimblesteps.network;

import mod.acomit.nimblesteps.NimbleStepsMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-12-22 14:03
 * @Description: TODO
 */
public class SwimmingBoostPacket implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SwimmingBoostPacket> TYPE = new CustomPacketPayload.Type<>(NimbleStepsMod.prefix("swimming_boost"));
    public static final StreamCodec<ByteBuf, SwimmingBoostPacket> STREAM_CODEC = StreamCodec.unit(new SwimmingBoostPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SwimmingBoostPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                Vec3 lookDirection = player.getLookAngle();
                double boost = 0.4;
                player.setDeltaMovement(
                        player.getDeltaMovement().add(lookDirection.x * boost, lookDirection.y * boost, lookDirection.z * boost)
                );
                player.level().playSound(null,
                        player.getX(), player.getY(), player.getZ(),
                        SoundEvents.AMBIENT_UNDERWATER_ENTER,
                        SoundSource.PLAYERS,
                        0.9f, 0.8f);
                if (player.connection != null) {
                    player.connection.resetPosition();
                }
            }
        });
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SwimmingBoostPacket;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
