package simpledb.index.extensible;

import simpledb.query.Constant;
import simpledb.record.RID;


/**
 * CS4432-project2: This is the node class, it represents data stored in the buckets of the extensible hash
 * @author Jackson Powell
 * @author Andrew Nolan
 *
 */
public class Node {
	
	private RID rid;
	private Constant dataval;
	
	/**
	 * Constructor for a node
	 * @param dataval - the data to store
	 * @param rid - the id
	 */
	public Node(Constant dataval, RID rid) {
		this.dataval=dataval;
		this.rid=rid;
	}
	
	/**
	 * getter for rid
	 * @return the RID
	 */
	public RID getRid() {
		return this.rid;
	}
	
	/**
	 * getter for dataval
	 * @return the dataval
	 */
	public Constant getDataval() {
		return this.dataval;
	}
	
	/**
	 * checks if two nodes are equal
	 * @param n
	 * @return true if the nodes are equal
	 */
	public boolean equals(Node n) {
		return this.dataval.equals(n.getDataval());
	}
	
	/**
	 * converts the node to a string
	 * overridding the default toString method
	 */
	public String toString() {
		String ridS = rid.toString();
		String datavalS = dataval.toString();
		return "rid: "+ridS+" data: "+datavalS;
	}
}
