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

    File config = new File(getDataFolder() + File.separator + "config.yml");
    File messenger = new File(getDataFolder() + File.separator + "messages.yml");
    File mineFile = new File(getDataFolder() + File.separator + "mines.json");
    File mineFile2 = new File(getDataFolder() + File.separator + "mines.yml");

    public Main() {
        instance = this;
    }

    @Override
    public void onEnable() {
       // interfaceManager = new InterfaceManager();
       // bookManager = new BookManager();

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new MainHandler(this), this);
    //    pm.registerEvents(interfaceManager,this);

      //  Objects.requireNonNull(getCommand("heal")).setExecutor(new HealExecutor());
      //  Objects.requireNonNull(getCommand("spawn")).setExecutor(new HealExecutor());
        //Objects.requireNonNull(getCommand("addMine")).setExecutor(new MineAdder());
       // Objects.requireNonNull(getCommand("playnewarcadegame")).setExecutor(new GameExecutor());


        loadConfig();
        entitiesHP();
        // LoadEnchantments();

        if (!setupEconomy()) {
            this.getLogger().severe("Disabled due to no Vault dependency found!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }




        try {
            BufferedReader reader = new BufferedReader(new FileReader(mineFile));
            StringBuilder builder = new StringBuilder();
            String currentLine = reader.readLine();
            while (currentLine != null) {
                builder.append(currentLine);
                currentLine = reader.readLine();
            }
            reader.close();

            String json = builder.toString();
            if(!json.isEmpty()) {
            //    mainMineList = new Gson().fromJson(json, MineList.class);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }




    }




    public void loadConfig() {

        if(!config.exists()) {
            log.info("Creating new config file...");
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }
        if(!messenger.exists()) {
            try {
                messenger.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(!mineFile.exists()) {
            try {
                mineFile.createNewFile();
                log.info("New file: Mines.json was created!!!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(!mineFile2.exists()) {
            try {
                mineFile2.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }





        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        saveConfig();
    }

    private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }



    public void stopAllTasks() {
        if(!blockList.isEmpty()) {
            for(Pair<Location,Material> h: blockList) {
                h.t.getBlock().setType(h.u);
            }
            blockList.clear();
        }
    }

    public static String readResourceFile(String path) throws IOException {
        StringBuilder result = new StringBuilder("");
        ClassLoader classLoader = Main.getInstance().getClass().getClassLoader();
        File file = new File(classLoader.getResource(path).getFile());

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }

            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }


    @Override
    @SuppressWarnings("unchecked")
    public void onDisable() {
        log.info("DISABLED!!!");
        stopAllTasks();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            FileWriter file = new FileWriter(mineFile,false);
           // String str = gson.toJson(mainMineList);
         //   file.write(str);
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
