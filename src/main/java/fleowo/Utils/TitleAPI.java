package fleowo.Utils;

import org.bukkit.entity.Player;

import static fleowo.Utils.TransColorAPI.TransColor;

public class TitleAPI {

        //Send Title and Subtitle to Player v1.19+ only
        public static void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {
            title = TransColor(title);
            subtitle = TransColor(subtitle);
            player.sendTitle((title), (subtitle), 10, 70, 20);

        }
}