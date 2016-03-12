package es.deantonious.imagefireworks;
 
import java.awt.Color;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
 
 
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
 
public class ReflectionParticleEffects {
    public enum ColoreableParticle {
        SPELL_MOB, SPELL_MOB_AMBIENT, REDSTONE, FIREWORKS_SPARK
    }
    public void initializeClasses(){
       
    }
    /**
     * @param effect         If true, particle distance increases from 256 to 65536.
     * @param location       Position of the particle
     * @param offsetX        This is added to the X position after being multiplied by random.nextGaussian()
     * @param offsetY        This is added to the Y position after being multiplied by random.nextGaussian()
     * @param offsetZ        This is added to the Z position after being multiplied by random.nextGaussian()
     * @param particleData   The data of each particle
     * @param count          Number of particles (The number of particles to create)
     * @param data           Length depends on particle. ICON_CRACK, BLOCK_CRACK, and BLOCK_DUST have lengths of 2, the rest have 0.
     * @throws ClassNotFoundException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws NoSuchFieldException
     * @throws InstantiationException
     */
    public static void sendToLocation(String enumParticleString, Location location, float offsetX, float offsetY, float offsetZ, float particleData, int count, int data) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException, InstantiationException {
        String path = Bukkit.getServer().getClass().getPackage().getName();
        String version = path.substring(path.lastIndexOf(".")+1, path.length());
        Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
        Class<?> Packet = Class.forName("net.minecraft.server." + version + ".Packet");
        Method getHandleMethod = craftPlayer.getMethod("getHandle", new Class<?>[0]);
        Class<?> EnumParticle = Class.forName("net.minecraft.server." + version + ".EnumParticle");
        Object enumParticle = Enum.valueOf((Class<Enum>) EnumParticle, enumParticleString);
        Class<?> PacketPlayOutWorldParticles = Class.forName("net.minecraft.server." + version + ".PacketPlayOutWorldParticles");
        Constructor<?> playOutConstructor =PacketPlayOutWorldParticles.getConstructor(new Class<?>[] {EnumParticle,boolean.class, float.class,float.class,float.class,float.class,float.class,float.class,float.class,int.class,int[].class});
        Object packet = playOutConstructor.newInstance(enumParticle, true, (float)location.getX(), (float)location.getY(), (float)location.getZ(), offsetX, offsetY, offsetZ, particleData, count, new int[]{data});
        for(Player p : Bukkit.getOnlinePlayers()) {
                Object handle = getHandleMethod.invoke(craftPlayer.cast(p), new Object[0]);
                Object pc = handle.getClass().getField("playerConnection").get(handle);
                Method sendPacketMethod = pc.getClass().getMethod("sendPacket", new Class<?>[] {Packet});
                sendPacketMethod.invoke(pc, packet);
        }
    }
 
