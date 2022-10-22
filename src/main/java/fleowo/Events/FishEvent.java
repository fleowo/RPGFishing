package fleowo.Events;

import fleowo.FishData;
import fleowo.RPGFishing;
import fleowo.Utils.*;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class FishEvent implements Listener{
    private final RPGFishing m;

    private Sound complete;

    private final HashMap<Player, FishData> data;

    public FishEvent(RPGFishing m) {
        this.complete = null;
        this.data = new HashMap<>();
        this.m = m;
        try {
            this.complete = Sound.valueOf("UI_TOAST_CHALLENGE_COMPLETE");
        } catch (IllegalArgumentException illegalArgumentException) {
        }
        (new BukkitRunnable() {
            public void run() {
                for (FishData dat : FishEvent.this.data.values()) {
                    if (!dat.getP().getInventory().getItemInMainHand().isSimilar(dat.getItem())) {
                        FishEvent.this.data.remove(dat.getP());
                        continue;
                    }
                    if (!dat.getHook().isValid() || dat.getHook().isDead()) {
                        FishEvent.this.data.remove(dat.getP());
                        continue;
                    }
                    dat.setDelay(false);
                    dat.setExpire(dat.getExpire() + 1);
                    if (dat.getExpire() >= dat.getExpirelimit()) {
                        FishEvent.this.data.remove(dat.getP());
                        TitleAPI.sendTitle(dat.getP(), 10, 30, 20, "&c&lAww..", "&7You lost your fish");
                        continue;
                    }
                    if (dat.getBarCurrent() >= dat.getBarLimit()) {
                        dat.setBarCurrent(0);
                    } else {
                        dat.setBarCurrent(dat.getBarCurrent() + 1);
                    }
                    FishEvent.this.playSound(dat);
                    ActionBarAPI.sendActionBar(dat.getP(), TransColorAPI.TransColor(FishEvent.this.getProgress(dat)));
                    TitleAPI.sendTitle(dat.getP(), 0, 20, 10, "", FishEvent.this.getBar(dat));
                }
            }
        }).runTaskTimerAsynchronously((Plugin)m, 12L, 12L);
    }

    private String getProgress(FishData data) {
        int progress = data.getProgressCurrent();
        int target = data.getProgressTarget();
        StringBuilder build = new StringBuilder();
        for (int i = 0; i < target; i++) {
            if (i < progress) {
                build.append("&a✔");
            } else {
                build.append("&f✔");
            }
        }
        return build.toString();
    }

    private String getFailProgress(FishData data) {
        int target = data.getProgressTarget();
        StringBuilder build = new StringBuilder();
        for (int i = 0; i < target; i++)
            build.append("&c&l✖");
        return build.toString();
    }

    private String getDoneProgress(FishData data) {
        int progress = data.getProgressCurrent();
        int target = data.getProgressTarget();
        StringBuilder build = new StringBuilder();
        for (int i = 0; i < target; i++) {
            if (i < progress) {
                build.append("&2&l✔");
            } else {
                build.append("&f✔");
            }
        }
        return build.toString();
    }

    private void playSound(FishData data) {
        int current = data.getBarCurrent();
        int target = data.getBarTarget();
        boolean done = (target == current);
        Sound s = null;
        if (!done) {
            s = SoundAPI.NOTE_BASS.bukkitSound();
        } else {
            s = SoundAPI.NOTE_PLING.bukkitSound();
        }
        if (done) {
            PlaySoundEvent.PlaySound(data.getP(), s, 1.0F, 2.0F);
            return;
        }
        PlaySoundEvent.PlaySound(data.getP(), s, 0.5F, 0.6F);
    }

    private String getBar(FishData data) {
        int limit = data.getBarLimit();
        int current = data.getBarCurrent();
        int target = data.getBarTarget();
        StringBuilder build = new StringBuilder();
        for (int i = 0; i <= limit; i++) {
            if (i == target && i == current) {
                build.append("&a&l■");
            } else if (i == target) {
                build.append("&e■");
            } else if (i == current) {
                build.append("&6■");
            } else {
                build.append("&7■");
            }
        }
        return build.toString();
    }

    @EventHandler
    public void onRightclick(PlayerInteractEvent e) {

        if (!this.data.containsKey(e.getPlayer()))
            return;
        if (e.getItem() == null)
            return;
        if (e.getItem().getType() != Material.FISHING_ROD)
            return;
        Player player = e.getPlayer();
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;
        e.setCancelled(true);
        FishData dat = this.data.get(e.getPlayer());
        if (dat.isDelay())
            return;
        dat.setDelay(true);
        if (dat.getBarCurrent() != dat.getBarTarget()) {
            if (setBar(dat, e.getPlayer(), e)) return;
            PlaySoundEvent.PlaySound(e.getPlayer(), SoundAPI.BAT_TAKEOFF.bukkitSound(), 1.0F, 0.2F);
        } else {
            dat.setBarCurrent(dat.getBarCurrent() + 1);
            TitleAPI.sendTitle(dat.getP(), 0, 20, 10, "", getBar(dat));
            dat.setProgressCurrent(dat.getProgressCurrent() + 1);
            if (dat.getProgressCurrent() >= dat.getProgressTarget()) {
                RPGFishCaughtEvent ev = new RPGFishCaughtEvent(dat.getP());
                Bukkit.getServer().getPluginManager().callEvent((Event)ev);
                dat.getHook().remove();
                if (!ev.isReplace()) {
                    Item item = (Item)dat.getCaught();
                    e.getPlayer().getInventory().addItem(item.getItemStack());
                }
                this.data.remove(e.getPlayer());
                if (this.complete != null)
                    PlaySoundEvent.PlaySound(e.getPlayer(), this.complete, 0.3F, 1.2F);
                TitleAPI.sendTitle(dat.getP(), 10, 30, 20, this.m.getLanguageStorage().getCaughtTitle()[0], this.m.getLanguageStorage().getCaughtTitle()[1]);
                PlaySoundEvent.PlaySound(e.getPlayer(), SoundAPI.FIREWORK_LAUNCH.bukkitSound(), 1.0F, 2.0F);
                ActionBarAPI.sendActionBar(dat.getP(), TransColorAPI.TransColor(getDoneProgress(dat)));
                return;
            }
            PlaySoundEvent.PlaySound(e.getPlayer(), SoundAPI.NOTE_PIANO.bukkitSound(), 1.0F, 2.0F);
        }
    }

    private boolean setBar(FishData dat, Player player2, PlayerInteractEvent e) {
        dat.setBarCurrent(dat.getBarCurrent() + 1);
        TitleAPI.sendTitle(dat.getP(), 0, 20, 10, "", getBar(dat));
        dat.setProgressCurrent(dat.getProgressCurrent() - 1);
        ActionBarAPI.sendActionBar(dat.getP(), TransColorAPI.TransColor(getProgress(dat)));
        if (dat.getProgressCurrent() < 0) {
            dat.getHook().remove();
            ActionBarAPI.sendActionBar(dat.getP(), TransColorAPI.TransColor(getFailProgress(dat)));
            TitleAPI.sendTitle(dat.getP(), 10, 30, 20, this.m.getLanguageStorage().getFailTitle()[0], this.m.getLanguageStorage().getFailTitle()[1]);
            this.data.remove(player2);

            PlaySoundEvent.PlaySound(player2, SoundAPI.DONKEY_ANGRY.bukkitSound(), 1.0F, 0.4F);
            return true;
        }
        return false;
    }

    @EventHandler
    public void onFish(PlayerFishEvent e) {
        if (this.data.containsKey(e.getPlayer()))
            return;
        if (e.getState() != PlayerFishEvent.State.CAUGHT_FISH)
            return;
        if (this.data.containsKey(e.getPlayer())) {
            e.setCancelled(true);
            FishData dat = this.data.get(e.getPlayer());
            if (dat.isDelay())
                return;
            dat.setDelay(true);
            if (dat.getBarCurrent() != dat.getBarTarget()) {
                dat.setBarCurrent(dat.getBarCurrent() + 1);
                PlaySoundEvent.PlaySound(e.getPlayer(), SoundAPI.BAT_TAKEOFF.bukkitSound(), 0.8F, 0.3F);
            } else {
                dat.setBarCurrent(dat.getBarCurrent() + 1);
                TitleAPI.sendTitle(dat.getP(), 0, 20, 10, "", getBar(dat));
                dat.setProgressCurrent(dat.getProgressCurrent() + 1);
                if (dat.getProgressCurrent() >= dat.getProgressTarget()) {
                    dat.getHook().remove();
                    Item item = (Item)dat.getCaught();
                    e.getPlayer().getInventory().addItem(item.getItemStack());
                    this.data.remove(e.getPlayer());
                    if (this.complete != null)
                        PlaySoundEvent.PlaySound(e.getPlayer(), this.complete, 0.3F, 1.2F);
                    TitleAPI.sendTitle(dat.getP(), 10, 30, 20, this.m.getLanguageStorage().getCaughtTitle()[0], this.m.getLanguageStorage().getCaughtTitle()[1]);
                    PlaySoundEvent.PlaySound(e.getPlayer(), SoundAPI.FIREWORK_LAUNCH.bukkitSound(), 1.0F, 2.0F);
                    ActionBarAPI.sendActionBar(dat.getP(), TransColorAPI.TransColor(getDoneProgress(dat)));
                    return;
                }
                PlaySoundEvent.PlaySound(e.getPlayer(), SoundAPI.NOTE_PIANO.bukkitSound(), 1.0F, 2.0F);
            }
        } else {
            PlaySoundEvent.PlaySound(e.getPlayer(), SoundAPI.NOTE_PIANO.bukkitSound(), 1.0F, 0.4F);
            e.setCancelled(true);
            FishData data = new FishData(e.getPlayer(), e.getCaught(), (FishHook)e.getHook());
            data.setItem(e.getPlayer().getInventory().getItemInMainHand());
            this.data.put(e.getPlayer(), data);
        }
    }
}
