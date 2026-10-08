package hypernebulae.tinb;

import java.math.BigInteger;
import java.util.Locale;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public final class Commands {
    private Commands() {}

    public static boolean execute(String input) {
        Minecraft minecraft = Minecraft.getInstance();
        String[] args = input.trim().split("\\s+");

        if (args.length == 0 || args[0].isBlank()) {
            return false;
        }

        String command = args[0].toLowerCase(Locale.ROOT);

        switch (command) {
            case "farlands" -> {
                if (args.length != 2) return usage(minecraft);

                switch (args[1].toLowerCase(Locale.ROOT)) {
                    case "true", "enable", "on" -> Main.farlands = true;
                    case "false", "disable", "off" -> Main.farlands = false;
                    default -> {
                        return feedback(minecraft,
                                "Usage: tinb farlands <true|false|enable|disable|on|off>");
                    }
                }

                return feedback(minecraft, "Farlands: " + Main.farlands);
            }

            case "precision" -> {
                if (args.length != 2) return usage(minecraft);

                try {
                    BigInteger entered = new BigInteger(args[1]);
                    if (entered.signum() < 0) {
                        return feedback(minecraft, "Precision must be 0 or greater.");
                    }

                    int precision = entered.min(BigInteger.valueOf(100)).intValue();
                    Main.precision = precision == 0
                            ? "0"
                            : "0." + "#".repeat(precision);

                    return feedback(minecraft, "Precision set to " + precision + ".");
                } catch (NumberFormatException ignored) {
                    return feedback(minecraft, "Usage: tinb precision <0-100>");
                }
            }

            case "wbmax" -> {
                if (args.length != 1) {
                    return feedback(minecraft, "Usage: tinb wbmax");
                }

                if (minecraft.level == null) {
                    return feedback(minecraft, "You need to be in a world.");
                }

                minecraft.level.getWorldBorder().setSize(Math.pow(2, 32));
                return feedback(minecraft, "World border set to maximum size.");
            }

            case "help" -> {
                return feedback(minecraft,
                        "Commands: tinb farlands <true|false|enable|disable|on|off> | tinb precision <0-100> | tinb wbmax");
            }

            default -> {
                return false;
            }
        }
    }

    public static boolean handleInput(String input) {
        String trimmed = input.trim();

        if (trimmed.equalsIgnoreCase("tinb")) {
            return execute("help");
        }

        if (trimmed.regionMatches(true, 0, "tinb ", 0, 5)) {
            return execute(trimmed.substring(5));
        }

        return false;
    }

    private static boolean usage(Minecraft minecraft) {
        return feedback(minecraft,
                "Usage: tinb farlands <true|false|enable|disable|on|off> | tinb precision <0-100> | tinb border max");
    }

    private static boolean feedback(Minecraft minecraft, String message) {
        if (minecraft.player == null) {
            System.out.println("[TINB] " + message);
            return true;
        }

        Component text = Component.literal("[TINB] " + message);

        for (Class<?> type = minecraft.player.getClass();
             type != null;
             type = type.getSuperclass()) {
            for (java.lang.reflect.Method method : type.getDeclaredMethods()) {
                Class<?>[] params = method.getParameterTypes();

                if (params.length == 2
                        && params[0] == Component.class
                        && params[1] == boolean.class
                        && method.getReturnType() == void.class) {
                    try {
                        method.setAccessible(true);
                        method.invoke(minecraft.player, text, false);
                        return true;
                    } catch (ReflectiveOperationException | RuntimeException e) {
                        // Try another matching method, if present.
                    }
                }
            }
        }

        System.out.println("[TINB] " + message);
        return true;
    }
}