package de.nrc.autotext;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public final class AutoTextClient implements ClientModInitializer {
    private static final int SEND_INTERVAL_TICKS = 300; // 15 seconds at 20 TPS

    private static KeyMapping openScreenKey;
    private static String message = "Hallo!";
    private static boolean enabled = false;
    private static int ticks = 0;

    @Override
    public void onInitializeClient() {
        KeyMapping.Category category = KeyMapping.Category.register(
                Identifier.fromNamespaceAndPath("autotextnrc", "main")
        );

        openScreenKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.autotextnrc.open",
                InputConstants.Type.KEYSYM,
                InputConstants.Type.KEYSYM.getOrCreate(93),
                category
        ));

        ClientTickEvents.END_CLIENT_TICK.register(AutoTextClient::onClientTick);
    }

    private static void onClientTick(Minecraft client) {
        while (openScreenKey.consumeClick()) {
            client.setScreen(new AutoTextScreen());
        }

        if (!enabled || client.player == null || client.getConnection() == null) {
            ticks = 0;
            return;
        }

        ticks++;
        if (ticks >= SEND_INTERVAL_TICKS) {
            String outgoing = message.trim();
            if (!outgoing.isEmpty()) {
                client.getConnection().sendChat(outgoing);
            }
            ticks = 0;
        }
    }

    static String getMessage() {
        return message;
    }

    static void setMessage(String value) {
        message = value == null ? "" : value.trim();
    }

    static boolean isEnabled() {
        return enabled;
    }

    static void toggleEnabled() {
        enabled = !enabled;
        ticks = 0;
    }

    static Component getStatusText() {
        return Component.literal(enabled ? "Status: AN" : "Status: AUS");
    }
}
