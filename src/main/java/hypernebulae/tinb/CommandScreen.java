package hypernebulae.tinb;

import java.math.BigInteger;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class CommandScreen extends Screen {
    private EditBox precisionField;

    public CommandScreen() {
        super(Component.literal("there is no border menu"));
    }

    @Override
    protected void init() {
        int x = this.width / 2 - 100;
        int y = this.height / 2 - 65;

        this.addRenderableWidget(new StringWidget(
                this.width / 2 - 60,
                y - 22,
                this.width,
                18,
                Component.literal("there is no border menu"),
                this.font
        ));

        this.addRenderableWidget(Button.builder(
                Component.literal("Toggle Farlands: " + Main.farlands),
                button -> {
                    Main.farlands = !Main.farlands;
                    button.setMessage(Component.literal(
                            "Toggle Farlands: " + Main.farlands
                    ));
                }
        ).bounds(x, y, 200, 20).build());

        precisionField = new EditBox(
                this.font,
                x,
                y + 25,
                200,
                20,
                Component.literal("Precision field")
        );
        precisionField.setValue(Integer.toString(getCurrentPrecision()));
        this.addRenderableWidget(precisionField);

        this.addRenderableWidget(Button.builder(
                Component.literal("Apply precision"),
                button -> applyPrecision()
        ).bounds(x, y + 50, 200, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("Set border to max size"),
                button -> setHugeWorldBorder()
        ).bounds(x, y + 75, 200, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("Done"),
                button -> this.onClose()
        ).bounds(x, y + 100, 200, 20).build());
    }

    private int getCurrentPrecision() {
        // For formats such as "0.######", count the # characters after the dot.
        String precision = Main.precision;
        int decimalPoint = precision.indexOf('.');

        if (decimalPoint < 0) {
            return 0;
        }

        int digits = 0;
        for (int i = decimalPoint + 1; i < precision.length(); i++) {
            if (precision.charAt(i) == '#') {
                digits++;
            }
        }

        return Math.min(digits, 100);
    }

    private void applyPrecision() {
        String value = precisionField.getValue();

        try {
            BigInteger entered = new BigInteger(value);
            if (entered.signum() < 0) {
                return;
            }

            int precision = entered.min(BigInteger.valueOf(100)).intValue();

            // Reflect the clamped value in the field too.
            precisionField.setValue(Integer.toString(precision));

            Main.precision = precision == 0
                    ? "0"
                    : "0." + "#".repeat(precision);
        } catch (NumberFormatException ignored) {
            // Blank or non-numeric input: leave the current precision unchanged.
        }
    }

    private void setHugeWorldBorder() {
        if (this.minecraft.level != null) {
            this.minecraft.level.getWorldBorder().setSize(Math.pow(2, 32));
        }
    }
}