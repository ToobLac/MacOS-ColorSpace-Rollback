package me.calboot.mcsr.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.renderpearl.api.device.GpuBackend;
import com.mojang.renderpearl.backend.opengl.GlBackend;
import me.calboot.mcsr.MacosUtil;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;initRenderer(Lcom/mojang/renderpearl/api/device/GpuDevice;)V"
            )
    )
    private void setMainWindowColorSpace(CallbackInfo ci, @Local GpuBackend backend) {
        if (MacosUtil.IS_MACOS && backend instanceof GlBackend) {
            MacosUtil.setMainWindowColorSpace();
        }
    }


}
