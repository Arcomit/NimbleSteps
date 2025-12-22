package mod.acomit.nimblesteps.event.handler;

import mod.acomit.nimblesteps.ServerConfig;
import mod.acomit.nimblesteps.NimbleStepsMod;
import mod.acomit.nimblesteps.client.NsKeyBindings;
import mod.acomit.nimblesteps.attachment.NimbleStepsState;
import mod.acomit.nimblesteps.client.NsKeyMapping;
import mod.acomit.nimblesteps.client.event.InputJustPressedEvent;
import mod.acomit.nimblesteps.init.NsAttachmentTypes;
import mod.acomit.nimblesteps.network.CancelSlidePacket;
import mod.acomit.nimblesteps.network.SetLandingRollWindowPacket;
import mod.acomit.nimblesteps.network.UpdateCrawlStatePacket;
import mod.acomit.nimblesteps.network.UseSlideOrEvadePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * Author: Arcomit
 * CreateTime: 2025-12-20
 * Description: 爬行/翻滚/滑铲
 */
@EventBusSubscriber(modid = NimbleStepsMod.MODID)
public class CrawlRollSlideHandler {

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void inputJustPressed(InputJustPressedEvent event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        NsKeyMapping key = event.getKeyMapping();
        if (key == NsKeyBindings.ROLL_KEY) {
            NimbleStepsState nimbleStepsState = player.getData(NsAttachmentTypes.CRAWL_ATTACHMENT);

            // Landing Roll Logic
            if (!player.onGround() && player.fallDistance > 3.0f) {
                // TODO: 翻滚动画
                nimbleStepsState.setLandingRollWindow(10); // 0.5s window (10 ticks)
                PacketDistributor.sendToServer(new SetLandingRollWindowPacket(10));
                return;
                // Don't return, allow normal roll/slide logic if applicable, or maybe return?
                // If we are falling, we probably want to trigger the roll animation upon landing if timed correctly.
                // For now, just setting the window.
            }

            if (nimbleStepsState.isCrawling()) {
                nimbleStepsState.setCrawling(false);
                player.setForcedPose(null);
                PacketDistributor.sendToServer(new UpdateCrawlStatePacket(false));
                return;
            }

            float forwardImpulse = 0;
            float leftImpulse = 0;
            boolean hasInput = false;
            if (player instanceof LocalPlayer localPlayer) {
                forwardImpulse = localPlayer.input.forwardImpulse;
                leftImpulse = localPlayer.input.leftImpulse;
                hasInput = forwardImpulse != 0 || leftImpulse != 0;
            }

            if (!hasInput) {
                if (!canCrawl(player)) return;
                nimbleStepsState.setCrawling(true);
                player.setForcedPose(Pose.SWIMMING);
                PacketDistributor.sendToServer(new UpdateCrawlStatePacket(true));
            } else {
                if (!player.onGround() && forwardImpulse < 0) return;
                if (!canSlideOrEvade(player)) return;
                applySlideOrEvade(player, forwardImpulse, leftImpulse);
                PacketDistributor.sendToServer(new UseSlideOrEvadePacket(forwardImpulse, leftImpulse));
            }
        }
    }

