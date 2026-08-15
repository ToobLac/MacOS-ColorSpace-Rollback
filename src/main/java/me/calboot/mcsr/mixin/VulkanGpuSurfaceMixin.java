/*
 * This file is part of the MacOS Colorspace Rollback project, licensed under the
 * GNU General Public License v3.0
 *
 * Copyright (C) 2026  Calboot and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.calboot.mcsr.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.renderpearl.backend.vulkan.VulkanGpuSurface;
import me.calboot.mcsr.MacosColorspaceRollbackClient;
import me.calboot.mcsr.MacosUtil;
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
