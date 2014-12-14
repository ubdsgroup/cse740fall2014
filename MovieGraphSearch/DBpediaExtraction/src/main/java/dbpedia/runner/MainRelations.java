package dbpedia.runner;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;

import util.FileUtil;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.sparql.engine.http.QueryExceptionHTTP;

public class MainRelations {
	public static String service = "http://dbpedia.org/sparql";
	static HashMap<String, HashSet<String>> proptomovie = new HashMap<String, HashSet<String>>();
	static HashMap<String,String> edges = new HashMap<String,String>();
	
	public static void main(String[] args) {
		int count = 0;
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("MovieMap.properties"));
			for (Object key : properties.keySet()) {
				// ldapContent.put((String)key,(String)properties.get(key));
				addDataforMovie((String) key,"moviefiles");
				System.out.println(count++);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			
				
		}

		
	}

	public static void getMovieFiles(String moviemap,String directory){
		int count = 0;
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(moviemap+".properties"));
			for (Object key : properties.keySet()) {
				// ldapContent.put((String)key,(String)properties.get(key));
				addDataforMovie((String) key,directory);
				System.out.println(count++);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			
				
		}
		
	}
	
	private static void addDataforMovie(String str,String directory) {

		String str2 = "\"" + str + "\"^^xsd:integer";

		String query = getMovieQuery(str2);
		QueryExecution qe = QueryExecutionFactory.sparqlService(service, query);
		HashMap<String,HashSet<String>> MovieHashMap = new HashMap<String, HashSet<String>>();
		HashSet<String> r = new HashSet<String>();
		r.add(str);
		MovieHashMap.put("id", r);

		try {
			ResultSet results = qe.execSelect();
			for (; results.hasNext();) {
				QuerySolution soln = results.next();
				
				RDFNode property = soln.get("property");
				RDFNode data = soln.get("data");
				
				// dbo:director OR dbpedia2:director
				if(property.toString().trim().equals("http://dbpedia.org/property/director") || 
						property.toString().trim().equals("http://dbpedia.org/ontology/director") ){
					if(data.isLiteral()){
						addtoHashMap("director",parseLiteral(data.asLiteral().getString()),str,MovieHashMap);
					}else{
						if(data.asResource().toString().length() > 28)
						if(!data.asResource().getLocalName().trim().equals("")){
							addtoHashMap("director",data.asResource().getLocalName(),str,MovieHashMap);
						}else{
							addtoHashMap("director",data.asResource().toString().substring(28, data.asResource().toString().length()),str,MovieHashMap);
						}
						
					}
				}
				
				
				// dbo:starring OR dbpedia2:starring
				else if(property.toString().trim().equals("http://dbpedia.org/property/starring") || 
						property.toString().trim().equals("http://dbpedia.org/ontology/starring") ){
					if(data.isLiteral()){
						addtoHashMap("starring",parseLiteral(data.asLiteral().getString()),str,MovieHashMap);
					}else{
						if(data.asResource().toString().length() > 28)
						if(!data.asResource().getLocalName().trim().equals("")){
							addtoHashMap("starring",data.asResource().getLocalName(),str,MovieHashMap);
						}else{
							addtoHashMap("starring",data.asResource().toString().substring(28, data.asResource().toString().length()),str,MovieHashMap);
						}
					}
				}
				
				
				// dbo:producer OR dbpedia2:producer
				else if(property.toString().trim().equals("http://dbpedia.org/property/producer") || 
						property.toString().trim().equals("http://dbpedia.org/ontology/producer") ){
					if(data.isLiteral()){
						addtoHashMap("producer",parseLiteral(data.asLiteral().getString()),str,MovieHashMap);
					}else{
						if(data.asResource().toString().length() > 28)
						if(!data.asResource().getLocalName().trim().equals("")){
							addtoHashMap("producer",data.asResource().getLocalName(),str,MovieHashMap);
						}else{
							addtoHashMap("producer",data.asResource().toString().substring(28, data.asResource().toString().length()),str,MovieHashMap);
						}
					}
				}
				
				
				// dbo:cinematography OR dbpedia2:cinematography
				else if(property.toString().trim().equals("http://dbpedia.org/property/cinematography") || 
						property.toString().trim().equals("http://dbpedia.org/ontology/cinematography") ){
					if(data.isLiteral()){
						addtoHashMap("camera",parseLiteral(data.asLiteral().getString()),str,MovieHashMap);
					}else{
						if(data.asResource().toString().length() > 28)
						if(!data.asResource().getLocalName().trim().equals("")){
							addtoHashMap("camera",data.asResource().getLocalName(),str,MovieHashMap);
						}else{
							addtoHashMap("camera",data.asResource().toString().substring(28, data.asResource().toString().length()),str,MovieHashMap);
						}
					}
				}
				
				// dbo:editing OR dbpedia2:editing
				else if(property.toString().trim().equals("http://dbpedia.org/property/editing") || 
						property.toString().trim().equals("http://dbpedia.org/ontology/editing") ){
					if(data.isLiteral()){
						addtoHashMap("editor",parseLiteral(data.asLiteral().getString()),str,MovieHashMap);
					}else{
						if(data.asResource().toString().length() > 28)
						if(!data.asResource().getLocalName().trim().equals("")){
							addtoHashMap("editor",data.asResource().getLocalName(),str,MovieHashMap);
						}else{
							addtoHashMap("editor",data.asResource().toString().substring(28, data.asResource().toString().length()),str,MovieHashMap);
						}
					}
				}
				
				
				// dbo:musicComposer OR dbpedia2:music
				else if(property.toString().trim().equals("http://dbpedia.org/property/music") || 
						property.toString().trim().equals("http://dbpedia.org/ontology/musicComposer") ){
					if(data.isLiteral()){
						addtoHashMap("music",parseLiteral(data.asLiteral().getString()),str,MovieHashMap);
					}else{
						if(data.asResource().toString().length() > 28)
						if(!data.asResource().getLocalName().trim().equals("")){
							addtoHashMap("music",data.asResource().getLocalName(),str,MovieHashMap);
						}else{
							addtoHashMap("music",data.asResource().toString().substring(28, data.asResource().toString().length()),str,MovieHashMap);
						}
					}
				}
				
				// dbo:writer OR dbpedia2:writer OR dbpedia2:writers OR dbpedia2:story OR dbpedia2:screenplay
				
				else if(property.toString().trim().equals("http://dbpedia.org/ontology/writer") || 
						property.toString().trim().equals("http://dbpedia.org/property/writer") ||
						property.toString().trim().equals("http://dbpedia.org/property/writers") ||
						property.toString().trim().equals("http://dbpedia.org/property/story") ||
						property.toString().trim().equals("http://dbpedia.org/property/screenplay") ){
					if(data.isLiteral()){
						addtoHashMap("writer",parseLiteral(data.asLiteral().getString()),str,MovieHashMap);
					}else{
						if(data.asResource().toString().length() > 28)
						if(!data.asResource().getLocalName().trim().equals("")){
							addtoHashMap("writer",data.asResource().getLocalName(),str,MovieHashMap);
						}else{
							addtoHashMap("writer",data.asResource().toString().substring(28, data.asResource().toString().length()),str,MovieHashMap);
						}
					}
				}
				
				
				// dbpedia2:genre
				else if(property.toString().trim().equals("http://dbpedia.org/property/genre") ){
					if(data.isLiteral()){
						addtoHashMap("genre",parseLiteral(data.asLiteral().getString()),str,MovieHashMap);
					}else{
						if(data.asResource().toString().length() > 28)
						if(!data.asResource().getLocalName().trim().equals("")){
							addtoHashMap("genre",data.asResource().getLocalName(),str,MovieHashMap);
						}else{
							addtoHashMap("genre",data.asResource().toString().substring(28, data.asResource().toString().length()),str,MovieHashMap);
						}
					}
				}
				
				// dbpedia2:awards
				else if(property.toString().trim().equals("http://dbpedia.org/property/awards") ){
					if(data.isLiteral()){
						addtoHashMap("awards",parseLiteral(data.asLiteral().getString()),str,MovieHashMap);
					}else{
						if(data.asResource().toString().length() > 28)
						if(!data.asResource().getLocalName().trim().equals("")){
							addtoHashMap("awards",data.asResource().getLocalName(),str,MovieHashMap);
						}else{
							addtoHashMap("awards",data.asResource().toString().substring(28, data.asResource().toString().length()),str,MovieHashMap);
							
						}
					}
				}
				
				// dcterms:subject
				else if(property.toString().trim().equals("http://purl.org/dc/terms/subject") ){
					if(data.isLiteral()){
						
						addtoHashMap("category",parseLiteral(data.asLiteral().getString()),str,MovieHashMap);
					}else{
						String URI = data.asResource().toString().substring(37, data.asResource().toString().length()).trim();
						if(!URI.toString().equalsIgnoreCase("English-language_films") &&	!URI.toString().equalsIgnoreCase("American_films") ){
							if(!data.asResource().getLocalName().trim().equals("")){
								addtoHashMap("category",URI,str,MovieHashMap);
							}else{
								addtoHashMap("category",URI,str,MovieHashMap);
							}
						}
					}
				}
				FileUtil.saveHashMapToDisk(directory+"/"+str, MovieHashMap);
				

			}
		} catch (QueryExceptionHTTP e) {
			System.out.println(service + " is DOWN");
		} finally {
			qe.close();
		} // end try/catch/finally

	}

	private static void addtoHashMap(String prop,String property, String movie, HashMap<String, HashSet<String>> movieHashMap ) {
		
		if (!property.equalsIgnoreCase("English-language_films")
				&& !property.equalsIgnoreCase("American_films")) {
			if (proptomovie.containsKey(prop + ":" + property)) {
				if (!proptomovie.get(prop + ":" + property).contains(movie)) {
					proptomovie.get(prop + ":" + property).add(movie);
				}
			} else {
				HashSet<String> t = new HashSet<String>();
				t.add(movie);
				proptomovie.put(prop + ":" + property, t);
			}

		}
		if (movieHashMap.containsKey(prop)) {
			if (!movieHashMap.get(prop).contains(property)) {
				movieHashMap.get(prop).add(property);
			}
		} else {
			HashSet<String> t = new HashSet<String>();
			t.add(property);
			movieHashMap.put(prop, t);
		}

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
