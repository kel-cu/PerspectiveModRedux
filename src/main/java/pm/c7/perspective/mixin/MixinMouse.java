package pm.c7.perspective.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import pm.c7.perspective.PerspectiveMod;

@Mixin(Mouse.class)
public class MixinMouse {
    @Inject(
        method = "updateMouse",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/tutorial/TutorialManager;onUpdateMouse(DD)V"
        ),
        locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    private void perspectiveUpdatePitchYaw(double timeDelta, CallbackInfo ci, double i, double j) {
        if (PerspectiveMod.INSTANCE.perspectiveEnabled) {
            PerspectiveMod.INSTANCE.cameraYaw += i / 8.0F;
            PerspectiveMod.INSTANCE.cameraPitch += (j * (MinecraftClient.getInstance().options.getInvertYMouse().getValue() ? -1 : 1)) / 8.0F;

            if (Math.abs(PerspectiveMod.INSTANCE.cameraPitch) > 90.0F) {
                PerspectiveMod.INSTANCE.cameraPitch = PerspectiveMod.INSTANCE.cameraPitch > 0.0F ? 90.0F : -90.0F;
            }
        }
    }

    @Inject(
        method = "updateMouse",
        at = @At(
            value = "INVOKE",
            target = "net/minecraft/client/network/ClientPlayerEntity.changeLookDirection(DD)V"
        ),
            cancellable = true)
    private void perspectivePreventPlayerMovement(CallbackInfo info) {
        if (PerspectiveMod.INSTANCE.perspectiveEnabled) {
            info.cancel();
        }
    }
}
