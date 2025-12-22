package mod.acomit.nimblesteps.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-12-21 19:01
 * @Description: TODO:攀爬改革1
 */
@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "onClimbable", at = @At("HEAD"), cancellable = true)
    public void onClimbableMixin(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;

        // 获取脚下的方块位置
        BlockPos pos = entity.blockPosition();
        BlockState state = entity.level().getBlockState(pos);

        // 如果符合我们的自定义攀爬逻辑，直接返回 true，跳过原版判断
        if (isClimbablePole(state, entity.level(), pos)) {
            cir.setReturnValue(true);
        }
    }

    /**
     * 自定义判断逻辑：判断方块是否像杆子一样可攀爬
     */
    @Unique
    private boolean isClimbablePole(BlockState state, Level world, BlockPos pos) {
        Block block = state.getBlock();

        // 1. 铁栏杆、玻璃板等 (FourWayBlock)
        // 或者是 栅栏 (FenceBlock) - Minecraft代码中栅栏通常也继承自类似的连接属性
        if (block instanceof CrossCollisionBlock) {
            // 逻辑：必须是“堆叠”的才能爬（防止只是路过一个单独的栏杆就触发攀爬）
            boolean isStacked = isSameBlockType(world, pos.above(), block) ||
                    isSameBlockType(world, pos.below(), block);

            // 可以在这里加更复杂的判断，比如 ParCool 判断了水平方向的连接数
            // 如果只有上下连接（像一根棍子），才允许攀爬
            return isStacked;
        }
        // 2. 锁链 (Chain) 或 其它旋转柱体
        else if (block instanceof RotatedPillarBlock) {
            // 必须是垂直摆放的
            boolean isVertical = state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y;
            // 同样需要堆叠检测，避免爬地上的锁链
            boolean isStacked = isSameBlockType(world, pos.above(), block) ||
                    isSameBlockType(world, pos.below(), block);

            // 且不能是实心方块（排除原木等）- 锁链不是实心的，原木是
            boolean notFullBlock = !state.isCollisionShapeFullBlock(world, pos);

            return isVertical && isStacked && notFullBlock;
        }
        // 3. 末地烛 (End Rod)
        else if (block instanceof EndRodBlock) {
            Direction facing = state.getValue(EndRodBlock.FACING);
            // 必须是垂直朝向
            return facing == Direction.UP || facing == Direction.DOWN;
        }

        return false;
    }

    @Unique
    private boolean isSameBlockType(Level world, BlockPos pos, Block targetBlock) {
        return world.isLoaded(pos) && world.getBlockState(pos).getBlock() == targetBlock;
    }
}
