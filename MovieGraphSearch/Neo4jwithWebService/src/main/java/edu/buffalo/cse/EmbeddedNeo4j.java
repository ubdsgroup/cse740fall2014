package edu.buffalo.cse;

/**
 * Licensed to Neo Technology under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Neo Technology licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;

public class EmbeddedNeo4j
{
    private static final String DB_PATH = "E:\\UB_Acads\\Seminar\\embedded_graphdb";
    private static final String SAMPLE_DB_PATH = "E:\\UB_Acads\\Seminar\\sample_embedded_graphdb";
    private static final Label MOVIE_LABEL = DynamicLabel.label("Movie");
    private static final String MOVIE_ID = "id";
    private static final String sample_dir = "E:\\UB_Acads\\Seminar\\Movie_Props\\sample_files";
    private static final String final_movie_dir = "E:\\UB_Acads\\Seminar\\Movie_Props\\moviefiles\\moviefiles";
    private static final RelationshipType RELATION = DynamicRelationshipType.withName("RELATED_TO");
    
    private static final String final_edge_file = "E:\\UB_Acads\\Seminar\\All_EdgeMap.properties";
    private static final String sample_edge_file = "E:\\UB_Acads\\Seminar\\sample_edge.properties";
    
    private static final String WEIGHT = "weight";
    
    private Map moveNameMap;
    
    public String greeting;

    public static int count = 0;
    private static Index<Node> indexService;
    
    // START SNIPPET: vars
    GraphDatabaseService graphDb;
    Node firstNode;
    Node secondNode;
    Relationship relationship;
    // END SNIPPET: vars

    // START SNIPPET: createReltype
    private static enum RelTypes implements RelationshipType
    {
        KNOWS
    }
    // END SNIPPET: createReltype

    public static void main( final String[] args )
    {
        EmbeddedNeo4j hello = new EmbeddedNeo4j();
        hello.startUp();
        //hello.moveNameMap = hello.getMovieNameMapping("E:\\UB_Acads\\Seminar\\MovieMap.properties");
        //hello.loadMovieProperties(final_movie_dir);
        //System.out.println("Total count is: " + count);
        //hello.loadEdgeProperties(final_edge_file);
        //hello.createUserDB();
        //hello.createDb();
        //hello.removeData();
        //hello.getUsersFromDb("1210684", "1125221");
        //hello.traverse();
        hello.shutDown();
    }

    void startUp(){
    	 //deleteFileOrDirectory( new File( CONTEXT_DB_PATH ) );
    	 graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
     	try ( Transaction tx = graphDb.beginTx() )
     	{
    	 indexService = graphDb.index().forNodes( "nodes" );
     	}
         registerShutdownHook( graphDb );
    }
    
    HashMap getMovieNameMapping(String fileName){
		Map ldapContent = new HashMap();
		Properties properties = new Properties();
		try(FileInputStream inStream = new FileInputStream(fileName)){
			properties.load(inStream);
		for (Object key : properties.keySet()) {
		   ldapContent.put(key, properties.get(key));
		}
			return (HashMap) ldapContent;
		}
		catch(IOException e){
			return (HashMap) ldapContent;
		}
    }
    void createUserDB(Map propertyMap){
    	//System.out.println("Creating Movies: " +  ++count);
    	count++;
        //graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
        //registerShutdownHook( graphDb );
    	try ( Transaction tx = graphDb.beginTx() )
    	{
//    	    Label label = DynamicLabel.label( "User" );
//
//    	    // Create some users
//    	    for ( int id = 0; id < 100; id++ )
//    	    {
//    	        Node userNode = graphDb.createNode( label );
//    	        userNode.setProperty( "username", "user" + id + "@neo4j.org" );
//    	    }
//    	    System.out.println( "Users created" );
            Node movieNode = null;
            		//indexService.get( MOVIE_ID, propertyMap.get(MOVIE_ID) ).getSingle();
            if ( movieNode == null )
            {
            	movieNode = graphDb.createNode(MOVIE_LABEL);
            	//movieNode.setProperty( NAME_KEY, name );
        		for (Object key : propertyMap.keySet()) {
    				movieNode.setProperty((String)key, propertyMap.get(key));
    			}
        		String id = (String)propertyMap.get("id");
        		
        		System.out.println("id: " + id.substring(1, id.length()-1));
        		
        		String mName = (String)moveNameMap.get(id.substring(1, id.length()-1));
        		mName = mName.substring(28);
        		mName = mName.replaceAll("_", " ");
        		//System.out.println("Movie is: " + mName);
        		movieNode.setProperty("name", mName);
                indexService.add( movieNode, MOVIE_ID, propertyMap.get(MOVIE_ID) );
            }

    		
    	    tx.success();
    	}
    }
    
    void loadMovieProperties(String dirName){
    	File dir = new File(dirName);
    	File[] allFiles = dir.listFiles();
    	for (File file : allFiles) {
    		HashMap pMap = readHashMapFromDisk(file);
    		if(pMap.size()>0){
    			createUserDB(pMap);
    		}
    		
    		//if(count>1000) break;
			//System.out.println(file.getAbsolutePath());
			//String val = (String) readHashMapFromDisk(file).get("category");
			//System.out.println(val);
		}
    	
    }
    
    void loadEdgeProperties(String fileName){
    	Properties properties = new Properties();
		try(FileInputStream inStream = new FileInputStream(fileName)){
			properties.load(inStream);
		for (Object key : properties.keySet()) {
			
			String k = (String) key;
			String value = (String)properties.get(key);
			//System.out.println(value);
			String[] nodeIds = k.split("-");
			Node one = getNode(nodeIds[0]);
			Node two = getNode(nodeIds[1]);
			
			if(one!=null && two!=null){
				try(Transaction tx = graphDb.beginTx()){
					Relationship r = one.createRelationshipTo(two, RELATION);
					String[] values = value.split("\\|");
					double w =0;
					for (int i = 0; i < values.length; i++) {
						//System.out.println("&&&&&" + values[i]);
						String[] eachProp = values[i].split("\\:");
						//System.out.println("***" + eachProp[0]);
						r.setProperty(eachProp[0], eachProp[1]);
							/*
							 * 
							 * star_weight = 0.25 director_weight = 0.175
							 * writer_weight = 0.15 music_weight = 0.10
							 * producer_weight = 0.125 editor_weight = 0.10
							 * camera_weight = 0.10
							 */
							switch (eachProp[0]) {
							case "starring":
								w = w + 0.25;
								break;
							case "director":
								w = w + 0.175;
								break;
							case "writer":
								w = w + 0.15;
								break;
							case "music":
								w = w + 0.10;
								break;
							case "editor":
								w = w + 0.10;
								break;
							case "camera":
								w = w + 0.10;
								break;
							case "producer":
								w = w + 0.125;
								break;

							default:
							}
					}
					r.setProperty(WEIGHT, w);
					
					tx.success();
					//System.out.println("@@@@@@@" + r.getProperty("writer"));
				}
				
			}
		   //ldapContent.put((K)key, (V)properties.get(key));
		}
		}		catch(IOException e){
			//return (HashMap<K, V>) ldapContent;
		}
    	
    }
    
    
    
	public static <K, V> HashMap<K ,V> readHashMapFromDisk(File fileName){
		Map<K, V> ldapContent = new HashMap<K, V>();
		Properties properties = new Properties();
		try(FileInputStream inStream = new FileInputStream(fileName)){
			properties.load(inStream);
		for (Object key : properties.keySet()) {
		   ldapContent.put((K)key, (V)properties.get(key));
		}
			return (HashMap<K, V>) ldapContent;
		}
		catch(IOException e){
			return (HashMap<K, V>) ldapContent;
		}
	}
	
	Node getNode( String id )
    {
		Node node = null;
    	try ( Transaction tx = graphDb.beginTx() )
    	{
    		node = indexService.get( "id", "["+id+"]" ).getSingle();
    		//System.out.println(node.getProperty(MOVIE_ID));
    	}
//        if ( node == null )
//        {
//            node = graphDb.createNode();
//            node.setProperty( NAME_KEY, name );
//            indexService.add( node, NAME_KEY, name );
//        }
        return node;
    }
	
    Map getUsersFromDb(String nodeOne, String nodeTwo){
    	try ( Transaction tx = graphDb.beginTx() )
    	{
    		
    		Map<String, Double> weightMap = new HashMap<String, Double>();
            Node one = getNode(nodeOne);
            System.out.println("Id: " + one.getProperty("name"));
            
            Node two = getNode(nodeTwo);
            System.out.println("Id: " + two.getProperty("name"));
            
            Iterator< Relationship> iter = one.getRelationships(RELATION).iterator();
            Iterator< Relationship> iterTwo = two.getRelationships(RELATION).iterator();

            while(iter.hasNext()){

            	Relationship r = iter.next();
            	Node n = r.getEndNode();
            	if(n.getProperty("id").toString().equals(two.getProperty("id")) || n.getProperty("id").toString().equals(one.getProperty("id"))) continue;
            	String id = (String) n.getProperty("name");
            	//id = id.substring(1, id.length()-1);
            	double weight = (double) r.getProperty(WEIGHT);
            	if(weightMap.containsKey(id)){
            		weight += weightMap.get(id);
            	}
            	
            	weightMap.put(id, weight);

            }
            
            while(iterTwo.hasNext()){
            	
            	Relationship r2 = iterTwo.next();
            	Node n2 = r2.getEndNode();
            	if(n2.getProperty("id").toString().equals(one.getProperty("id")) || n2.getProperty("id").toString().equals(two.getProperty("id"))) continue;
            	String id2 = (String) n2.getProperty("name");
            	double weight2 = (double) r2.getProperty(WEIGHT);
            	if(weightMap.containsKey(id2)){
            		weight2 += weightMap.get(id2);
            	}
            	
            	weightMap.put(id2, weight2);
            }
            
            Map sortedMap = sortByValue(weightMap);
            int count = 0;
            for (Object id: sortedMap.keySet()) {
            	String sid = (String)id;
            	//System.out.println("id: " + sid);
            	if(count>20) break;
//        		String mName = (String)moveNameMap.get(sid.substring(1, sid.length()-1));
//        		mName = mName.substring(28);
//        		mName = mName.replaceAll("_", " ");
				System.out.println("Movie is: " + sid + " and Weight is: " + sortedMap.get(id) );
				count++;
			}
            
            return sortedMap;
            //System.out.println("Total no of edges: " + edgeCOunt);
//            Relationship r = one.getSingleRelationship(RELATION, Direction.BOTH);
//            System.out.println("xxxxxx" + r.getProperty("writer"));
            
//            Iterable<Relationship> iter = one.getRelationships();
//            
//            while(iter.iterator().hasNext()){
//            	Relationship r = iter.iterator().next();
//            	System.out.println("Relationship is:" + r.getProperty("writer"));
//            }
    	}
    }
    
