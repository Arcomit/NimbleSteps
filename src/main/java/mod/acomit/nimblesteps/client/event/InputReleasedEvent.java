package mod.acomit.nimblesteps.event;

import lombok.Getter;
import mod.acomit.nimblesteps.client.NsKeyMapping;
import net.neoforged.bus.api.Event;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-12-21 16:52
 * @Description: 按键释放事件
 */
public class InputReleasedEvent extends Event {
    @Getter
    private final NsKeyMapping keyMapping;

    public InputReleasedEvent(NsKeyMapping keyMapping) {
        this.keyMapping = keyMapping;
    }
}
