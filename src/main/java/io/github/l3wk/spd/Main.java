package io.github.l3wk.spd;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import io.github.l3wk.spd.graph.Graph;
import io.github.l3wk.spd.loader.JsonSocialNetworkGraphLoader;
import io.github.l3wk.spd.search.DijkstraSearchService;
import io.github.l3wk.spd.search.SearchService;

public class Main {
	
	private static final String SOURCE_ID_ARG = "sourceId";
	private static final String TARGET_ID_ARG = "targetId";
	private static final String DATA_FILE_ARG = "dataFile";
	
	// mvn -q exec:java -Dexec.args="--sourceId=<source_id> --targetId=<target_id> --dataFile=<data.json>"
	
	public static void main( String[] args ) {
		
		CommandLineParser parser = new DefaultParser();
		
		Options options = new Options();
		
		options.addOption(Option.builder().longOpt(SOURCE_ID_ARG).hasArg().required().build());
		options.addOption(Option.builder().longOpt(TARGET_ID_ARG).hasArg().required().build());
		options.addOption(Option.builder().longOpt(DATA_FILE_ARG).hasArg().required().build());
				
		try {
			CommandLine command = parser.parse(options, args);
			
			Integer sourceId = Integer.valueOf(command.getOptionValue(SOURCE_ID_ARG));
			Integer targetId = Integer.valueOf(command.getOptionValue(TARGET_ID_ARG));
			
			searchGraph(loadGraph(command.getOptionValue(DATA_FILE_ARG)), sourceId, targetId);
			
		} catch (Exception e) {
			System.err.println(e.getClass().getSimpleName() + ": " + e.getMessage());
		}
	}
	
	private static Graph loadGraph(String filepath) throws IOException {
	
		System.out.println("Loading data set: " + filepath);
		
		Graph graph = new JsonSocialNetworkGraphLoader().load(Files.lines(Paths.get(filepath)));
		
		if (graph != null) {
			System.out.println(" * Loaded graph with " + graph.getVertexes().size() + " vertexes and " + graph.getEdges().size() + " edges.");
		}
		
		return graph;
	}
	
	private static void searchGraph(Graph graph, Integer sourceId, Integer targetId) {
		
		System.out.println("Finding path between " + sourceId + " and " + targetId + ":");
		
		SearchService searchService = new DijkstraSearchService(graph);
		
		System.out.println(" * Result: " + searchService.findShortestPath(graph.getVertex(sourceId), graph.getVertex(targetId)));
	}
}
