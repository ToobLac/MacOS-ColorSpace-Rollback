package me.calboot.mcsr.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.platform.MacosUtil;
import com.mojang.blaze3d.vulkan.VulkanGpuSurface;
import me.calboot.mcsr.MacosColorspaceRollbackClient;
import org.lwjgl.vulkan.VkSurfaceFormatKHR;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static org.lwjgl.vulkan.EXTSwapchainColorspace.VK_COLOR_SPACE_DISPLAY_P3_NONLINEAR_EXT;

@Mixin(VulkanGpuSurface.class)
public class VulkanGpuSurfaceMixin {

    @WrapMethod(method = "pickSwapchainSurfaceFormat")
    public VkSurfaceFormatKHR wrapPickFormat(VkSurfaceFormatKHR.Buffer formats, Operation<VkSurfaceFormatKHR> original) {
        if (MacosUtil.IS_MACOS) {
            MacosColorspaceRollbackClient.LOGGER.info("Modifying swapchain format for Vulkan");
            for (VkSurfaceFormatKHR format : formats) {
                if (format.colorSpace() == VK_COLOR_SPACE_DISPLAY_P3_NONLINEAR_EXT && (format.format() == 37 || format.format() == 44)) {
                    return format;
                }
            }

            throw new IllegalStateException("Could not find compatible swapchain format");
        }
        return original.call(formats);
    }

    @ModifyArg(
            method = "configure",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/vulkan/VkSwapchainCreateInfoKHR;imageColorSpace(I)Lorg/lwjgl/vulkan/VkSwapchainCreateInfoKHR;"
            )
    )
    public int modifyImageColorSpace(int imageColorSpace) {
        if (MacosUtil.IS_MACOS) {
            MacosColorspaceRollbackClient.LOGGER.info("Modifying swapchain color space for Vulkan");
            return VK_COLOR_SPACE_DISPLAY_P3_NONLINEAR_EXT;
        }
        return imageColorSpace;
    }

}
