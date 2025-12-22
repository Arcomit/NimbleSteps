package mod.acomit.nimblesteps.event;

import mod.acomit.nimblesteps.CommonConfig;
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
 * @Description: 可翻越的方块高度
 */
@EventBusSubscriber(modid = NimbleStepsMod.MODID)
public class StepAssistHandler {
    private static final ResourceLocation WALK_STEP_HEIGHT_MODIFIER_ID = NimbleStepsMod.prefix("walk_step_height");
    private static final ResourceLocation SPRINT_STEP_HEIGHT_MODIFIER_ID = NimbleStepsMod.prefix("sprint_step_height");

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();
        AttributeInstance movementSpeed = player.getAttribute(Attributes.STEP_HEIGHT);
        if (movementSpeed == null) {
            return;
        }
        // 移除旧的修改器
        movementSpeed.removeModifier(WALK_STEP_HEIGHT_MODIFIER_ID);
        movementSpeed.removeModifier(SPRINT_STEP_HEIGHT_MODIFIER_ID);

        if (player.isSprinting()) {
            double sprintBonus = CommonConfig.sprinStepHeight - 0.6;
            AttributeModifier sprintModifier = new AttributeModifier(
                    SPRINT_STEP_HEIGHT_MODIFIER_ID,
                    sprintBonus,
                    AttributeModifier.Operation.ADD_VALUE
            );
            movementSpeed.addTransientModifier(sprintModifier);
        } else {
            double walkBonus = CommonConfig.walkStepHeight - 0.6;
            AttributeModifier walkModifier = new AttributeModifier(
                    WALK_STEP_HEIGHT_MODIFIER_ID,
                    walkBonus,
                    AttributeModifier.Operation.ADD_VALUE
            );
            movementSpeed.addTransientModifier(walkModifier);
        }
    }
}

