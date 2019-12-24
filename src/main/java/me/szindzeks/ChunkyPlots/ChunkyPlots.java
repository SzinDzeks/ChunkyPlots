package me.szindzeks.ChunkyPlots;

import me.szindzeks.ChunkyPlots.commands.PlotAdminCommand;
import me.szindzeks.ChunkyPlots.commands.PlotCommand;
import me.szindzeks.ChunkyPlots.listener.*;
import me.szindzeks.ChunkyPlots.manager.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class ChunkyPlots extends JavaPlugin {
	public static ChunkyPlots plugin;
	public FileConfiguration config;

	public PlotManager plotManager;
	public UserManager userManager;
	public ConfigManager configManager;
	public VisitManager visitManager;
	public CraftingManager craftingManager;
	public MarketManager marketManager;

	@EventHandler
	public void onEnable(){
		setupPluginConfig();
		config = this.getConfig();
		plugin = this;

		initializeManagers();

		registerListeners();
		registerCommands();
	}

	private void setupPluginConfig() {
		this.saveDefaultConfig();
	}

	@EventHandler
	public void onDisable(){

	}

	private void initializeManagers(){
		configManager = new ConfigManager();
		plotManager = new PlotManager();
		userManager = new UserManager();
		visitManager = new VisitManager();
		craftingManager = new CraftingManager();
		marketManager = new MarketManager();
	}

	private void registerListeners(){
		this.getServer().getPluginManager().registerEvents(new AreaEffectCloudApplyListener(),this);
		this.getServer().getPluginManager().registerEvents(new BlockBreakListener(),this);
		this.getServer().getPluginManager().registerEvents(new BlockBurnListener(),this);
		this.getServer().getPluginManager().registerEvents(new BlockIgniteListener(),this);
		this.getServer().getPluginManager().registerEvents(new BlockFromToListener(),this);
		this.getServer().getPluginManager().registerEvents(new BlockSpreadListener(),this);
		this.getServer().getPluginManager().registerEvents(new BlockPistonExtendListener(),this);
		this.getServer().getPluginManager().registerEvents(new BlockPistonRetractListener(),this);
		this.getServer().getPluginManager().registerEvents(new BlockPlaceListener(),this);
		this.getServer().getPluginManager().registerEvents(new DispenseListener(),this);
		this.getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(),this);
		this.getServer().getPluginManager().registerEvents(new ExplodeProtection(),this);
		this.getServer().getPluginManager().registerEvents(new PlayerBucketEmptyListener(),this);
		this.getServer().getPluginManager().registerEvents(new PlayerInteractAtEntityListener(),this);
		this.getServer().getPluginManager().registerEvents(new PlayerInteractListener(),this);
		this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(),this);
		this.getServer().getPluginManager().registerEvents(new PlayerLeftListener(),this);
		this.getServer().getPluginManager().registerEvents(new PlayerLeashEntityListener(),this);
		this.getServer().getPluginManager().registerEvents(new PlayerMoveListener(),this);
		this.getServer().getPluginManager().registerEvents(new PlayerShearEntityListener(),this);
		this.getServer().getPluginManager().registerEvents(new PlayerTakeLecternBookListener(),this);
		this.getServer().getPluginManager().registerEvents(new PlayerUnleashEntityListener(),this);
		this.getServer().getPluginManager().registerEvents(new PotionProtection(),this);
		this.getServer().getPluginManager().registerEvents(new VehicleDamageListener(),this);
		this.getServer().getPluginManager().registerEvents(new VehicleEnterListener(),this);
	}
	private void registerCommands(){
		getCommand("plot").setExecutor(new PlotCommand());
		getCommand("plotadmin").setExecutor(new PlotAdminCommand());
	}
}
