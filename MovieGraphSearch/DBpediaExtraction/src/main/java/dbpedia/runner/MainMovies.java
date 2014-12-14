package dbpedia.runner;

import java.util.HashMap;

import util.FileUtil;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.sparql.engine.http.QueryExceptionHTTP;

public class MainMovies {
	public static String service = "http://dbpedia.org/sparql";
	public static void main(String[] args) {
		HashMap<String,String> moviehashmap = getMovieResults();
		FileUtil.saveHashMapToDisk("MovieMap",moviehashmap);
	}
	
	public static void getMovies(String filename){
		HashMap<String,String> moviehashmap = getMovieResults();
		FileUtil.saveHashMapToDisk(filename,moviehashmap);
	}
	
	private static String getMovieQuery(String str) {
		String query = "PREFIX dbo: <http://dbpedia.org/ontology/> "+
				"PREFIX dbpedia2: <http://dbpedia.org/property/> "+
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "+
				"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "+
				"SELECT DISTINCT ?film ?id "+
				"WHERE {?film dbo:wikiPageID ?id . "+
				//for only one film
				//"?film dbo:wikiPageID 38923881 . "+
				// one film
				"?film rdf:type dbo:Film . "+
				"?film dbpedia2:country \"United States\"@en . "+
				"?film dbpedia2:language \"English\"@en . "+
				"OPTIONAL {?film dbo:releaseDate ?releasedate . "+
				 "        FILTER (?releasedate > \"1950-01-01\"^^xsd:date). }"+
				"?film rdfs:label ?label . "+
				"FILTER (LANG(?label ) = 'en') . "+
				"FILTER regex(?label , \"^["+str+"](.*)*$\", \"i\" )  . }"+
				"ORDER BY ?film";
		return query;
	}
	private static HashMap<String, String> getMovieResults() {
		HashMap<String,String> moviehashmap = new HashMap<String, String>();
		String query1 = getMovieQuery("A-La-l");
	
        QueryExecution qe = QueryExecutionFactory.sparqlService(service, query1);
        int count = 0;
        try {
        	ResultSet results = qe.execSelect() ;
            for ( ; results.hasNext() ; )
            {
                QuerySolution soln = results.next() ;
                RDFNode it = soln.get("film");
                String s = soln.getLiteral("id").toString();
                moviehashmap.put(s.substring(0, s.length() - 42), it.toString());
                count++;
            }
        } catch (QueryExceptionHTTP e) {
            System.out.println(service + " is DOWN");
        } finally {
            qe.close();
        } // end try/catch/finally
        
        query1 = getMovieQuery("L-Sl-s");
    	
        qe = QueryExecutionFactory.sparqlService(service, query1);
       
        try {
        	ResultSet results = qe.execSelect() ;
            for ( ; results.hasNext() ; )
            {
                QuerySolution soln = results.next() ;
                RDFNode it = soln.get("film");
                String s = soln.getLiteral("id").toString();
                moviehashmap.put(s.substring(0, s.length() - 42), it.toString());
                count++;
            }
        } catch (QueryExceptionHTTP e) {
            System.out.println(service + " is DOWN");
        } finally {
            qe.close();
        } // end try/catch/finally
        
        query1 = getMovieQuery("S-Zs-z");
    	
        qe = QueryExecutionFactory.sparqlService(service, query1);
     
        try {
        	ResultSet results = qe.execSelect() ;
            for ( ; results.hasNext() ; )
            {
                QuerySolution soln = results.next() ;
                RDFNode it = soln.get("film");
                String s = soln.getLiteral("id").toString();
                moviehashmap.put(s.substring(0, s.length() - 42), it.toString());
                count++;
            }
        } catch (QueryExceptionHTTP e) {
            System.out.println(service + " is DOWN");
        } finally {
            qe.close();
        } // end try/catch/finally
		
        System.out.println(count);
        return moviehashmap;
	}

}
