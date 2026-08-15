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
