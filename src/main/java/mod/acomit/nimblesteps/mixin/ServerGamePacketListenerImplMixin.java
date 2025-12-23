package mod.acomit.nimblesteps.mixin;

import mod.acomit.nimblesteps.ServerConfig;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;


@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {

    @ModifyConstant(method = "handleMovePlayer", constant = @Constant(floatValue = 100.0F))
    private float modifyConstantPlayerMaxSpeed(float speed) {
        if (ServerConfig.removeSpeedLimits) {
            return Float.MAX_VALUE;
        }
        return speed;
    }

    @ModifyConstant(method = "handleMovePlayer", constant = @Constant(floatValue = 300.0F))
    private float modifyConstantElytraMaxSpeed(float speed) {
        if (ServerConfig.removeSpeedLimits) {
            return Float.MAX_VALUE;
        }
        return speed;
    }

    @ModifyConstant(method = "handleMovePlayer", constant = @Constant(doubleValue = 0.0625))
    private double modifyConstantMovedWrong(double speed) {
        if (ServerConfig.removeSpeedLimits) {
            return Double.MAX_VALUE;
        }
        return speed;
    }

    @ModifyConstant(method = "handleMoveVehicle", constant = @Constant(doubleValue = 100.0))
    private double modifyConstantVehicleMaxSpeed(double speed) {
        if (ServerConfig.removeSpeedLimits) {
            return Double.MAX_VALUE;
        }
        return speed;
    }

    @ModifyConstant(method = "handleMoveVehicle", constant = @Constant(doubleValue = 0.0625))
    private double modifyConstantVehicleMovedWrong(double speed) {
        if (ServerConfig.removeSpeedLimits) {
            return Double.MAX_VALUE;
        }
        return speed;
    }
}