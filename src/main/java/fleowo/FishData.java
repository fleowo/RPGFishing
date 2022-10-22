package fleowo;

import java.util.Random;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FishData {
    private Player p;

    public Player getP() {
        return this.p;
    }

    private ItemStack item = null;

    public ItemStack getItem() {
        return this.item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    private FishHook hook = null;

    public FishHook getHook() {
        return this.hook;
    }

    private int expire = 0;

    public int getExpire() {
        return this.expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    private int expirelimit = 22;

    public int getExpirelimit() {
        return this.expirelimit;
    }

    public void setExpirelimit(int expirelimit) {
        this.expirelimit = expirelimit;
    }

    private boolean delay = true;

    public boolean isDelay() {
        return this.delay;
    }

    public void setDelay(boolean delay) {
        this.delay = delay;
    }

    private int progressTarget = 3;

    public int getProgressTarget() {
        return this.progressTarget;
    }

    private int progressCurrent = 0;

    private Entity caught;

    private int barCurrent;

    private int barTarget;

    private int barLimit;

    public void setProgressCurrent(int progressCurrent) {
        this.progressCurrent = progressCurrent;
    }

    public int getProgressCurrent() {
        return this.progressCurrent;
    }

    public Entity getCaught() {
        return this.caught;
    }

    public FishData(Player p, Entity caught, FishHook hook) {
        this.barCurrent = 0;
        this.barTarget = 2;
        this.barLimit = 4;
        this.hook = hook;
        this.caught = caught;
        this.p = p;
        Random rand = new Random();
        this.progressTarget = rand.nextInt(6);
        if (this.progressTarget <= 1)
            this.progressTarget = 2;
        this.expirelimit = this.progressTarget * 10;
    }

    public void setBarCurrent(int barCurrent) {
        this.barCurrent = barCurrent;
    }

    public int getBarCurrent() {
        return this.barCurrent;
    }

    public int getBarTarget() {
        return this.barTarget;
    }

    public int getBarLimit() {
        return this.barLimit;
    }
}
