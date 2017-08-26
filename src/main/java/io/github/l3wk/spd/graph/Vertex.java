package io.github.l3wk.spd.graph;

import java.util.List;
import java.util.Objects;

public class Vertex {

	private Integer id;
	private List<Integer> edges;
	
	public Vertex(Integer id, List<Integer> edges) {
		
		this.id = id;
		this.edges = edges;
	}
	
	public Integer getId() {
		return id;
	}
	
	public List<Integer> getEdges() {
		return edges;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.id);
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			
			Vertex vertex = (Vertex) obj;
			
			return Objects.equals(this.id, vertex.id);
		}
	}
	
	@Override
	public String toString() {
		return id.toString();
	}
}
