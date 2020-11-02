package by.d0mpi.rebornPlugin;



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.util.*;
import java.util.logging.Handler;
import java.util.logging.Logger;

import static javax.sql.rowset.spi.SyncFactory.getLogger;

public class Main extends JavaPlugin {

    public List<Pair<Location, Material>> blockList = new ArrayList<>();



    @Getter
    private static Main instance;
    @Getter
    private Economy economy;
    @Getter

    final private Logger log = getLogger();

    public Main() {
        instance = this;
    }

    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new MainHandler(this), this);
        log.info("ENABLED!!!");
        entitiesHP();
    }


    @Override
    public void onDisable() {
        log.info("DISABLED!!!");


    }


    public void entitiesHP() {
        new BukkitRunnable() {

            @Override
            @Deprecated
            public void run() {
                for(LivingEntity e: Objects.requireNonNull(getServer().getWorld("world")).getLivingEntities()) {

                    e.setCustomName(e.getType() + ""  + ChatColor.GREEN + " HP [" + e.getHealth() + "/" + e.getMaxHealth() + "]");
                    e.setCustomNameVisible(true);
                }
            }
        }.runTaskTimer(Main.getInstance(),0,10);

    }
}

class Pair<T, U>  {
    public final T t;
    public final U u;

    public Pair(T t, U u) {
        this.t= t;
        this.u= u;
    }

    public T getT() {
        return t;
    }

    public U getU() {
        return u;
    }
}