    public static void sendToLocation(int particleID, Location location, float offsetX, float offsetY, float offsetZ, float particleData, int count, int data) throws NoSuchFieldException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
        String path = Bukkit.getServer().getClass().getPackage().getName();
        String version = path.substring(path.lastIndexOf(".")+1, path.length());
        Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
        Class<?> Packet = Class.forName("net.minecraft.server." + version + ".Packet");
        Method getHandleMethod = craftPlayer.getMethod("getHandle", new Class<?>[0]);
        Class<?> EnumParticle = Class.forName("net.minecraft.server." + version + ".EnumParticle");
        Class<?> PacketPlayOutWorldParticles = Class.forName("net.minecraft.server." + version + ".PacketPlayOutWorldParticles");
        Constructor<?> playOutConstructor =PacketPlayOutWorldParticles.getConstructor(new Class<?>[] {EnumParticle,boolean.class, float.class,float.class,float.class,float.class,float.class,float.class,float.class,int.class,int[].class});
        Method a = EnumParticle.getMethod("a", new Class<?>[]{int.class});
        Object packet = playOutConstructor.newInstance(a.invoke(null, particleID), true, (float)location.getX(), (float)location.getY(), (float)location.getZ(), offsetX, offsetY, offsetZ, particleData, count, new int[]{data});
        for(Player p : Bukkit.getOnlinePlayers()) {
                Object handle = getHandleMethod.invoke(craftPlayer.cast(p), new Object[0]);
                Object pc = handle.getClass().getField("playerConnection").get(handle);
                Method sendPacketMethod = pc.getClass().getMethod("sendPacket", new Class<?>[] {Packet});
                sendPacketMethod.invoke(pc, packet);
        }
    }
 
    /**
     * @param effect         If true, particle distance increases from 256 to 65536.
     * @param player         The player who sees the particle
     * @param location       Position of the particle
     * @param offsetX        This is added to the X position after being multiplied by random.nextGaussian()
     * @param offsetY        This is added to the Y position after being multiplied by random.nextGaussian()
     * @param offsetZ        This is added to the Z position after being multiplied by random.nextGaussian()
     * @param particleData   The data of each particle
     * @param count          Number of particles (The number of particles to create)
     * @param data           Length depends on particle. ICON_CRACK, BLOCK_CRACK, and BLOCK_DUST have lengths of 2, the rest have 0.
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws NoSuchMethodException
     * @throws ClassNotFoundException
     */
    public static void sendToPlayer(String enumParticleString, Player player, Location location, float offsetX, float offsetY, float offsetZ, float particleData, int count, int data) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException, NoSuchMethodException, ClassNotFoundException {
        String path = Bukkit.getServer().getClass().getPackage().getName();
        String version = path.substring(path.lastIndexOf(".")+1, path.length());
        Class<?> Packet = Class.forName("net.minecraft.server." + version + ".Packet");
        Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
        Method getHandleMethod = craftPlayer.getMethod("getHandle", new Class<?>[0]);
        Class<?> EnumParticle = Class.forName("net.minecraft.server." + version + ".EnumParticle");
        Class<?> PacketPlayOutWorldParticles = Class.forName("net.minecraft.server." + version + ".PacketPlayOutWorldParticles");
        Constructor<?> playOutConstructor =PacketPlayOutWorldParticles.getConstructor(new Class<?>[] {EnumParticle,boolean.class, float.class,float.class,float.class,float.class,float.class,float.class,float.class,int.class,int[].class});
        Object enumParticle = Enum.valueOf((Class<Enum>) EnumParticle, enumParticleString);
        Object packet = playOutConstructor.newInstance(enumParticle, true, (float)location.getX(), (float)location.getY(), (float)location.getZ(), offsetX, offsetY, offsetZ, particleData, count, new int[]{data});
        Object handle = getHandleMethod.invoke(craftPlayer.cast(player), new Object[0]);
        Object pc = handle.getClass().getField("playerConnection").get(handle);
        Method sendPacketMethod = pc.getClass().getMethod("sendPacket", new Class<?>[] {Packet});
        sendPacketMethod.invoke(pc, packet);
    }
 
    @SuppressWarnings("rawtypes")
        public static void sendColorParticle(ColoreableParticle particle, Location loc, Color color) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException, InstantiationException {
        String path = Bukkit.getServer().getClass().getPackage().getName();
        String version = path.substring(path.lastIndexOf(".")+1, path.length());
        Class<?> EnumParticle = Class.forName("net.minecraft.server." + version + ".EnumParticle");
        if(particle == ColoreableParticle.REDSTONE)
            sendToLocation(30, loc, color.getRed()/255, color.getGreen()/255, color.getBlue()/255, 1, 0, 0);
        if(particle == ColoreableParticle.SPELL_MOB)
            sendToLocation("SPELL_MOB", loc, color.getRed()/255, color.getGreen()/255, color.getBlue()/255, 1, 0, 0);
        if(particle == ColoreableParticle.SPELL_MOB_AMBIENT)
            sendToLocation("SPELL_MOB_AMBIENT", loc, color.getRed()/255, color.getGreen()/255, color.getBlue()/255, 1, 0, 0);
    }
 
}