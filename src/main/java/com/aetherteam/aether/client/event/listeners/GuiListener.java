package com.aetherteam.aether.client.event.listeners;

import com.aetherteam.aether.client.AetherClient;
import com.aetherteam.aether.client.event.hooks.GuiHooks;
import com.aetherteam.aether.client.gui.component.inventory.AccessoryButton;
import com.aetherteam.aether.client.gui.screen.inventory.AetherAccessoriesScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.Tuple;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

import java.util.UUID;

public class GuiListener {
    /**
     * @see AetherClient#eventSetup(IEventBus)
     */
    public static void listen(IEventBus bus) {
        bus.addListener(GuiListener::onGuiInitialize);
        bus.addListener(GuiListener::onGuiDraw);
        bus.addListener(GuiListener::onClientTick);
        bus.addListener(GuiListener::onKeyPress);
        bus.addListener(GuiListener::onRenderBossBar);
    }

    /**
     * @see AetherAccessoriesScreen#getButtonOffset(Screen)
     * @see GuiHooks#setupAccessoryButton(Screen, Tuple)
     * @see GuiHooks#setupPerksButtons(Screen)
     */
    public static void onGuiInitialize(ScreenEvent.Init.Post event) {
        Screen screen = event.getScreen();

        Tuple<Integer, Integer> offsets = AetherAccessoriesScreen.getButtonOffset(screen);
        AccessoryButton inventoryAccessoryButton = GuiHooks.setupAccessoryButton(screen, offsets);
        if (inventoryAccessoryButton != null) {
            if (GuiHooks.isAccessoryButtonEnabled()) {
                event.addListener(inventoryAccessoryButton);
            }
        }

        GridLayout layout = GuiHooks.setupPerksButtons(screen);
        if (layout != null) {
            if (!GuiHooks.isAccessoryButtonEnabled()) {
                layout.visitWidgets(event::addListener);
            }
        }
    }

    /**
     * @see GuiHooks#drawTrivia(Screen, GuiGraphics)
     * @see GuiHooks#drawAetherTravelMessage(Screen, GuiGraphics)
     */
    public static void onGuiDraw(ScreenEvent.Render.Post event) {
        Screen screen = event.getScreen();
        GuiGraphics guiGraphics = event.getGuiGraphics();
        if (!ModList.get().isLoaded("tipsmod")) {
            GuiHooks.drawTrivia(screen, guiGraphics);
        }
        GuiHooks.drawAetherTravelMessage(screen, guiGraphics);
    }

    /**
     * @see GuiHooks#handlePatreonRefreshRebound()
     */
    public static void onClientTick(ClientTickEvent.Post event) {
        GuiHooks.handlePatreonRefreshRebound();
    }

    /**
     * @see GuiHooks#openAccessoryMenu()
     * @see GuiHooks#closeContainerMenu(int, int)
     */
    public static void onKeyPress(InputEvent.Key event) {
        GuiHooks.openAccessoryMenu();
        GuiHooks.closeContainerMenu(event.getKey(), event.getAction());
    }

    /**
     * This event is cancelled in BossHealthOverlayMixin. See it for more info.
     *
     * @see com.aetherteam.aether.mixin.mixins.client.BossHealthOverlayMixin#event(CustomizeGuiOverlayEvent.BossEventProgress)
     * @see GuiHooks#drawBossHealthBar(GuiGraphics, int, int, LerpingBossEvent)
     */
    public static void onRenderBossBar(CustomizeGuiOverlayEvent.BossEventProgress event) {
        GuiGraphics guiGraphics = event.getGuiGraphics();
        LerpingBossEvent bossEvent = event.getBossEvent();
        UUID bossUUID = bossEvent.getId();
        if (GuiHooks.isAetherBossBar(bossUUID)) {
            GuiHooks.drawBossHealthBar(guiGraphics, event.getX(), event.getY(), bossEvent);
            event.setIncrement(event.getIncrement() + 13);
        }
    }
}
