package es.deantonious.imagefireworks;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import es.deantonious.imagefireworks.ReflectionParticleEffects.ColoreableParticle;

public class CustomFirework {
	
	private String image;
	private ColoreableParticle particle;
	private Color color;
	private String name;
	private boolean useColor;
	
	public CustomFirework(String fireworkFile) {
		File fwFile = new File(ImageFireworks.dataFolder + File.separator + "fireworks" + File.separator + fireworkFile);
		FileConfiguration fw = YamlConfiguration.loadConfiguration(fwFile);
		
		this.name = fw.getString("Name");
		this.image = fw.getString("Image");
		this.particle = ColoreableParticle.FIREWORKS_SPARK;
		this.useColor = fw.getBoolean("Color.Use");
		this.color = new Color(fw.getInt("Color.R"), fw.getInt("Color.G"), fw.getInt("Color.B"));
	}
	
	public void useFirework(final Location center) {
		
		final Firework item = (Firework) center.getWorld().spawnEntity(center, EntityType.FIREWORK);
		FireworkMeta fM = item.getFireworkMeta();
		fM.setPower(2);
		item.setFireworkMeta(fM);
		
		center.getWorld().playSound(center, Sound.FIREWORK_LAUNCH, 3, 1);

		
		new BukkitRunnable() {
			
			int timer = 100;
			
			@Override
			public void run() {
				if(timer > 0) {
					timer--;
					if(item.getLocation().add(0, 1, 0).getBlock().getType() != Material.AIR) {
						Location loc = item.getLocation().clone();
						loc.setY(loc.getY()+4);
						explodeFirework(loc);
						item.remove();
						cancel();
					}
					if(!(item.getLocation().getY() < center.getY()+10)) {
						Location loc = item.getLocation().clone();
						loc.setY(loc.getY()+4);
						explodeFirework(loc);
						item.remove();
						cancel();
					}
				} else {
					cancel();
				}
			}
		}.runTaskTimer(ImageFireworks.plugin, 0, 2);
		
	}
	
	private void explodeFirework(final Location center) {
		center.getWorld().playSound(center, Sound.FIREWORK_BLAST, 3, 1);
		final ArrayList<Vector> firework = generateFirework(image);
		final int xIni = center.getBlockX();
		final int yIni = center.getBlockY();
		final int zIni = center.getBlockZ();
		
		if(useColor == true) {
			this.particle = ColoreableParticle.REDSTONE;
			new BukkitRunnable() {
				int times = 30;
				@Override
				public void run() {
					if(times > 0) {
						times--;
						for(int i = 0; i < firework.size(); i++){
							center.setX(xIni + firework.get(i).getX()/5.0);
							center.setZ(zIni);
							center.setY(yIni + firework.get(i).getY()/5.0);
							try {
								ReflectionParticleEffects.sendColorParticle(particle, center, color);
							} catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException | InstantiationException e) {
								e.printStackTrace();
							}
						}
					} else {
						cancel();
					}	
				}
			}.runTaskTimer(ImageFireworks.plugin, 0L, 0L);
		} else {
			new BukkitRunnable() {
				int times = 1;
				@Override
				public void run() {
					if(times > 0) {
						times--;
						for(int i = 0; i < firework.size(); i++){
							center.setX(xIni + firework.get(i).getX()/5.0);
							center.setZ(zIni);
							center.setY(yIni + firework.get(i).getY()/5.0);
							try {
								ReflectionParticleEffects.sendToLocation(3, center, 0, 0, 0, 0, 1, 0);
							} catch (NoSuchFieldException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
								e.printStackTrace();
							}
						}
					} else {
						cancel();
					}	
				}
			}.runTaskTimer(ImageFireworks.plugin, 0L, 0L);
		}


	}
	
	private static ArrayList<Vector> generateFirework(String image) {
		
		ArrayList<Vector> result = new ArrayList<Vector>();
		File imageFile = new File(ImageFireworks.dataFolder + File.separator + "images" + File.separator + image);
		if(!imageFile.exists()) {
			try {
				throw new Exception(ChatColor.RED + "Could not find " + image);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		BufferedImage imagen;
        try{
            imagen = ImageIO.read(imageFile);
        }
        catch(Exception e){
        	throw new RuntimeException("Exception: " + e.getMessage() + " - File:" + imageFile.getAbsolutePath());
        }
        if(imagen == null)
        	return result;
        process(imagen, result);
        return result;
		
	}

	private static void process(BufferedImage image, ArrayList<Vector> result) {
		int offsetX = -image.getWidth()/2;
		int offsetY = image.getHeight()/2;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if (image.getRGB(x, y) != -16777216) {
					result.add(new Vector(x + offsetX, -1*y + offsetY, 0));
                }
            }
        }
	}
}
