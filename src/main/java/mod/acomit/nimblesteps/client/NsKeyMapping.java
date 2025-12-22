package mod.acomit.nimblesteps.client;

import com.mojang.blaze3d.platform.InputConstants;
import mod.acomit.nimblesteps.client.event.InputJustPressedEvent;
import mod.acomit.nimblesteps.client.event.InputReleasedEvent;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForge;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-12-21 16:34
 * @Description: TODO
 */
@OnlyIn(Dist.CLIENT)
public class NsKeyMapping extends KeyMapping {

    public NsKeyMapping(String name, InputConstants.Type type, int keyCode, String category) {
        super(name, type, keyCode, category);
    }

    @Override
    public void setDown(boolean value) {
        if (this.isDown() == value) {
            return;
        }
        if (value) {
            NeoForge.EVENT_BUS.post(new InputJustPressedEvent(this));
        }else {
            NeoForge.EVENT_BUS.post(new InputReleasedEvent(this));
        }
        super.setDown(value);
    }
}
