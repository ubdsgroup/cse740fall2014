package dbpedia.runner;

public class AllRunner {

	public static void main(String[] args) {
		//Get the List of Movies - MovieMap.properties
		MainMovies.getMovies("MovieMap");
		System.out.println("List of Movies Downloaded from DB pedia");
		//Save all the Movies into Files - dir/moviefiles
		MainRelations.getMovieFiles("MovieMap", "moviefiles");
		System.out.println("Each Movie Details Downloaded from DB pedia stored in directory - moviefiles ");
		//Parse through the Directory of Files - to Create EdgeMap.properties
		ExtractEdges.getEdgesfromMovies("D:\\Workspaces\\BackupWorkspace\\jena-examples\\moviefiles", "EdgeMap");
		System.out.println("Edges extracted from moviefiles stored in EdgeMap");
	}

}
