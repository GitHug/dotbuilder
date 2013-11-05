package ax.makila.dotbuilder.test;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ax.makila.dotbuilder.DotBuilder;
import ax.makila.dotbuilder.DotBuilder.DotNode;
import ax.makila.dotbuilder.DotBuilder.RemoveDotNodeException;

public class DotBuilderTest {
	String fileName = "out.gv";
	File file = new File(fileName);

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
		if(file.exists()) {
			file.delete();
		}
		
	}
	
	@Test
	public void testCreateGraph() {
		DotBuilder dot = new DotBuilder(true);
		DotNode n0 = dot.newNode("a");
		DotNode n1 = dot.newNode("b");
		DotNode n2 = dot.newNode("c");
		DotNode n3 = dot.newNode("d");
		DotNode n4 = dot.newNode("f");
		
		DotNode n5 = dot.newNode("g");
		DotNode n6 = dot.newNode("h");
		DotNode n7 = dot.newNode("i");
		DotNode n8 = dot.newNode("j");
		DotNode n9 = dot.newNode("k");
		DotNode n10 = dot.newNode("l");
		
		
		dot.link(n0, n1);
		dot.link(n0, n2);
		dot.link(n0, n3);
		dot.link(n0, n4);
		dot.link(n0, n5);
		dot.link(n0, n6);
		dot.link(n0, n7);
		dot.link(n0, n8);
		dot.link(n0, n9);
		dot.link(n0, n10);

		dot.link(n1, n2);
		dot.link(n2, n3);
		dot.link(n3, n4);
		dot.link(n4, n5);
		dot.link(n5, n6);
		dot.link(n6, n7);
		dot.link(n7, n8);
		dot.link(n8, n9);
		dot.link(n9, n10);
		dot.link(n10, n1);
		
		
		
		dot.finalize();
		
		assertTrue(file.exists());
		
		dot.setOpen(true);
		dot.createGraph();
		
	}

	@Test
	public void testFinalize() {
		DotBuilder dot = new DotBuilder();
		DotNode n0 = dot.newNode("a");
		DotNode n1 = dot.newNode("b");
		DotNode n2 = dot.newNode("c");
		DotNode n3 = dot.newNode("d");
		
		dot.link(n0, n1);
		dot.link(n0, n2);
		dot.link(n0, n3);
		
		dot.link(n1, n2);
		dot.link(n1, n3);
		
		dot.link(n2, n3);
		
		dot.finalize();
		
		assertTrue(file.exists());
	}

	@Test
	public void testDotBuilder() {
		DotBuilder dot = new DotBuilder();
		assertNotNull(dot.getFileName());
		assertFalse(dot.isNonDirectional());
		assertFalse(dot.isDuplicatesEqual());
		
		dot = new DotBuilder("testfile");
		assertEquals("testfile", dot.getFileName());
		assertFalse(dot.isNonDirectional());
		assertFalse(dot.isDuplicatesEqual());
		
		dot = new DotBuilder(true);
		assertNotNull(dot.getFileName());
		assertFalse(dot.isNonDirectional());
		assertTrue(dot.isDuplicatesEqual());
		
		dot = new DotBuilder(true, true);
		assertNotNull(dot.getFileName());
		assertTrue(dot.isNonDirectional());
		assertTrue(dot.isDuplicatesEqual());
		
		dot = new DotBuilder("testfile", true);
		assertEquals("testfile", dot.getFileName());
		assertFalse(dot.isNonDirectional());
		assertTrue(dot.isDuplicatesEqual());
		
		dot = new DotBuilder("testfile", true, true);
		assertEquals("testfile", dot.getFileName());
		assertTrue(dot.isNonDirectional());
		assertTrue(dot.isDuplicatesEqual());
	}

	@Test
	public void testNewNode() {
		DotBuilder dot = new DotBuilder();
		DotNode node = dot.newNode("test");
		assertEquals(node.label, "test");
		assertTrue(node.id == 0);
		assertFalse(node.linked);
		
		DotNode node2 = dot.newNode("test");
		assertTrue(node2.id == 1);
		
	}

	@Test
	public void testRemoveNode() {
		DotBuilder dot = new DotBuilder();
		DotNode node = dot.newNode("test");
		assertTrue(dot.nodeExists(node));
		try {
			dot.removeNode(node);
		} catch (RemoveDotNodeException e) {
			e.printStackTrace();
			assertEquals("NOT EQUAL", e.getMessage());	
		}
		assertFalse(dot.nodeExists(node));
		
		node = dot.newNode("test2");
		DotNode node2 = dot.newNode("test2");
		dot.link(node,  node2);
		try {
			dot.removeNode(node2);
		} catch(RemoveDotNodeException e) {
			assertTrue(true);	
		}
		
		try {
			dot.removeNode(null);
			//Nothing should happen
		} catch (RemoveDotNodeException e) {
			e.printStackTrace();
			assertFalse(true);
		}
		
	}

	@Test
	public void testLink() {
		DotBuilder dot = new DotBuilder();
		DotNode from = dot.newNode("from");
		DotNode to = dot.newNode("to");
		dot.link(from, to);
		assertTrue(from.linked);
		assertTrue(to.linked);
		assertTrue(dot.linkExists(from, to));	
	}
	
	@Test
	public void testGetFileName() {
		DotBuilder dot = new DotBuilder();
		String fileName = dot.getFileName();
		dot.setFileName("test");
		assertFalse(fileName.equals(dot.getFileName()));
	}
	
	@Test
	public void testSetFileName() {
		DotBuilder dot = new DotBuilder();
		String fileName = dot.getFileName();
		dot.setFileName("test");
		assertFalse(fileName.equals(dot.getFileName()));
		
		dot.setFileName(null);
		assertFalse(fileName.equals(dot.getFileName()));
	}
	
	@Test
	public void testIsDirectional() {
		DotBuilder dot = new DotBuilder();
		assertFalse(dot.isNonDirectional());
		
		dot.setNonDirectional(true);
		assertTrue(dot.isNonDirectional());
	}
	
	@Test
	public void testSetDirectional() {
		DotBuilder dot = new DotBuilder();
		dot.setNonDirectional(true);
		assertTrue(dot.isNonDirectional());
		dot.setNonDirectional(false);
		assertFalse(dot.isNonDirectional());
	}
	
	@Test
	public void testLinkExists() {
		DotBuilder dot = new DotBuilder();
		DotNode n0 = dot.newNode("n0");
		DotNode n1 = dot.newNode("n1");
		DotNode n2 = dot.newNode("n2");
		dot.link(n0, n1);
		assertTrue(dot.linkExists(n0, n1));
		assertTrue(dot.linkExists(n1, n0));
		assertFalse(dot.linkExists(n0, n2));
		assertFalse(dot.linkExists(null, n0));
		
	}

}
