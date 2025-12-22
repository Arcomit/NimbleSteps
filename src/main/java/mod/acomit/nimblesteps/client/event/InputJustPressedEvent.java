package mod.acomit.nimblesteps.event;

import lombok.Getter;
import mod.acomit.nimblesteps.client.NsKeyMapping;
import net.minecraft.network.chat.ClickEvent;
import net.neoforged.bus.api.Event;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-12-21 16:49
 * @Description: 按键 "刚" 输入的事件
 */
public class InputJustPressedEvent extends Event {
    @Getter private final NsKeyMapping keyMapping;
ClickEvent
    public InputJustPressedEvent(NsKeyMapping keyMapping) {
        this.keyMapping = keyMapping;
    }
}
