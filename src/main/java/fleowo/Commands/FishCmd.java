    package fleowo.Commands;

    import fleowo.RPGFishing;
    import org.bukkit.command.Command;
    import org.bukkit.command.CommandExecutor;
    import org.bukkit.command.CommandSender;

    import static fleowo.Utils.TransColorAPI.TransColor;

    public class FishCmd implements CommandExecutor {
        private RPGFishing m;

        public FishCmd(RPGFishing m) {
            this.m = m;
        }

        public boolean onCommand(CommandSender snd, Command cmd, String lbl, String[] args) {
            if (!snd.hasPermission("rpgfishing.admin"))
                return true;
            redo(snd, args);
            return true;
        }

        public static void sendMsg(CommandSender b, String msg) {
            b.sendMessage(TransColor(msg));
        }

        private void redo(CommandSender p, String[] args) {
            if (args.length == 0) {
                sendMsg(p, "&2&lRPG&a&lFishing");
                sendMsg(p, "&7> &7/rpgfishing &creload");
                return;
            }
            String str;
            switch ((str = args[0].toLowerCase()).hashCode()) {
                case -934641255:
                    if (!str.equals("reload"))
                        break;
                    this.m.getLanguageStorage().loadConfig();
                    sendMsg(p, "&aSuccessfully reloaded");
                    return;
            }
            sendMsg(p, "&2&lRPG&a&lFishing");
            sendMsg(p, "&7> &7/rpgfishing &creload");
        }
    }