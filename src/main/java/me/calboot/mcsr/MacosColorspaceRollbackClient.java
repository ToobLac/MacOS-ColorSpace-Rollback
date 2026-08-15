package me.calboot.mcsr;

import net.fabricmc.api.ClientModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MacosColorspaceRollbackClient implements ClientModInitializer {

	public static final String MOD_ID = "mac-colorspace-rollback";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		// NO-OP
	}
}
