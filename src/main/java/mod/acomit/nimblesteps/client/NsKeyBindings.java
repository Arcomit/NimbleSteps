package mod.acomit.nimblesteps.client;

import com.mojang.blaze3d.platform.InputConstants;
import mod.acomit.nimblesteps.NimbleStepsMod;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-12-21 16:15
 * @Description: TODO
 */
@EventBusSubscriber(modid = NimbleStepsMod.MODID, value = Dist.CLIENT)
public class NsKeyBindings {
    public static final String CATEGORY = "key.categories." + NimbleStepsMod.MODID;

    public static final KeyMapping ROLL_KEY = new NsKeyMapping(
            "key." + NimbleStepsMod.MODID + ".roll_slide",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            CATEGORY
    );

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(ROLL_KEY);
    }
}

