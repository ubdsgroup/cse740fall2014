package edu.buffalo.cse;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MovieController {

	@RequestMapping(value = "/movie", method = RequestMethod.GET)
	public Map<String, Double> getSimilarMovies(
			@RequestParam(value = "movie1", required = true) String movieOne, 
			@RequestParam(value = "movie2", required = true) String movieTwo, 
			HttpServletRequest request,
			HttpServletResponse response){
		
		System.out.println(movieOne + movieTwo);
		EmbeddedNeo4j e = new EmbeddedNeo4j();
		e.startUp();
		Map m = e.getUsersFromDb(movieOne, movieTwo);
		e.shutDown();
		return m;
		 
//		List<String> l = new LinkedList<String>();
//		l.add(movieOne);
//		l.add(movieTwo);
//		return l;
	}
}
