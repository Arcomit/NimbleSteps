package mod.acomit.nimblesteps.event.handler;

import mod.acomit.nimblesteps.ServerConfig;
import mod.acomit.nimblesteps.NimbleStepsMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-12-20 16:24
 * @Description: 移动加速
 */
@EventBusSubscriber(modid = NimbleStepsMod.MODID)
public class MoveAccelerationHandler {
    private static final ResourceLocation WALK_SPEED_MULTIPLIER_MODIFIER_ID = NimbleStepsMod.prefix("walk_speed_multiplier");
    private static final ResourceLocation SPRINT_SPEED_MULTIPLIER_MODIFIER_ID = NimbleStepsMod.prefix("sprint_speed_multiplier");

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();
        AttributeInstance movementSpeed = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (movementSpeed == null) {
            return;
        }
        // 移除旧的修改器
        movementSpeed.removeModifier(WALK_SPEED_MULTIPLIER_MODIFIER_ID);
        movementSpeed.removeModifier(SPRINT_SPEED_MULTIPLIER_MODIFIER_ID);

        if (player.isSprinting()) {
            double sprintBonus = ServerConfig.sprintSpeedMultiplier - 1.0;
            AttributeModifier sprintModifier = new AttributeModifier(
                    SPRINT_SPEED_MULTIPLIER_MODIFIER_ID,
                    sprintBonus,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE
            );
            movementSpeed.addTransientModifier(sprintModifier);
        } else {
            double walkBonus = ServerConfig.walkSpeedMultiplier - 1.0;
            AttributeModifier walkModifier = new AttributeModifier(
                    WALK_SPEED_MULTIPLIER_MODIFIER_ID,
                    walkBonus,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE
            );
            movementSpeed.addTransientModifier(walkModifier);
        }
    }
}
