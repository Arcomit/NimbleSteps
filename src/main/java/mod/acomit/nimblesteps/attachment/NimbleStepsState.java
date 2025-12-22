package mod.acomit.nimblesteps.attachment;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

public class NimbleStepsState implements INBTSerializable<CompoundTag> {
    public static final StreamCodec<ByteBuf, NimbleStepsState> STREAM_CODEC = StreamCodec.of(
            (buf, attachment) -> {
                buf.writeBoolean(attachment.isCrawling);
                buf.writeInt(attachment.swimmingBoostCooldown);
                buf.writeInt(attachment.slideAndEvadeCooldown);
                buf.writeInt(attachment.slideDuration);
                buf.writeInt(attachment.landingRollWindow);
            },
            (buf) -> {
                NimbleStepsState attachment = new NimbleStepsState();
                attachment.isCrawling = buf.readBoolean();
                attachment.swimmingBoostCooldown = buf.readInt();
                attachment.slideAndEvadeCooldown = buf.readInt();
                attachment.slideDuration = buf.readInt();
                attachment.landingRollWindow = buf.readInt();
                return attachment;
            }
    );

    @Getter @Setter
    private boolean isCrawling;
    @Getter @Setter
    private int swimmingBoostCooldown;
    @Getter @Setter
    private int slideAndEvadeCooldown;
    @Getter @Setter
    private int slideDuration;
    @Getter @Setter
    private int landingRollWindow;

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("isCrawling", isCrawling);
        tag.putInt("swimmingBoostCooldown", swimmingBoostCooldown);
        tag.putInt("slideAndEvadeCooldown", slideAndEvadeCooldown);
        tag.putInt("slideDuration", slideDuration);
        tag.putInt("landingRollWindow", landingRollWindow);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, @NotNull CompoundTag nbt) {
        isCrawling = nbt.getBoolean("isCrawling");
        swimmingBoostCooldown = nbt.getInt("swimmingBoostCooldown");
        slideAndEvadeCooldown = nbt.getInt("slideAndEvadeCooldown");
        slideDuration = nbt.getInt("slideDuration");
        landingRollWindow = nbt.getInt("landingRollWindow");
    }
}
