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

	private static final String USER_KEY = "user";
	private static final String FRIENDS_KEY = "friends";
	private static final String SKILL_KEY = "skill";
	
	public static final int DEFAULT_SKILL = 0;
	
	private AtomicInteger edgeIds = new AtomicInteger();
	
	private List<Vertex> vertexes;
	private List<Edge> edges;
	private Map<Integer, Integer> skillByUser;
	
	@Override
	public Graph load(Stream<String> stream) {
		
		initialise();
		
		if (stream != null) {
			stream.filter(entry -> !entry.isEmpty()).forEach(entry -> {
								
				JSONObject json = new JSONObject(entry);
				
				if (json.has(USER_KEY)) {
					int user = loadUser(json);
					
					skillByUser.put(user, loadSkill(json));
					
					List<Edge> friends = loadFriends(json).stream()
							.map(friendId -> new Edge(edgeIds.getAndIncrement(), user, friendId, DEFAULT_SKILL))
							.collect(Collectors.toList());
					
					vertexes.add(new Vertex(user, friends.stream().map(friend -> friend.getId()).collect(Collectors.toList())));
					edges.addAll(friends);
				}
			});
			
			populateEdgeWeights();
		}
		
		return new Graph(vertexes, edges);
	}
	
	private void initialise() {
		
		vertexes = new ArrayList<>();
		edges = new ArrayList<>();
		skillByUser = new HashMap<>();
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
		
		OptionalInt maxSkill = skillByUser.values().stream().mapToInt(Integer::intValue).max();
		
		if (maxSkill.isPresent()) {
			edges.forEach(edge -> edge.setWeight(maxSkill.getAsInt() - skillByUser.get(edge.getTargetId())));
		}
	}
}
