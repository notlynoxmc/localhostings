package dev.localhost.mixin;

import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen {

    protected GameMenuScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "initWidgets", at = @At("TAIL"))
    private void addCustomInviteButton(CallbackInfo ci) {
        int buttonWidth = 204;
        int buttonHeight = 20;
        int x = this.width / 2 - buttonWidth / 2;
        int y = this.height / 4 + 128;

        this.addDrawableChild(
            ButtonWidget.builder(
                Text.literal("§b⚡ دعوة الأصدقاء (LocalHost)"),
                button -> {
                    if (this.client != null && this.client.player != null) {
                        this.client.player.networkHandler.sendChatCommand("lstart");
                        this.close();
                    }
                }
            )
            .dimensions(x, y, buttonWidth, buttonHeight)
            .build()
        );
    }
}
