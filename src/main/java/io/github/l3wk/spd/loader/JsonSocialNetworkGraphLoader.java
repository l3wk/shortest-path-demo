package io.github.l3wk.spd.loader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.json.JSONObject;

import io.github.l3wk.spd.graph.Edge;
import io.github.l3wk.spd.graph.Graph;
import io.github.l3wk.spd.graph.Vertex;

public class JsonSocialNetworkGraphLoader implements GraphLoader {

	// It is possible to save a few seconds when loading the test data set by using parallel streams and
	// concurrent hash maps, however this incurs additional clean-up costs at the time of JVM shutdown which
	// results in an overall execution time that is about the same as a single-threaded implementation.
	
	private static final String USER_KEY = "user";
	private static final String FRIENDS_KEY = "friends";
	private static final String SKILL_KEY = "skill";
	
	public static final int DEFAULT_SKILL = 0;
	
	private AtomicInteger edgeIdGenerator = new AtomicInteger();
	
	private Map<Integer, Vertex> usersById;
	private Map<Integer, Edge> friendsById;
	private Map<Integer, Integer> skillsByUserId;
	
	@Override
	public Graph load(Stream<String> stream) {
				
		initialise();
		
		if (stream != null) {
			stream.filter(entry -> !entry.isEmpty()).forEach(entry -> loadEntry(entry));
			
			populateEdgeWeights();
		}
				
		return new Graph(usersById, friendsById);
	}
	
	private void initialise() {
		
		usersById = new HashMap<>();
		friendsById = new HashMap<>();
		skillsByUserId = new HashMap<>();
	}
	
	private void loadEntry(String entry) {
		
		loadUser(new JSONObject(entry));
	}
	
	private void loadUser(JSONObject json) {
		 
		if (json.has(USER_KEY)) {
			int userId = json.getInt(USER_KEY);
		
			skillsByUserId.put(userId, loadSkill(json));
			usersById.put(userId, new Vertex(userId, loadFriends(userId, json)));
		}
	}
	
	private int loadSkill(JSONObject json) {
		
		return json.has(SKILL_KEY) ? json.getInt(SKILL_KEY) : DEFAULT_SKILL;
	}
	
	private List<Integer> loadFriends(int userId, JSONObject json) {
		
		if (!json.has(FRIENDS_KEY)) {
			return Collections.<Integer>emptyList();
			
		} else {
			List<Integer> edgeIds = new ArrayList<>();
			List<Integer> friendIds = new ArrayList<>();
			
			json.getJSONArray(FRIENDS_KEY).forEach(id -> friendIds.add((Integer) id));
			
			friendIds.stream().forEach(friendId -> {
				
				Integer edgeId = edgeIdGenerator.getAndIncrement();
				
				edgeIds.add(edgeId);
				friendsById.put(edgeId, new Edge(edgeId, userId, friendId, DEFAULT_SKILL));
			});
			
			return edgeIds;
		}
	}
	
	private void populateEdgeWeights() {
		
		// populate the graph edge weights with the inverse skill of the edge target
		
		OptionalInt maxSkill = skillsByUserId.values().stream().mapToInt(Integer::intValue).max();
		
		if (maxSkill.isPresent()) {
			friendsById.values().forEach(edge -> edge.setWeight(maxSkill.getAsInt() - skillsByUserId.get(edge.getTargetId())));
		}
	}
}
