package mod.acomit.nimblesteps.network;

import mod.acomit.nimblesteps.NimbleStepsMod;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-12-21 16:15
 * @Description: 注册网络通讯数据包
 */
@EventBusSubscriber(modid = NimbleStepsMod.MODID)
public class RegisterPayloadHandler {

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(
                UpdateCrawlStatePacket.TYPE,
                UpdateCrawlStatePacket.STREAM_CODEC,
                UpdateCrawlStatePacket::handle
        );
        registrar.playToServer(
                UseSwimmingBoostPacket.TYPE,
                UseSwimmingBoostPacket.STREAM_CODEC,
                UseSwimmingBoostPacket::handle
        );
        registrar.playToServer(
                UseSlideOrEvadePacket.TYPE,
                UseSlideOrEvadePacket.STREAM_CODEC,
                UseSlideOrEvadePacket::handle
        );
        registrar.playToServer(
                CancelSlidePacket.TYPE,
                CancelSlidePacket.STREAM_CODEC,
                CancelSlidePacket::handle
        );
        registrar.playToServer(
                SetLandingRollWindowPacket.TYPE,
                SetLandingRollWindowPacket.STREAM_CODEC,
                SetLandingRollWindowPacket::handle
        );
        registrar.playToServer(
                UseFreestyleJumpPacket.TYPE,
                UseFreestyleJumpPacket.STREAM_CODEC,
                UseFreestyleJumpPacket::handle
        );
    }
}
