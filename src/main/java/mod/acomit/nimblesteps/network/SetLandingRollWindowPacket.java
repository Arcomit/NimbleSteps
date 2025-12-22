package mod.acomit.nimblesteps.network;

import io.netty.buffer.ByteBuf;
import mod.acomit.nimblesteps.NimbleStepsMod;
import mod.acomit.nimblesteps.attachment.NimbleStepsState;
import mod.acomit.nimblesteps.init.NsAttachmentTypes;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-12-21 16:15
 * @Description: 设置着陆翻滚窗口网络包
 */
public record SetLandingRollWindowPacket(int window) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SetLandingRollWindowPacket> TYPE = new CustomPacketPayload.Type<>(NimbleStepsMod.prefix("set_landing_roll_window"));
    public static final StreamCodec<ByteBuf, SetLandingRollWindowPacket> STREAM_CODEC = StreamCodec.of(
            (buf, packet) -> buf.writeInt(packet.window),
            (buf) -> new SetLandingRollWindowPacket(buf.readInt())
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SetLandingRollWindowPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                NimbleStepsState state = player.getData(NsAttachmentTypes.CRAWL_ATTACHMENT);
                state.setLandingRollWindow(packet.window);
            }
        });
    }
}

