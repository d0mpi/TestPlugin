package by.d0mpi.rebornPlugin;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.logging.Logger;

public class MainHandler implements Listener {

    final public Logger logger = Main.getInstance().getLogger();

    public MainHandler(Main main) {
    }

    @EventHandler
    public void  join(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        World world = player.getWorld();

        player.sendMessage("Hello" + player.getName());
    }
}
