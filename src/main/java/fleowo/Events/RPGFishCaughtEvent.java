package fleowo.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RPGFishCaughtEvent extends Event {
    private Player player;

    public Player getPlayer() {
        return this.player;
    }

    private boolean replace = false;

    public boolean isReplace() {
        return this.replace;
    }

    public RPGFishCaughtEvent(Player p) {
        this.player = p;
    }

    public RPGFishCaughtEvent(Player p, boolean replace) {
        this.player = p;
        this.replace = replace; 
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private static final HandlerList handlers = new HandlerList();
}
