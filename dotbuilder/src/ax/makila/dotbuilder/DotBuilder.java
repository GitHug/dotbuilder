package ax.makila.dotbuilder;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import ax.makila.pair.Pair;

/**
 * This class contains all the essentials for building a basic dot file. The primary functionality is
 * creating nodes and linking them as well as producing an output dot file as well as. 
 * @author fredrik
 */
public class DotBuilder {
	//Builds the string for the output file
	private StringBuilder sb = new StringBuilder();
	//Contains all the nodes
	private List<DotNode> nodes = new ArrayList<DotNode>();
	//The default file name
	private String fileName = "out";
	//The file suffix to be appended
	private final String fileSuffix = ".gv";
	//The output file
	private File file;
	//A counter to give each node a unique ID
	private AtomicInteger idCounter = new AtomicInteger();
	//If a node has the same label as another node, treat them as the same node if this parameter is true
	private final boolean enableDuplicatesEquals;
	//Contains all the links
	private final List<Pair<DotNode, DotNode>> links = new ArrayList<Pair<DotNode, DotNode>>();
	//Sets whether the edges should have no directions
	private boolean nonDirectional = false;
	//Newline
	private final String NEWLINE = System.getProperty("line.separator");
	//The default filename of the graph
	private String output = "output.png";
	//if true, opens the graph in the default window
	private boolean open = false;
	
	/**
	 * Constructor. Uses the default fileName "out.gv" as the output file
	 */
	public DotBuilder() {
		enableDuplicatesEquals = false;
		file = new File(fileName + fileSuffix);
		sb.append("digraph G {");
		sb.append(NEWLINE);
	}
	
	
	/**
	 * Constructor. Allows for specification of the fileName to be used. No suffix is needed for the
	 * file name.
	 * @param fileName The name of the output file
	 */
	public DotBuilder(String fileName) {
		enableDuplicatesEquals = false;
		this.fileName = fileName;
		file = new File(fileName + fileSuffix);
		sb.append("digraph G {");
		sb.append(NEWLINE);
	}
	
	/**
	 * Constructor.If duplicateEquals is <tt>true</tt> then nodes with the same label are 
	 * considered equal.
	 * @param fileName The name of the output file
	 * @param duplicateEquals Set as <tt>true</tt> if nodes with the same label are equal, else <tt>false</tt>
	 */
	public DotBuilder(boolean duplicateEquals) {
		enableDuplicatesEquals = duplicateEquals;
		file = new File(fileName + fileSuffix);
		sb.append("digraph G {");
		sb.append(NEWLINE);
	}
	
	/**
	 * Constructor. If <tt>nonDirectional</tt>
	 * is set to true, then the edges won't have any direction. Default is false. If duplicateEquals is <tt>true</tt> then nodes with the same label are 
	 * considered equal.
	 * @param fileName The name of the output file
	 * @param nonDirectional <tt>true</tt> if the edges should have no direction. Default <tt>false</tt>
	 * @param duplicateEquals Set as <tt>true</tt> if nodes with the same label are equal, else <tt>false</tt>
	 */
	
	public DotBuilder(boolean nonDirectional, boolean duplicateEquals) {
		enableDuplicatesEquals = duplicateEquals;
		file = new File(fileName + fileSuffix);
		sb.append("digraph G {");
		sb.append(NEWLINE);
		this.nonDirectional = nonDirectional;
	}
	
	/**
	 * Constructor. Allows for specification of the fileName to be used. No suffix is needed for the
	 * file name. If duplicateEquals is <tt>true</tt> then nodes with the same label are 
	 * considered equal.
	 * @param fileName The name of the output file
	 * @param duplicateEquals Set as <tt>true</tt> if nodes with the same label are equal, else <tt>false</tt>
	 */
	public DotBuilder(String fileName, boolean duplicateEquals) {
		enableDuplicatesEquals = duplicateEquals;
		this.fileName = fileName;
		file = new File(fileName + fileSuffix);
		sb.append("digraph G {");
		sb.append(NEWLINE);
	}
	
	
	/**
	 * Constructor. Allows for specification of the fileName to be used. No suffix is needed for the
	 * file name. If <tt>nonDirectional</tt> is set to true, then the edges won't have any direction. 
	 * Default is false. If duplicateEquals is <tt>true</tt> then nodes with the same label are 
	 * considered equal.
	 * @param fileName The name of the output file
	 * @param nonDirectional <tt>true</tt> if the edges should have no direction. Default <tt>false</tt>
	 * @param duplicateEquals Set as <tt>true</tt> if nodes with the same label are equal, else <tt>false</tt>
	 */
	public DotBuilder(String fileName, boolean nonDirectional, boolean duplicateEquals) {
		this.enableDuplicatesEquals = duplicateEquals;
		this.fileName = fileName;
		file = new File(fileName + fileSuffix);
		sb.append("digraph G {");
		sb.append(NEWLINE);
		this.nonDirectional = nonDirectional;
	}
	
