package hypernebulae.tinb;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

public class Commands implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(
                    ClientCommands.literal("farlands")
                            .then(ClientCommands.literal("enable")
                                    .executes(context -> {
                                        hypernebulae.tinb.Main.farlands = true;
                                        context.getSource().sendFeedback(
                                                Component.literal("Farlands enabled")
                                        );
                                        return 1;
                                    }))
                            .then(ClientCommands.literal("disable")
                                    .executes(context -> {
                                        hypernebulae.tinb.Main.farlands = false;
                                        context.getSource().sendFeedback(
                                                Component.literal("Farlands disabled")
                                        );
                                        return 1;
                                    }))
            );

            dispatcher.register(
                    ClientCommands.literal("precision")
                            .then(ClientCommands.argument(
                                    "decimalPlaces",
                                    IntegerArgumentType.integer(0, 100)
                            ).executes(context -> {
                                int decimalPlaces =
                                        IntegerArgumentType.getInteger(
                                                context,
                                                "decimalPlaces"
                                        );

                                hypernebulae.tinb.Main.precision =
                                        decimalPlaces == 0
                                                ? "0"
                                                : "0." + "#".repeat(decimalPlaces);

                                context.getSource().sendFeedback(
                                        Component.literal(
                                                "Precision set to " + decimalPlaces
                                        )
                                );

                                return 1;
                            }))
            );
            dispatcher.register(
                    ClientCommands.literal("wbmax")
                            .executes(context -> {
                                context.getSource().getClient().player.connection.sendCommand("worldborder set 4294967296");
                                return 1;
                            })
            );
        });
    }

    public static void execute(ServerLevel world) {
        MinecraftServer server = world.getServer();
        for (ServerLevel level : server.getAllLevels()) {
            level.getWorldBorder().setSize(4294967296.0);
        }
    }
}
