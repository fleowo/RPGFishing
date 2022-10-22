package fleowo;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class LangStorage {
    private String[] caughtTitle;

    private String[] failTitle;

    private RPGFishing m;

    public String[] getCaughtTitle() {
        return this.caughtTitle;
    }

    public String[] getFailTitle() {
        return this.failTitle;
    }

    public LangStorage(RPGFishing m) {
        this.m = m;
        m.getConfig().options().copyDefaults(true);
        m.saveConfig();
        loadConfig();
    }

    public void loadConfig() {
        this.m.reloadConfig();
        FileConfiguration config = this.m.getConfig();
        ConfigurationSection lang = config.getConfigurationSection("lang");
        this.caughtTitle = lang.getString("caught").split("_");
        this.failTitle = lang.getString("lost").split("_");
    }
}

