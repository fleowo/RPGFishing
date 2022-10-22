package fleowo.Events;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PlaySoundEvent {
    public static void PlaySound(Player p, Sound s, Float vol, Float pit) {
        p.playSound(p.getLocation(), s, vol.floatValue(), pit.floatValue());
    }
}
