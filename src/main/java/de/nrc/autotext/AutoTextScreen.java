package de.nrc.autotext;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class AutoTextScreen extends Screen {
    private EditBox messageField;
    private Button toggleButton;

    public AutoTextScreen() {
        super(Component.literal("AutoText NRC"));
    }

    @Override
    protected void init() {
        int center = this.width / 2;

        messageField = new EditBox(this.font, center - 150, 76, 300, 22,
                Component.literal("Nachricht"));
        messageField.setMaxLength(256);
        messageField.setValue(AutoTextClient.getMessage());
        addRenderableWidget(messageField);

        addRenderableWidget(Button.builder(Component.literal("Text speichern"), button -> {
            AutoTextClient.setMessage(messageField.getValue());
        }).bounds(center - 150, 112, 145, 20).build());

        toggleButton = Button.builder(toggleLabel(), button -> {
            AutoTextClient.setMessage(messageField.getValue());
            AutoTextClient.toggleEnabled();
            button.setMessage(toggleLabel());
        }).bounds(center + 5, 112, 145, 20).build();
        addRenderableWidget(toggleButton);

        addRenderableWidget(Button.builder(Component.literal("Schließen"), button -> onClose())
                .bounds(center - 50, 148, 100, 20).build());
    }

    private static Component toggleLabel() {
        return Component.literal(AutoTextClient.isEnabled() ? "Automatik stoppen" : "Automatik starten");
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        graphics.fill(0, 0, this.width, this.height, 0xC0101010);
        graphics.drawCenteredString(this.font, "AutoText NRC – alle 15 Sekunden",
                this.width / 2, 42, 0xFFFFFF);
        graphics.drawCenteredString(this.font, AutoTextClient.getStatusText(),
                this.width / 2, 180, 0xFFFFFF);
        super.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