//    private static Node getOrCreateNode( String id )
//    {
//        Node node = indexService.get( NAME_KEY, id ).getSingle();
//        if ( node == null )
//        {
//            node = graphDb.createNode();
//            node.setProperty( NAME_KEY, name );
//            indexService.add( node, NAME_KEY, name );
//        }
//        return node;
//    }
    
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(
			Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(
				map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
    
    void createDb()
    {
        //deleteFileOrDirectory( new File( DB_PATH ) );
        // START SNIPPET: startDb
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
        registerShutdownHook( graphDb );
        // END SNIPPET: startDb

        // START SNIPPET: transaction
        try ( Transaction tx = graphDb.beginTx() )
        {
            // Database operations go here
            // END SNIPPET: transaction
            // START SNIPPET: addData
            firstNode = graphDb.createNode();
            firstNode.setProperty( "message", "Hello, " );
            secondNode = graphDb.createNode();
            secondNode.setProperty( "message", "World!" );

            relationship = firstNode.createRelationshipTo( secondNode, RelTypes.KNOWS );
            relationship.setProperty( "message", "brave Neo4j " );
            // END SNIPPET: addData

            // START SNIPPET: readData
            System.out.print( firstNode.getProperty( "message" ) );
            System.out.print( relationship.getProperty( "message" ) );
            System.out.print( secondNode.getProperty( "message" ) );
            // END SNIPPET: readData

            greeting = ( (String) firstNode.getProperty( "message" ) )
                       + ( (String) relationship.getProperty( "message" ) )
                       + ( (String) secondNode.getProperty( "message" ) );

            // START SNIPPET: transaction
            tx.success();
        }
        // END SNIPPET: transaction
    }

    void removeData()
    {
        try ( Transaction tx = graphDb.beginTx() )
        {
            // START SNIPPET: removingData
            // let's remove the data
            firstNode.getSingleRelationship( RelTypes.KNOWS, Direction.OUTGOING ).delete();
            firstNode.delete();
            secondNode.delete();
            // END SNIPPET: removingData

            tx.success();
        }
    }

//    void traverse(){
//    	TraversalExample example = new TraversalExample( graphDb, RELATION);
//    	example.run(getNode("11604017"));
//    	
//    }
    void shutDown()
    {
        System.out.println();
        System.out.println( "Shutting down database ..." );
        // START SNIPPET: shutdownServer
        graphDb.shutdown();
        // END SNIPPET: shutdownServer
    }

    // START SNIPPET: shutdownHook
    private static void registerShutdownHook( final GraphDatabaseService graphDb )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                graphDb.shutdown();
            }
        } );
    }
    // END SNIPPET: shutdownHook

    private static void deleteFileOrDirectory( File file )
    {
        if ( file.exists() )
        {
            if ( file.isDirectory() )
            {
                for ( File child : file.listFiles() )
                {
                    deleteFileOrDirectory( child );
                }
            }
            file.delete();
        }
    }
}