    @SubscribeEvent
    public static void tickPlayer(PlayerTickEvent.Pre event) {
        // 检查玩家是否仍然可以保持爬行状态
        Player player = event.getEntity();
        NimbleStepsState nimbleStepsState = player.getData(NsAttachmentTypes.CRAWL_ATTACHMENT);
        if (nimbleStepsState.isCrawling()) {
            if (!canCrawl(player)) {
                nimbleStepsState.setCrawling(false);
                player.setForcedPose(null);
                player.setData(NsAttachmentTypes.CRAWL_ATTACHMENT, nimbleStepsState);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void tickPlayerClient(PlayerTickEvent.Pre event) {
        // 客户端检查玩家是否仍然可以保持滑铲状态
        Player player = event.getEntity();
        if (player.level().isClientSide()) {
            NimbleStepsState nimbleStepsState = player.getData(NsAttachmentTypes.CRAWL_ATTACHMENT);
            int slideDuration = nimbleStepsState.getSlideDuration();
            if (slideDuration > 0) {
                if (player.getDeltaMovement().length() < 0.1) {
                    nimbleStepsState.setSlideDuration(0);
                    player.setForcedPose(null);
                    PacketDistributor.sendToServer(new CancelSlidePacket());
                }
            }
        }
    }

    @SubscribeEvent
    public static void tickPlayerServer(PlayerTickEvent.Pre event) {
        // 双端处理滑铲状态自然结束
        Player player = event.getEntity();
        NimbleStepsState nimbleStepsState = player.getData(NsAttachmentTypes.CRAWL_ATTACHMENT);
        int slideDuration = nimbleStepsState.getSlideDuration();
        if (slideDuration > 0) {
            nimbleStepsState.setSlideDuration(slideDuration - 1);
            if (slideDuration - 1 == 0) {
                player.setForcedPose(null);
            }
        }
    }

    // 如果玩家是旁观者模式、乘坐实体、滑翔、睡觉、游泳或自动旋转攻击，则不可进入爬行状态
    public static boolean canCrawl(Player player) {
        return ServerConfig.enableCrawl
                && !player.isSpectator()
                && !player.isPassenger()
                && !player.isFallFlying()
                && !player.isSleeping()
                && !player.isSwimming()
                && !player.isAutoSpinAttack();
    }

    public static boolean canSlideOrEvade(Player player) {
        NimbleStepsState attachment = player.getData(NsAttachmentTypes.CRAWL_ATTACHMENT);
        return ServerConfig.enableSlideAndEvade
                && attachment.getSlideAndEvadeCooldown() <= 0
                && !attachment.isCrawling()
                && !player.isCrouching()
                && !player.isSpectator()
                && !player.isPassenger()
                && !player.isFallFlying()
                && !player.isSleeping()
                && !player.isSwimming()
                && !player.isInWater()
                && !player.isAutoSpinAttack();
    }

    public static void applySlideOrEvade(Player player, float forwardImpulse, float leftImpulse) {
        NimbleStepsState nimbleStepsState = player.getData(NsAttachmentTypes.CRAWL_ATTACHMENT);
        nimbleStepsState.setSlideAndEvadeCooldown(ServerConfig.slideAndEvadeCooldown);

        float forward = forwardImpulse;

        if (!player.onGround() && forward < 0) return;

        if (forward == 0 && leftImpulse == 0) {
            forward = 1.0f;
        }

        if (forward >= 0) {
            nimbleStepsState.setSlideDuration(10); // Set slide duration (e.g., 10 ticks)
            player.setForcedPose(Pose.SWIMMING);
            // TODO: 滑铲动画
        }

        double boost = 0.6;
        float yRot = player.getYRot() * ((float)Math.PI / 180F);
        float sin = (float)Math.sin(yRot);
        float cos = (float)Math.cos(yRot);

        double x = leftImpulse * cos - forward * sin;
        double z = forward * cos + leftImpulse * sin;

        if (!player.onGround()) {
            player.yRotO = player.getYRot();
            double targetYRot = Math.toDegrees(Math.atan2(-x, z));
            player.setYRot((float) targetYRot);
        }

        Vec3 motion = new Vec3(x, 0, z).normalize().scale(boost);

        player.setDeltaMovement(
                player.getDeltaMovement().add(motion)
        );
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onMovementInput(MovementInputUpdateEvent event) {
        // 禁用滑铲状态下的跳跃输入
        Player player = event.getEntity();
        NimbleStepsState nimbleStepsState = player.getData(NsAttachmentTypes.CRAWL_ATTACHMENT);
        if (nimbleStepsState.getSlideDuration() <= 0) return;
        event.getInput().jumping = false;
    }

    @SubscribeEvent
    public static void updateCooldown(PlayerTickEvent.Pre event) {
        // 处理滑铲/闪避冷却时间
        Player player = event.getEntity();
        NimbleStepsState nimbleStepsState = player.getData(NsAttachmentTypes.CRAWL_ATTACHMENT);
        int slideAndEvadeCooldown = nimbleStepsState.getSlideAndEvadeCooldown();
        if (slideAndEvadeCooldown > 0) {
            nimbleStepsState.setSlideAndEvadeCooldown(slideAndEvadeCooldown - 1);
        }

        int landingRollWindow = nimbleStepsState.getLandingRollWindow();
        if (landingRollWindow > 0) {
            nimbleStepsState.setLandingRollWindow(landingRollWindow - 1);
        }
    }

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        if (event.getEntity() instanceof Player player) {
            NimbleStepsState nimbleStepsState = player.getData(NsAttachmentTypes.CRAWL_ATTACHMENT);
            if (nimbleStepsState.getLandingRollWindow() > 0) {
                event.setDamageMultiplier(0);
                event.setCanceled(true); // Cancel fall damage
                // Trigger roll animation/logic here if needed
                // For example, force a slide or roll
                if (!player.level().isClientSide) {
                     // Maybe apply a forward boost or just play sound
                }
            }
        }
    }

}
