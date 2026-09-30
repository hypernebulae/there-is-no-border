package hypernebulae.tinb;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

public class Commands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> registerCommands(dispatcher));
    }

    private static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(net.minecraft.commands.Commands.literal("farlands")
                .then(
                        net.minecraft.commands.Commands.literal("enable")
                                .executes(context -> {
                                    Main.farlands = true;
                                    context.getSource().sendSuccess(() -> Component.literal("Farlands enabled"), false);
                                    return 1;
                                })
                )
                .then(
                        net.minecraft.commands.Commands.literal("disable")
                                .executes(context -> {
                                    Main.farlands = false;
                                    context.getSource().sendSuccess(() -> Component.literal("Farlands disabled"), false);
                                    return 1;
                                })
                )
        );
        dispatcher.register(
                net.minecraft.commands.Commands.literal("precision")
                        .then(net.minecraft.commands.Commands.argument("decimalPlaces", IntegerArgumentType.integer(0, 100))
                                .executes(context -> {
                                    int decimalPlaces = IntegerArgumentType.getInteger(context, "decimalPlaces");
                                    Main.precision = decimalPlaces == 0 ? "0" : "0." + "#".repeat(decimalPlaces);
                                    String digitText = decimalPlaces == 1 ? "digit" : "digits";
                                    context.getSource().sendSuccess(() -> Component.literal("Precision set to " + decimalPlaces + " " + digitText), true);
                                    return 1;
                                })
                        )
        );
        dispatcher.register(
                net.minecraft.commands.Commands.literal("wbmax")
                        .executes(context -> {
                            CommandSourceStack source = context.getSource();
                            source.getServer().getCommands().performPrefixedCommand(
                                    source,
                                    "worldborder set 4294967296"
                            );
                            return 1;
                        })
        );
    }

    public static void execute(ServerLevel world) {
        MinecraftServer server = world.getServer();
        server.getCommands().performPrefixedCommand(server.createCommandSourceStack().withLevel(world).withSuppressedOutput(), "wbmax");
    }
}
