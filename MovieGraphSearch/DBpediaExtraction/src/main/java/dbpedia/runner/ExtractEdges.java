package dbpedia.runner;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import util.FileUtil;

public class ExtractEdges {
	public static String service = "http://dbpedia.org/sparql";
	static HashMap<String, HashSet<String>> proptomovie = new HashMap<String, HashSet<String>>();
	static HashMap<String, Set<String>> edgeBuffer = new HashMap<String,Set<String>>();
	static HashMap<String,String> edges = new HashMap<String,String>();
	
	//static HashSet<String> tempHashSet = new HashSet<String>();
	int edgefilecount = 0;
	public static void main(String[] args) {

		// loadfromTextFile();

		File dir = new File(
				"D:\\Workspaces\\BackupWorkspace\\jena-examples\\moviefiles");

		File[] allFiles = dir.listFiles();
		int count = 0;
		for (File file : allFiles) {
			count++;
			System.out.println(count);
			HashMap<String, String> pMap = FileUtil.readHashMapFromDisk(file);
			String movieid = pMap.get("id");
			movieid = movieid.substring(1, movieid.length() - 1);
			// if (tempHashSet.contains(movieid))
			for (String property : pMap.keySet()) {
				sendtofunction(property, pMap.get(property), movieid);
			}
		}

		// printHashMap(proptomovie);
		System.out.println("Property:" + proptomovie.keySet().size());
		saveEdges();
		// pivotEdges();
		// printHashMap2(edges);
		FileUtil.saveHashMapToDisk("EdgeMapExtra", edges);

		// }
		
	}
	
	public static void getEdgesfromMovies(String directory,String EdgeMap){
		// loadfromTextFile();

		File dir = new File(directory);
		File[] allFiles = dir.listFiles();
		int count = 0;
		for (File file : allFiles) {
			count++;
			System.out.println(count);
			HashMap<String, String> pMap = FileUtil.readHashMapFromDisk(file);
			String movieid = pMap.get("id");
			movieid = movieid.substring(1, movieid.length() - 1);
			// if (tempHashSet.contains(movieid))
			for (String property : pMap.keySet()) {
				sendtofunction(property, pMap.get(property), movieid);
			}
		}
		System.out.println("Property:" + proptomovie.keySet().size());
		saveEdges();
		FileUtil.saveHashMapToDisk(EdgeMap, edges);
	}

//	private static void loadfromTextFile() {
//		BufferedReader br = null;
//		try {
//			String sCurrentLine=null;
//			br = new BufferedReader(new FileReader("5000.txt"));
//			while ((sCurrentLine = br.readLine()) != null) {
//				tempHashSet.add(sCurrentLine.trim());
//			}
//		} catch (FileNotFoundException e) {
//
//			e.printStackTrace();
//		} catch (IOException e) {
//			
//			e.printStackTrace();
//		} finally{
//			try {
//				br.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}


	private static void addtoEdgeMapArray(String s, String[] strarray) {
		if (strarray.length == 2) {

			String node1 = strarray[0];
			String node2 = strarray[1];
			int maxnode = Math.max(Integer.valueOf(node1),
					Integer.valueOf(node2));
			String edgeStr = "";
			if (maxnode == Integer.valueOf(node1)) {
				edgeStr = maxnode + "-" + node2;
			} else {
				edgeStr = maxnode + "-" + node1;
			}
			if (edges.containsKey(edgeStr)) {
				edges.put(edgeStr, edges.get(edgeStr) + "|" + s);
			} else {
				edges.put(edgeStr, s);
			}
		}
	}

	private static void sendtofunction(String key, String value, String id) {
		value = value.substring(1, value.length() - 1);
		String[] r = value.split(",");
		for(String s : r){
			s=s.trim();
			if(!s.equals(""))
			addtoHashMap(key,s,id);
		}
		
	}
	
	private static void addtoHashMap(String prop,String property, String movie) {
		if(proptomovie.containsKey(prop+":"+property)){
			if(!proptomovie.get(prop+":"+property).contains(movie)){
				proptomovie.get(prop+":"+property).add(movie);
			}
		}else{
			HashSet<String> t = new HashSet<String>();
			t.add(movie);
			proptomovie.put(prop+":"+property, t);
		}
	}
	
	

	private static void saveEdges() {
		int count =0;
		for (String s : proptomovie.keySet()) {
			count++;
			
			System.out.println(count+" - "+proptomovie.get(s).size());
			if(proptomovie.get(s).size() > 1 && proptomovie.get(s).size() < 500){
				HashSet<String> r ;
				if(proptomovie.get(s).size() == 2){
					r = new HashSet<String>();
					Iterator<String> it = proptomovie.get(s).iterator();
					String one = it.next();
					String two = it.next();
					r.add(one+"-"+two);
					for(String a : r){
						addtoEdgeMapArray(s,a.split("-"));
					}
					r.clear();
					
				}else{
					if(proptomovie.get(s).contains("13149")){
						System.out.println("print");
					}
					List<String> l = new ArrayList<String>(proptomovie.get(s));
					r = new HashSet<String>();
					combinations(r, l.toArray());
					for(String a : r){
						addtoEdgeMapArray(s,a.split("-"));
					}
					r.clear();
					l.clear();
				}
			}
		}
	}
	
	private static void combinations(HashSet<String> r, Object[] objects){
		if(objects.length == 0){
			return;
		}else if(objects.length == 1){
			return;
		}else{
			//HashSet<String> set ;
			String s = (String) objects[0];
			for (Object a : Arrays.copyOfRange(objects, 1, objects.length) ) {
				
				r.add(s+"-"+(String)a);
			}
		}
		
		combinations(r, Arrays.copyOfRange(objects, 1, objects.length));
		
	}


	private static String parseLiteral(String string) {
		string = string.replaceAll("\\s", "_");
		return string;
	}

	private static String getMovieQuery(String str) {
		String q = "PREFIX dbo: <http://dbpedia.org/ontology/> "
				+ "PREFIX dbpedia2: <http://dbpedia.org/property/> "
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
				+ "SELECT ?film ?property ?data "
				+ "WHERE {?film dbo:wikiPageID " + str + " . "
				+ "?film rdf:type dbo:Film . " + "?film ?property ?data . }";
		return q;
	}

}
