package io.github.l3wk.spd.loader;

import static org.junit.Assert.*;

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
}
