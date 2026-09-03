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

package me.calboot.mcsr;

import ca.weblite.objc.Client;
import ca.weblite.objc.Proxy;

import java.util.Locale;

public class MacosUtil {

    public static final boolean IS_MACOS = System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("mac");

    public static void setMainWindowColorSpace() {
        Proxy windows = Client.getInstance()
                .sendProxy("NSApplication", "sharedApplication")
                .sendProxy("windows");
        if (windows != null) {
            int count = windows.sendInt("count");
            if (count > 0) {
                MacosColorspaceRollbackClient.LOGGER.info("Modifying color space for OpenGL windows");
                for (int i = 0; i < count; i++) {
                    Proxy window = windows.sendProxy("objectAtIndex:", i);
                    window.send("setColorSpace:", window.getClient().send("NSColorSpace", "displayP3ColorSpace"));
                }
            }

        }

        MacosColorspaceRollbackClient.LOGGER.warn("Failed to modify color space, due to absence of native windows");
    }

}