	/**
	 * Getters for the file name.
	 * @return the filename of the output file.
	 */
	public String getFileName() {
		return fileName;
	}
	
	/**
	 * Returns whether the current links are directional or not.
	 * @return <tt>true</tt> if the links are non-directional, else false.
	 */
	public boolean isNonDirectional() {
		return nonDirectional;
	}
	
	/**
	 * Sets whether the next links should be directional or not.
	 * @param enable Set as <tt>true</tt> to have non-directional links, <tt>false</tt> for directional.
	 */
	public void setNonDirectional(boolean enable) {
		nonDirectional = enable;
	}
	/**
	 * Setter for the name of the ouput file.
	 * @param newFileName the new name of the output file.
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
		file = new File(fileName + fileSuffix);
	}
	/**
	 * Returns <tt>true</tt> if nodes with same labels should be treated as equal, else <tt>false</tt>
	 * @return
	 */
	public boolean isDuplicatesEqual() {
		return enableDuplicatesEquals;
	}
	
	/**
	 * Adds a new node to the list of nodes, but only if this node doesn't already exist
	 * @param node The node to be added to the list of nodes
	 */
	private void testAndAddNode(DotNode node) {
		try {
			if(nodes.contains(node)) {;
				throw new Exception("Node with id " + node.id + " already exists!");
			}
			else {
				nodes.add(node);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	/**
	 * Creates and returns a new node with the label <tt>label</tt> and adds it to
	 * the list of nodes.
	 * @param label The label of the new node 
	 * @return The created node
	 */
	public DotNode newNode(String label) {
		//if true, then A.label1 == B.label1 else A.label1 != B.label1
		if(enableDuplicatesEquals) {
			for(DotNode node : nodes) {
				//if the node already exists, return the node
				if(node.label.equals(label)) {
					return node;
				}
			}
		}
		DotNode node = new DotNode(label);
		testAndAddNode(node);
		
		return node;
	}
	
	/**
	 * Removes a node. If the node is already linked, the node cannot be removed 
	 * and a <tt>RemoveDotNodeException</tt> is thrown.
	 * @param node The node to be removed
	 * @throws RemoveDotNodeException Thrown if <tt>node</tt> is linked.
	 */
	public void removeNode(DotNode node) throws RemoveDotNodeException {
		if(node != null) {
			if(node.linked) {
				throw new RemoveDotNodeException("Node is linked!");
			}
			else {
				nodes.remove(node);
			}
		}
	}
	
	/**
	 * Returns <tt>true</tt> if <tt>node</tt> exists.
	 * @param node The node to be checked if exists.
	 * @return <tt>true</tt> if <tt>node</tt> exists.
	 */
	public boolean nodeExists(DotNode node) {
		return nodes.contains(node);
	}

	
	/**
	 * Creates a link from node <tt>from</tt> to node <tt>to</tt>.
	 * 
	 * @param from The departure node
	 * @param to The destination node
	 */
	public void link(DotNode from, DotNode to) {
		from.linked = true;
		to.linked = true;
		sb.append(from.id + " -> " + to.id + " ");
		if(nonDirectional) {
			sb.append("[dir=none]");
		}
		sb.append(NEWLINE);
		links.add(new Pair<DotNode, DotNode>(from, to));
	}
	
	/**
	 * Creates a link from node <tt>from</tt> to node <tt>to</tt>. The <tt>color</tt> specifies the 
	 * edge color. 
	 * 
	 * @param from The departure node.
	 * @param to The destination node.
	 * @param color The color of the edge.
	 */
	public void link(DotNode from, DotNode to, String color) {
		from.linked = true;
		to.linked = true;
		sb.append(from.id + " -> " + to.id);
		sb.append(" [");
		if(nonDirectional) {
			sb.append("dir=none, ");
		}
		sb.append("color=\"" + color
				+ "\"]");
		sb.append(NEWLINE);
		links.add(new Pair<DotNode, DotNode>(from, to));
	}
	
	/**
	 * Creates a link from node <tt>from</tt> to node <tt>to</tt>. The <tt>penwidth</tt> specifies the 
	 * width of the edge. Default is 1. 
	 * 
	 * @param from The departure node.
	 * @param to The destination node.
	 * @param penwidth The width of the edge.
	 */
	public void link(DotNode from, DotNode to, int penwidth) {
		from.linked = true;
		to.linked = true;
		sb.append(from.id + " -> " + to.id);
		sb.append(" [");
		if(nonDirectional) {
			sb.append("dir=none, ");
		}
		sb.append("penwidth=" + penwidth + "]");
		sb.append(NEWLINE);
		links.add(new Pair<DotNode, DotNode>(from, to));
	}

	/**
	 * Returns <tt>true</tt> if a link exists between <tt>node0</tt> and <tt>node1</tt>.  
	 * @param node0 The first node in the link
	 * @param node1 The second node in the link
	 * @return <tt>true</tt> if a link exists between <tt>node0</tt> and <tt>node1</tt>.
	 */
	public boolean linkExists(DotNode node0, DotNode node1) {
		Pair<DotNode, DotNode> pair = new Pair<DotNode, DotNode>(node0, node1);
		return links.contains(pair);
	}
	
	/**
	 * Creates a link from node <tt>from</tt> to node <tt>to</tt>. The <tt>color</tt>
	 * indicates the color of the edge. The <tt>color</tt> specifies the 
	 * edge color. The <tt>penwidth</tt> specifies the width of the edge, default is 1. 
	 * 
	 * @param from The departure node
	 * @param to The destination node
	 * @param color 
	 */
	public void link(DotNode from, DotNode to, String color,
			int penwidth) {
		from.linked = true;
		to.linked = true;
		sb.append(from.id + " -> " + to.id);
		sb.append(" [");
		if(nonDirectional) {
			sb.append("dir=none, ");
		}
		sb.append("color=\"" + color+ "\"" + ", penwidth=" + penwidth + "]");
		sb.append(NEWLINE);
		links.add(new Pair<DotNode, DotNode>(from, to));
	}
	
	/**
	 * Labels all the nodes in the list of nodes so the resulting graphical 
	 * representation of the output file will show the nodes' labels instead of its
	 * ID. The string is of the form "node [label = "label"]" and is added to the
	 * output string
	 */
	private void labelize() {
		for(DotNode n : nodes) {
			if(n.linked) {
				sb.append(n.id + " [label = \"" + n.label + "\"]\n");
			}
		}
	}
	
	/**
	 * Finalizes the output and writes the result to the file. Also closes the
	 * output stream. Should be the last call to the DotWriter object.
	 */
	public void finalize() {
		labelize();
		sb.append("}");
		try {
			if(!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(sb.toString());
			bw.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void createGraph() {
		String[] cmd = {"sfdp", "-Tpng", "-Goverlap=false", "-o", output, file.getName(), "-Gsplines=true"};
		try {
			Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(open) {
			if(Desktop.isDesktopSupported()) {
				Desktop desktop = Desktop.getDesktop();
				try {
					desktop.open(new File(output));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void setOutputFileName(String output) {
		output = output + ".png";
	}
	
	public void setOpen(boolean open) {
		this.open = open;
	}
	
	public void setOutputName(String output) {
		
	}
	
	/**
	 * Class for creating nodes in the graph. The constructor is private and should
	 * only be accessed by DotWriter.newNode(String)
	 * @author fredrik
	 */
	public class DotNode {
		/**
		 * The label of the node
		 */
		public final String label;
		/**
		 * The unique ID of the node
		 */
		public final int id;
		/**
		 * linked is set as true if this node has an outgoing or ingoing link to another node
		 */
		public boolean linked = false;
		
		/**
		 * Constructor. Adds a unique label to each node created. Private as it is
		 * only accessed through DotWriter.newNode(String) 
		 * @param label The label of the node
		 */
		private DotNode(String label) {
			this.label = label;
			id = idCounter.getAndIncrement();
		}
		
		@Override
		public int hashCode() {
	        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
	            append(label).
	            append(id).
	            toHashCode();
	    }

		@Override
		public boolean equals(Object obj) {
			if(obj == null) {
				return false;
			}
			else if(getClass() != obj.getClass()) {
				return false;
			}
			DotNode node = (DotNode) obj;
			
	        return new EqualsBuilder().
	            append(label, node.label).
	            append(id, node.id).
	            isEquals();
		}
		
		@Override
		public String toString() {
			return label;
		}
	}
	
	public class RemoveDotNodeException extends Exception {
		/**
		 * Serial version id
		 */
		private static final long serialVersionUID = -5042582407776006135L;

		public RemoveDotNodeException() {
			super();
		}
		
		public RemoveDotNodeException(String message) {
			super(message);
		}
		
	}
	
}
