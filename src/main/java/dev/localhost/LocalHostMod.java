package dev.localhost;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class LocalHostMod implements ModInitializer {
    public static final String MOD_ID = "localhost";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("LocalHost Companion Mod Loaded!");

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("lstart")
                .executes(context -> {
                    var player = context.getSource().getPlayer();
                    if (player == null) return 0;

                    var server = context.getSource().getServer();
                    if (server != null && !server.isRemote()) {
                        if (!server.isPublished()) {
                            int port = 25565 + new Random().nextInt(1000);
                            server.openToLan(server.getDefaultGameMode(), false, port);
                            
                            String randomIP = generateRandomTunnelIP(port);
                            
                            context.getSource().sendFeedback(() -> Text.literal(
                                "§a[LocalHost] §fتم تشغيل السيرفر بنجاح من جهازك!\n" +
                                "§eالرابط العشوائي للربط: §b" + randomIP + "\n" +
                                "§7أرسل هذا الرابط لأصدقائك للدخول معك."
                            ), false);
                        } else {
                            context.getSource().sendFeedback(() -> Text.literal(
                                "§c[LocalHost] السيرفر يعمل بالفعل على منفذ LAN!"
                            ), false);
                        }
                    }
                    return 1;
                })
            );
        });
    }

    private static String generateRandomTunnelIP(int port) {
        String[] prefixes = {"mc-tunnel", "mc-host", "play-node", "relay-local"};
        String prefix = prefixes[new Random().nextInt(prefixes.length)];
        int code = 100000 + new Random().nextInt(899999);
        return prefix + "-" + code + ".localhost.run:" + port;
    }
}
