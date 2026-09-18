package rikka.lanserverproperties;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class IntegerEditBox extends EditBox {
    private boolean contentValid = true;

    public IntegerEditBox(Font font, int x, int y, int width, int height, Component title, int defaultVal,
                          Consumer<IntegerEditBox> onChanged, Predicate<String> validator, Component tooltip) {
        super(font, x, y, width, height, title);
        if (tooltip != null) {
            this.setTooltip(Tooltip.create(tooltip));
        }
        this.setValue(String.valueOf(defaultVal));
        this.setResponder(text -> {
            this.contentValid = validator.test(text);
            this.setTextColor(this.contentValid ? 0xE0E0E0 : 0xFF5555);
            if (onChanged != null) {
                onChanged.accept(this);
            }
        });
        this.contentValid = validator.test(this.getValue());
    }

    public int getValueAsInt(int fallback) {
        try {
            return Integer.parseInt(this.getValue().trim());
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    public boolean isContentValid() {
        return this.contentValid;
    }

    public static Predicate<String> makeValidator(int min, int max) {
        return text -> {
            if (text == null || text.trim().isEmpty()) return false;
            try {
                int val = Integer.parseInt(text.trim());
                return val >= min && val <= max;
            } catch (NumberFormatException e) {
                return false;
            }
        };
    }
}