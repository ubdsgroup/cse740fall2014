package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class FileUtil {
	public static <K, V> boolean saveHashMapToDisk(String fileName, HashMap<K, V> map ){
		try{
		Map<K, V> ldapContent = map;
		Properties properties = new Properties();

		for (Map.Entry<K,V> entry : ldapContent.entrySet()) {
		    properties.put(entry.getKey(), entry.getValue().toString());
		}

		properties.store(new FileOutputStream(fileName+".properties"), null);
		return true;
		}
		catch(IOException e){
			return false;
		}
	}
	public static <K, V> HashMap<K ,V> readHashMapFromDisk(File file){
		Map<K, V> ldapContent = new HashMap<K, V>();
		Properties properties = new Properties();
		try{
			properties.load(new FileInputStream(file));
		for (Object key : properties.keySet()) {
		   ldapContent.put((K)key, (V)properties.get(key));
		}
			return (HashMap<K, V>) ldapContent;
		}
		catch(IOException e){
			return (HashMap<K, V>) ldapContent;
		}
	}
	
}
