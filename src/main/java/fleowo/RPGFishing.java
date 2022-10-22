package fleowo;

import fleowo.Commands.FishCmd;
import fleowo.Events.FishEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class RPGFishing extends JavaPlugin {

    private LangStorage languageStorage;

    public void onEnable() {
        getCommand("RPGFishing").setExecutor(new FishCmd(this));
        getServer().getPluginManager().registerEvents(new FishEvent(this), (Plugin)this);
        this.languageStorage = new LangStorage(this);
    }

    public LangStorage getLanguageStorage() {
        return this.languageStorage;
    }
}

