package com.fggang;

import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;

import static com.fggang.datasave.items.ItemList.itemRead;

public final class WFHelp extends JavaPlugin {
    public static final WFHelp INSTANCE = new WFHelp();

    private WFHelp() {
        super(new JvmPluginDescriptionBuilder("com.fggang.WFHelp", "2.0.0")
                .name("WFHelp")
                .author("G_ang")
                .build());
    }

    @Override
    public void onEnable() {
        getLogger().info("WFHelp V2.0 载入成功!");
        GlobalEventChannel.INSTANCE.registerListenerHost(new Main());
        itemRead();
    }
}