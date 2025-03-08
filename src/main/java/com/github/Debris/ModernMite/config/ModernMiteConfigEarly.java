package com.github.Debris.ModernMite.config;

import com.github.Debris.ModernMite.ModernMite;
import net.xiaoyu233.fml.config.ConfigEntry;
import net.xiaoyu233.fml.config.ConfigRegistry;
import net.xiaoyu233.fml.config.ConfigRoot;
import net.xiaoyu233.fml.util.FieldReference;

import java.io.File;

public class ModernMiteConfigEarly {
    public static final FieldReference<Boolean> ReadStringFix = new FieldReference<>(true);
    public static final FieldReference<Boolean> AchievementFix = new FieldReference<>(true);

    public static final ConfigRoot ROOT = ConfigRoot.create(1)
            .addEntry(ConfigEntry.of("是否启用字符串长度修复", ReadStringFix).withComment("允许客户端接收较长的字符串"))
            .addEntry(ConfigEntry.of("是否启用成就读写修复", AchievementFix).withComment("服务器增加模组又删除时,多余nbt导致的崩溃"));

    public static final File CONFIG_FILE = new File(ModernMite.MOD_ID + "_early.json");

    public static final ConfigRegistry ConfigInstance = new ConfigRegistry(ROOT, CONFIG_FILE);

    static {
        ConfigInstance.reloadConfig();
    }

}
