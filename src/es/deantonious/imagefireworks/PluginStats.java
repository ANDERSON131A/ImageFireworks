package es.deantonious.imagefireworks;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class PluginStats {

	public static void sendData(Plugin pl) throws IOException {
		
		String plugin = pl.getDescription().getName();
		String version = pl.getDescription().getVersion();
		
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		
		URL url = new URL("http://stats.deantonious.es/submit.php?plugin=+"+plugin+"&version="+version+"&date="+dateFormat.format(date)+"&players=" + Bukkit.getServer().getOnlinePlayers().size()+"&"+"");
	    URLConnection conn = url.openConnection();
	    InputStream is = conn.getInputStream();
	}

}