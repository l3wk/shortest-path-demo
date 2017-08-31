package io.github.l3wk.spd.loader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
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
	
	private AtomicInteger edgeIds = new AtomicInteger();
	
	private Map<Integer, Vertex> usersById;
	private Map<Integer, Edge> friendsById;
	private Map<Integer, Integer> skillsByUser;
	
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
		skillsByUser = new HashMap<>();
	}
	
	private void loadEntry(String entry) {
		
		JSONObject json = new JSONObject(entry);
		
		if (json.has(USER_KEY)) {
			int user = loadUser(json);
			
			skillsByUser.put(user, loadSkill(json));
			
			List<Edge> friends = loadFriends(json).stream()
					.map(friendId -> new Edge(edgeIds.getAndIncrement(), user, friendId, DEFAULT_SKILL))
					.collect(Collectors.toList());
			
			usersById.put(user, new Vertex(user, friends.stream()
					.map(friend -> friend.getId())
					.collect(Collectors.toList())));
			
			friends.forEach(friend -> friendsById.put(friend.getId(), friend));
		}
	}
	
	private int loadUser(JSONObject object) {
		
		return object.getInt(USER_KEY);
	}
	
	private int loadSkill(JSONObject object) {
		
		return object.has(SKILL_KEY) ? object.getInt(SKILL_KEY) : DEFAULT_SKILL;
	}
	
	private List<Integer> loadFriends(JSONObject object) {
		
		if (!object.has(FRIENDS_KEY)) {
			return Collections.<Integer>emptyList();
			
		} else {		
			List<Integer> friends = new ArrayList<>();
			
			object.getJSONArray(FRIENDS_KEY).forEach(id -> friends.add((Integer) id));
			
			return friends;
		}
	}
	
	private void populateEdgeWeights() {
		
		// populate the graph edge weights with the inverse skill of the edge target
		
		OptionalInt maxSkill = skillsByUser.values().stream().mapToInt(Integer::intValue).max();
		
		if (maxSkill.isPresent()) {
			friendsById.values().forEach(edge -> edge.setWeight(maxSkill.getAsInt() - skillsByUser.get(edge.getTargetId())));
		}
	}
}
