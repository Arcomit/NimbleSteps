package mod.acomit.nimblesteps.attachment;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

public class PlayerStateAttachment implements INBTSerializable<CompoundTag> {
    public static final StreamCodec<ByteBuf, PlayerStateAttachment> STREAM_CODEC = StreamCodec.of(
            (buf, attachment) -> {
                buf.writeBoolean(attachment.isCrawling);
            },
            (buf) -> {
                PlayerStateAttachment attachment = new PlayerStateAttachment();
                attachment.isCrawling = buf.readBoolean();
                return attachment;
            }
    );

    private boolean isCrawling;
    private

    public boolean isCrawling() {
        return isCrawling;
    }
    public void setCrawling(boolean crawling) {
        isCrawling = crawling;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("isCrawling", isCrawling);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, @NotNull CompoundTag nbt) {
        isCrawling = nbt.getBoolean("isCrawling");
    }
}
