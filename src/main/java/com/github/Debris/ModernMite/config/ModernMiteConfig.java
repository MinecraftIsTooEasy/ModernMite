package com.github.Debris.ModernMite.config;

import fi.dy.masa.malilib.config.ConfigTab;
import fi.dy.masa.malilib.config.SimpleConfigs;
import fi.dy.masa.malilib.config.options.*;

import java.util.ArrayList;
import java.util.List;

import static com.github.Debris.ModernMite.ModernMite.MOD_ID;

public class ModernMiteConfig extends SimpleConfigs {
    private static final ModernMiteConfig Instance;
    public static final List<ConfigBase<?>> clientTweaks;
    public static final List<ConfigBase<?>> clientFix;
    public static final List<ConfigBase<?>> serverTweaks;
    public static final List<ConfigBase<?>> serverFix;
    public static final List<ConfigBase<?>> values = new ArrayList<>();
    public static final List<ConfigHotkey> hotkeys;

    // client tweaks
    public static final ConfigBoolean ASCIIFont = new ConfigBoolean("ASCIIFont", true);
    public static final ConfigBoolean IMBlocker = new ConfigBoolean("IMBlocker", true);
    public static final ConfigStringList ForceEnableInputMethodGuiScreens = new ConfigStringList("强制启用输入法的屏幕的类路径", List.of("net.minecraft.GuiScreenBook", "net.minecraft.GuiEditSign", "net.minecraft.GuiContainerCreative"), null);
    public static final ConfigBoolean SlashIM = new ConfigBoolean("SlashIM");
    public static final ConfigBoolean NoSpamLog = new ConfigBoolean("NoSpamLog", true);
    public static final ConfigBoolean NoAttackDump = new ConfigBoolean("NoAttackDump", false, "创造模式按下Ctrl攻击时, 不再打开弹窗");
    public static final ConfigEnum<EnumSprintingMode> SprintingMode = new ConfigEnum<>("SprintingMode", EnumSprintingMode.Toggle);
    public static final ConfigBoolean NoReferenceFile = new ConfigBoolean("NoReferenceFile", true, "即MITE/reference目录的文件, 这会稍微提升游戏启动速度");

    // client fix
    public static final ConfigBoolean ResourceLocationFix = new ConfigBoolean("资源定位修复", true, "空指针问题");
    public static final ConfigBoolean CraftingSpeedFix = new ConfigBoolean("合成速度修复", true);

    // hotkey

    public static final ConfigHotkey OpenWindow = new ConfigHotkey("打开配置屏幕", "H,C", null);

    // server
    public static final ConfigBoolean BetterContainerQuitting = new ConfigBoolean("更好的关闭容器", true, "玩家离开容器时, 物品会先尝试回到背包而不是丢出");

    // server fix
    public static final ConfigBoolean DevCurseFix = new ConfigBoolean("Dev诅咒修复", true);
    public static final ConfigBoolean CraftingKickFix = new ConfigBoolean("合成踢出修复", true);


    public static final List<ConfigTab> configTabs = new ArrayList<>();

    public ModernMiteConfig(String name, List<ConfigHotkey> hotkeys, List<ConfigBase<?>> values) {
        super(name, hotkeys, values);
    }

    static {
        clientTweaks = List.of(ASCIIFont, IMBlocker, ForceEnableInputMethodGuiScreens, SlashIM, NoSpamLog, NoAttackDump, SprintingMode, NoReferenceFile);
        clientFix = List.of(ResourceLocationFix, CraftingSpeedFix);
        serverTweaks = List.of(BetterContainerQuitting);
        serverFix = List.of(DevCurseFix, CraftingKickFix);
        values.addAll(clientTweaks);
//        values.addAll(clientFix);
        values.addAll(serverTweaks);
//        values.addAll(serverFix);
        hotkeys = List.of(OpenWindow);
        configTabs.add(new ConfigTab("客户端功能", clientTweaks));
        configTabs.add(new ConfigTab("客户端修复", clientFix));
        configTabs.add(new ConfigTab("热键", hotkeys));
        configTabs.add(new ConfigTab("服务端功能", serverTweaks));
        configTabs.add(new ConfigTab("服务端修复", serverFix));
        Instance = new ModernMiteConfig(MOD_ID, hotkeys, values);
        Instance.load();
    }

    @Override
    public List<ConfigTab> getConfigTabs() {
        return configTabs;
    }

    public static ModernMiteConfig getInstance() {
        return Instance;
    }
}
