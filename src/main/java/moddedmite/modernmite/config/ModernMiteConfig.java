package moddedmite.modernmite.config;

import fi.dy.masa.malilib.config.ConfigTab;
import fi.dy.masa.malilib.config.SimpleConfigs;
import fi.dy.masa.malilib.config.options.*;

import java.util.ArrayList;
import java.util.List;

import static moddedmite.modernmite.ModernMite.LEGACY_CONFIG_FILE_NAME;

public class ModernMiteConfig extends SimpleConfigs {

    private static final ModernMiteConfig Instance;
    public static final List<ConfigBase<?>> clientTweaks;
    public static final List<ConfigBase<?>> clientFix;
    public static final List<ConfigBase<?>> serverTweaks;
    public static final List<ConfigBase<?>> serverFix;
    public static final List<ConfigBase<?>> values = new ArrayList<>();
    public static final List<ConfigHotkey> hotkeys;

    // client tweaks
    public static final ConfigBoolean IMBlocker = new ConfigBoolean("modernmite.imBlocker", true, "config.comment.modernmite.imBlocker");
    public static final ConfigStringList ForceEnableInputMethodGuiScreens = new ConfigStringList("modernmite.forceEnableInputMethodGuiScreens", List.of("net.minecraft.GuiScreenBook", "net.minecraft.GuiEditSign", "net.minecraft.GuiContainerCreative"), "config.comment.modernmite.forceEnableInputMethodGuiScreens");
    public static final ConfigBoolean SlashIM = new ConfigBoolean("modernmite.slashIM", false, "config.comment.modernmite.slashIM");
    public static final ConfigBoolean NoSpamLog = new ConfigBoolean("modernmite.noSpamLog", true, "config.comment.modernmite.noSpamLog");
    public static final ConfigBoolean NoAttackDump = new ConfigBoolean("modernmite.noAttackDump", false, "config.comment.modernmite.noAttackDump");
    public static final ConfigEnum<EnumSprintingMode> SprintingMode = new ConfigEnum<>("modernmite.sprintingMode", EnumSprintingMode.Toggle, "config.comment.modernmite.sprintingMode");
    public static final ConfigBoolean NoReferenceFile = new ConfigBoolean("modernmite.noReferenceFile", true, "config.comment.modernmite.noReferenceFile");
    public static final ConfigBoolean VanillaChat = new ConfigBoolean("modernmite.vanillaChat", false, "config.comment.modernmite.vanillaChat");
    public static final ConfigBoolean ModChangeWarning = new ConfigBoolean("modernmite.modChangeWarning", true, "config.comment.modernmite.modChangeWarning");
    public static final ConfigBoolean BlockMITEWebRequests = new ConfigBoolean("modernmite.blockMiteWebRequests", true, "config.comment.modernmite.blockMiteWebRequests");


    // client fix
    public static final ConfigBoolean ResourceLocationFix = new ConfigBoolean("modernmite.resourceLocationFix", true, "config.comment.modernmite.resourceLocationFix");
    public static final ConfigBoolean CraftingSpeedFix = new ConfigBoolean("modernmite.craftingSpeedFix", true, "config.comment.modernmite.craftingSpeedFix");
    public static final ConfigBoolean Packet24Fix = new ConfigBoolean("modernmite.packet24Fix", true, "config.comment.modernmite.packet24Fix");
    public static final ConfigBoolean WorldLoadingInputFix = new ConfigBoolean("modernmite.worldLoadingInputFix", true, "config.comment.modernmite.worldLoadingInputFix");


    // hotkey
    public static final ConfigHotkey OpenWindow = new ConfigHotkey("modernmite.openWindow", "H,C", "config.comment.modernmite.openWindow");


    // server tweaks


    // server fix
    public static final ConfigBoolean DevCurseFix = new ConfigBoolean("modernmite.devCurseFix", true, "config.comment.modernmite.devCurseFix");
    public static final ConfigBoolean CraftingKickFix = new ConfigBoolean("modernmite.craftingKickFix", true, "config.comment.modernmite.craftingKickFix");
    public static final ConfigBoolean BoatFallDamageFix = new ConfigBoolean("modernmite.boatFallDamageFix", false, "config.comment.modernmite.boatFallDamageFix");
    public static final ConfigBoolean SpiderNPEFix = new ConfigBoolean("modernmite.spiderNpeFix", true, "config.comment.modernmite.spiderNpeFix");
    public static final ConfigBoolean NetherStarFix = new ConfigBoolean("modernmite.netherStarFix", true, "config.comment.modernmite.netherStarFix");
    public static final ConfigBoolean ExplosionDropFix = new ConfigBoolean("modernmite.explosionDropFix", true, "config.comment.modernmite.explosionDropFix");
    public static final ConfigBoolean DeathMessageFix = new ConfigBoolean("modernmite.deathMessageFix", true, "config.comment.modernmite.deathMessageFix");


    public static final List<ConfigTab> configTabs = new ArrayList<>();

    public ModernMiteConfig(String name, List<ConfigHotkey> hotkeys, List<ConfigBase<?>> values) {
        super(name, hotkeys, values, "config.menu.comment." + name);
    }

    static {
        clientTweaks = List.of(
                IMBlocker,
                ForceEnableInputMethodGuiScreens,
                SlashIM,
                NoSpamLog,
                NoAttackDump,
                SprintingMode,
                NoReferenceFile,
                VanillaChat,
                ModChangeWarning,
                BlockMITEWebRequests
        );
        clientFix = List.of(ResourceLocationFix, CraftingSpeedFix, Packet24Fix, WorldLoadingInputFix);
        serverTweaks = List.of();
        serverFix = List.of(DevCurseFix, CraftingKickFix, BoatFallDamageFix, SpiderNPEFix, NetherStarFix, ExplosionDropFix, DeathMessageFix);
        values.addAll(clientTweaks);
        values.addAll(clientFix);
        values.addAll(serverTweaks);
        values.addAll(serverFix);
        hotkeys = List.of(OpenWindow);
        configTabs.add(new ConfigTab("client_tweaks", clientTweaks));
        configTabs.add(new ConfigTab("client_fixes", clientFix));
        configTabs.add(new ConfigTab("hotkeys", hotkeys));
//        configTabs.add(new ConfigTab("服务端功能", serverTweaks));// empty at present
        configTabs.add(new ConfigTab("server_fixes", serverFix));
        Instance = new ModernMiteConfig(LEGACY_CONFIG_FILE_NAME, hotkeys, values);
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
