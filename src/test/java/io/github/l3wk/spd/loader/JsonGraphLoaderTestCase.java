package io.github.l3wk.spd.loader;

import static org.junit.Assert.*;

import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

public class JsonGraphLoaderTestCase {

	private GraphLoader loader;
	
	@Before
	public void setUp() throws Exception {
		
		loader = new JsonGraphLoader();
	}

	@Test
	public void testLoad_nullStream() {
		
		assertNull(loader.load(null));
	}
	
	@Test
	public void testLoad_emptyStream() {
		
		assertNull(loader.load(Stream.empty()));
	}
}
