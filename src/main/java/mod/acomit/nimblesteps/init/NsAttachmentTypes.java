package mod.acomit.nimblesteps.init;

import mod.acomit.nimblesteps.NimbleStepsMod;
import mod.acomit.nimblesteps.attachment.NimbleStepsState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-12-21 16:08
 * @Description: TODO
 */
public class NsAttachmentTypes {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, NimbleStepsMod.MODID);

    public static final Supplier<AttachmentType<NimbleStepsState>> CRAWL_ATTACHMENT = ATTACHMENT_TYPES.register(
            "crawl", () -> AttachmentType.serializable(NimbleStepsState::new)
                    .sync(NimbleStepsState.STREAM_CODEC)
                    .build()
    );

    public static void register(IEventBus bus) {
        ATTACHMENT_TYPES.register(bus);
    }
}
