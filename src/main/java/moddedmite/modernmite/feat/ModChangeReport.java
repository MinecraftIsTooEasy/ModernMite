package moddedmite.modernmite.feat;

import java.util.List;

public record ModChangeReport(List<ModChangeEntry> entries) {
    public static final ModChangeReport NEW_SAVE = new ModChangeReport(List.of());
    public static final ModChangeReport INVALID_MOD_LIST = new ModChangeReport(List.of());

    public boolean pass() {
        return this.entries.isEmpty();
    }
}
