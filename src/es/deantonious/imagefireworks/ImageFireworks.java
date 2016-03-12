package es.deantonious.imagefireworks;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Dispenser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class ImageFireworks extends JavaPlugin implements Listener {

	public static ImageFireworks plugin;
	public static File dataFolder;
	private static String commandUse = ChatColor.RED + "Command Use: /imgfws <give:launch> <player> <firework>";
	private static HashMap<String, String> fireworkList = new HashMap<String, String>();

	@Override
	public void onEnable() {
		plugin = this;
		dataFolder = getDataFolder();
		Bukkit.getServer().getPluginManager().registerEvents(this, this);

		File imageDir = new File(dataFolder + File.separator + "images");
		if (!imageDir.exists()) {
			imageDir.mkdirs();
		}

		File fwsDir = new File(dataFolder + File.separator + "fireworks");
		if (!fwsDir.exists()) {
			fwsDir.mkdirs();
		}

		File fireworksFile = new File(dataFolder + File.separator + "fireworks" + File.separator + "demofirework.yml");
		FileConfiguration fireworks = YamlConfiguration.loadConfiguration(fireworksFile);
		if (!fireworksFile.exists()) {
			fireworks.set("Name", "Demo Firework");
			fireworks.set("Image", "imgfw.png");
			fireworks.set("Color.Use", false);
			fireworks.set("Color.R", 255);
			fireworks.set("Color.G", 255);
			fireworks.set("Color.B", 0);
			try {
				fireworks.save(fireworksFile);
			} catch (Exception e) {
			}
		}
		saveResource("images" + File.separator + "imgfw.png", false);
		updateFireworkList();
	}

	@Override
	public void onDisable() {
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("imagefireworks")) {
			if (sender.hasPermission("imagefireworks.use")) {
				if (args.length > 0) {
					String option = args[0];

					if (option.equalsIgnoreCase("give")) {
						String pName = args[1];
						String fwName = ChatColor.RESET + "";
						for(int i = 2; i < args.length; i++) {
							fwName = fwName + args[i] + " ";
						}
						fwName = fwName.trim();
						if (Bukkit.getPlayer(pName) != null) {
							Player target = Bukkit.getPlayer(pName);

							if (fireworkList.containsKey(fwName)) {
								File fwFile = new File(dataFolder + File.separator + "fireworks" + File.separator + fireworkList.get(fwName));
								FileConfiguration fw = YamlConfiguration.loadConfiguration(fwFile);

								ItemStack iS = new ItemStack(Material.FIREWORK, 1);
								ItemMeta iM = iS.getItemMeta();
								iM.setDisplayName(ChatColor.RESET + fw.getString("Name"));
								ArrayList<String> iL = new ArrayList<String>();
								iL.add(ChatColor.RED + "Image Firework");
								iM.setLore(iL);
								iS.setItemMeta(iM);

								target.getInventory().addItem(iS);
								target.updateInventory();
								
							} else {
								sender.sendMessage(ChatColor.RED + "Not a valid firework name: " + fwName);
							}
						} else {
							sender.sendMessage(ChatColor.RED + "You must enter a valid player name...");
						}
					} else if (option.equalsIgnoreCase("launch")) {
						String pName = args[1];
						String fwName = ChatColor.RESET + "";
						for(int i = 2; i < args.length; i++) {
							fwName = fwName + args[i] + " ";
						}
						fwName = fwName.trim();
						if (Bukkit.getPlayer(pName) != null) {
							Player target = Bukkit.getPlayer(pName);
							if(fireworkList.containsKey(fwName)) {
								CustomFirework cfw = new CustomFirework(fireworkList.get(fwName));
								cfw.useFirework(target.getLocation());
							} else {
								sender.sendMessage(ChatColor.RED + "Not a valid firework name: " + fwName);
							}

						} else {
							sender.sendMessage(ChatColor.RED + "You must enter a valid player name...");
						}
					} else if (option.equalsIgnoreCase("reload")) {
						updateFireworkList();
						sender.sendMessage(ChatColor.GREEN + "Fireworks List reloaded...");
					} else {
						sender.sendMessage(commandUse);
					}
				} else {
					sender.sendMessage(commandUse);
					sender.sendMessage("Active Fireworks: " + fireworkList.keySet().toString());
				}
				return true;
			} else {
				sender.sendMessage("You don't have permission to use this command.");
			}

		}
		return true;
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.getItem() != null && event.getItem().hasItemMeta()) {
				if (event.getItem().getItemMeta().getLore().get(0).equals(ChatColor.RED + "Image Firework")) {
					event.setCancelled(true);
					if(fireworkList.containsKey(event.getItem().getItemMeta().getDisplayName())) {
						CustomFirework cfw = new CustomFirework(fireworkList.get(event.getItem().getItemMeta().getDisplayName()));
						Location loc = event.getClickedBlock().getLocation();
						loc.setY(loc.getY() + 1);
						cfw.useFirework(loc);
						if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
							event.getPlayer().getInventory().removeItem(event.getPlayer().getItemInHand());
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onDispenseFirework(BlockDispenseEvent event) {
	
		if (event.getItem() != null && event.getItem().hasItemMeta()) {
			if (event.getItem().getItemMeta().getLore().get(0).equals(ChatColor.RED + "Image Firework")) {
				event.setCancelled(true);
				if(fireworkList.containsKey(event.getItem().getItemMeta().getDisplayName())) {
					Dispenser d = (Dispenser) event.getBlock().getState();
					d.getInventory().removeItem(event.getItem());
					CustomFirework cfw = new CustomFirework(fireworkList.get(event.getItem().getItemMeta().getDisplayName()));
					Location loc = event.getBlock().getLocation();
					loc.setX(loc.getX()+0.5);
					loc.setY(loc.getY()+0.5);
					loc.setZ(loc.getZ()+0.5);

					cfw.useFirework(loc);
				}
			}
		}
	}

	private static void updateFireworkList() {
		File[] files = new File(dataFolder + File.separator + "fireworks").listFiles();
		for (File file : files) {
			if (file.isFile()) {
				File fwFile = new File(dataFolder + File.separator + "fireworks" + File.separator + file.getName());
				FileConfiguration fw = YamlConfiguration.loadConfiguration(fwFile);
				fireworkList.put(ChatColor.RESET + fw.getString("Name"), file.getName());
			}
		}

	}
}
