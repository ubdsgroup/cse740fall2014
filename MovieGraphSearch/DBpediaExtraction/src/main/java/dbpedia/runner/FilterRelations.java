package dbpedia.runner;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.sparql.engine.http.QueryExceptionHTTP;

public class FilterRelations {
	public static String service = "http://dbpedia.org/sparql";
	public static void main(String[] args) {
		String query = "";
		QueryExecution qe = QueryExecutionFactory.sparqlService(service, query);

		try {
			ResultSet results = qe.execSelect();
			for (; results.hasNext();) {
				QuerySolution soln = results.next();
				
				RDFNode property = soln.get("property");
				
				
				// dbo:abstract
				
				// dbo:cinematography OR dbpedia2:cinematography
				// dbo:director
				// dbo:distributor OR dbpedia2:distributor
				// dbo:editing OR dbpedia2:editing
				// dbo:musicComposer OR dbpedia2:music
				// dbo:producer OR dbpedia2:producer
				// dbo:starring OR dbpedia2:starring
				// dbo:writer OR dbpedia2:writer OR dbpedia2:writers OR dbpedia2:story OR dbpedia2:screenplay
				// dbpedia2:studio
				// dbpedia2:genre
				// dbpedia2:awards
				// dcterms:subject
				
			}
		} catch (QueryExceptionHTTP e) {
			System.out.println(service + " is DOWN");
		} finally {
			qe.close();
		} // end try/catch/finally

	}

}
