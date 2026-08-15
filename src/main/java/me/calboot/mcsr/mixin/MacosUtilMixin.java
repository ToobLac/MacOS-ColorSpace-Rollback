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

import com.mojang.blaze3d.platform.MacosUtil;
import me.calboot.mcsr.MacosColorspaceRollbackClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(MacosUtil.class)
public class MacosUtilMixin {

    @ModifyConstant(
            method = "lambda$setWindowColorSpaceForOpenGLBecauseGLFWDoesnt$0",
            constant = @Constant(stringValue = "sRGBColorSpace")
    )
    private static String modifyColorSpace(String constant) {
        // For OpenGL
        MacosColorspaceRollbackClient.LOGGER.info("Modifying color space for OpenGL");
        return "displayP3ColorSpace";
    }

}
