package moddedmite.modernmite.feat;

public abstract class ModChangeEntry {
    private final String modId;
    private final ModChangeType type;

    protected ModChangeEntry(String modId, ModChangeType type) {
        this.modId = modId;
        this.type = type;
    }

    public String getModId() {
        return this.modId;
    }

    public ModChangeType getType() {
        return this.type;
    }

    public abstract String getTip();

    static class Missing extends ModChangeEntry {

        protected Missing(String modId) {
            super(modId, ModChangeType.MISSING);
        }

        @Override
        public String getTip() {
            return "缺失";
        }
    }

    static class Extra extends ModChangeEntry {

        protected Extra(String modId) {
            super(modId, ModChangeType.EXTRA);
        }

        @Override
        public String getTip() {
            return "额外";
        }
    }

    static class DifferentVersion extends ModChangeEntry {
        private final String oldVersion;
        private final String newVersion;

        public DifferentVersion(String modid, String oldVersion, String newVersion) {
            super(modid, ModChangeType.DIFFERENT_VERSION);
            this.oldVersion = oldVersion;
            this.newVersion = newVersion;
        }

        public String getOldVersion() {
            return oldVersion;
        }

        public String getNewVersion() {
            return newVersion;
        }

        @Override
        public String getTip() {
            return String.format("之前是%s而现在是%s", this.oldVersion, this.newVersion);
        }
    }
}
