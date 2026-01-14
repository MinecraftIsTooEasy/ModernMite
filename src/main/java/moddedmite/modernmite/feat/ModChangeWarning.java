package moddedmite.modernmite.feat;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.util.JsonUtils;
import moddedmite.modernmite.config.ModernMiteConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.api.metadata.ModEnvironment;
import net.fabricmc.loader.impl.ModContainerImpl;
import net.minecraft.Minecraft;
import net.minecraft.SaveFormatComparator;
import net.xiaoyu233.fml.FishModLoader;
import net.xiaoyu233.fml.util.RemoteModInfo;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModChangeWarning {
    public static final String FILE_NAME = "last_mods.json";
    public static final String FIELD_NAME = "mods";

    private static boolean ACTIVE = true;

    public static boolean isActive() {
        if (!ModernMiteConfig.ModChangeWarning.getBooleanValue()) return false;
        return ACTIVE;
    }

    public static void setActive(boolean active) {
        ModChangeWarning.ACTIVE = active;
    }

    public static ModChangeReport generateReport(Minecraft client, SaveFormatComparator saveInfo) {
        Path path = client.saves_dir_MITE.toPath().resolve(saveInfo.getFileName()).resolve(FILE_NAME);

        if (Files.notExists(path)) return ModChangeReport.NEW_SAVE;

        List<RemoteModInfo> lastModInfo = readModInfo(path);
        if (lastModInfo == null) return ModChangeReport.INVALID_MOD_LIST;

        Map<String, ModContainerImpl> mutableMap = new HashMap<>(FishModLoader.getModsMap());

        List<ModChangeEntry> entries = new ArrayList<>(lastModInfo.size() / 8);

        for (RemoteModInfo info : lastModInfo) {
            if (!info.canBeUsedAt(EnvType.SERVER)) continue;// client only mods
            String modid = info.getModid();
            if (!mutableMap.containsKey(modid)) {
                entries.add(new ModChangeEntry.Missing(modid));
                continue;
            }

            Version oldVersion = info.getModVer();
            ModContainerImpl modContainer = mutableMap.remove(modid);
            modContainer.getMetadata().getProvides().forEach(mutableMap::remove);// mod alias

            Version newVersion = modContainer.getMetadata().getVersion();
            int compare = newVersion.compareTo(oldVersion);
            if (compare == 0) continue;
            entries.add(new ModChangeEntry.DifferentVersion(
                    modid,
                    oldVersion.getFriendlyString(),
                    newVersion.getFriendlyString()
            ));
        }

        for (ModContainerImpl modContainer : mutableMap.values()) {
            if (modContainer.getMetadata().getEnvironment() == ModEnvironment.CLIENT) continue;
            entries.add(new ModChangeEntry.Extra(modContainer.getMetadata().getId()));
        }

        return new ModChangeReport(entries);
    }

    public static void saveModList(File directory) {
        Path path = directory.toPath().resolve(FILE_NAME);
        JsonObject object = new JsonObject();
        object.add(FIELD_NAME, FishModLoader.getModsJson());
        JsonUtils.writeJsonToFile(object, path.toFile());
    }

    @Nullable
    private static List<RemoteModInfo> readModInfo(Path path) {
        JsonElement jsonElement = JsonUtils.parseJsonFile(path.toFile());
        if (jsonElement == null) return null;
        if (!jsonElement.isJsonObject()) return null;

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        if (!jsonObject.has(FIELD_NAME)) return null;

        JsonElement inner = jsonObject.get(FIELD_NAME);
        if (!inner.isJsonArray()) return null;

        JsonArray jsonArray = inner.getAsJsonArray();
        try {
            return RemoteModInfo.readFromJson(jsonArray);
        } catch (VersionParsingException e) {
            return null;
        }
    }
}
