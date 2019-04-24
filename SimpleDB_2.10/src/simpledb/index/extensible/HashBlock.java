package simpledb.index.extensible;

import java.util.ArrayList;

import simpledb.file.Block;
import simpledb.record.TableInfo;
import simpledb.tx.Transaction;
/**
 * cs4432-Project2: Created class to manage bucket information for extensible hash.
 * @author Jackson Powell
 * @author Andrew Nolan
 *
 */
public class HashBlock {
	
	private ArrayList<Node> nodes;
	private int localDepth;
	
	public HashBlock(int localDepth) {
		this.localDepth=localDepth;
		nodes = new ArrayList<Node>();
	}
	/** Increases the local depth by 1, for the bucket when splitting.
	 */
	public void increaseDepth() {
		this.localDepth++;
	}
	/**
	 * Returns the local depth for this hash bucket.
	 * @return the local depth for this hash bucket.
	 */
	public int getLocalDepth() {
		return this.localDepth;
	}
	/**
	 * Returns the list of index keys in this bucket.
	 * @return the list of index keys in this bucket.
	 */
	public ArrayList<Node> getNodes(){
		return this.nodes;
	}
	/**
	 * Returns a string formatted for printing out information of the entire extensible hash index.
	 */
	public String toString() {
		String ret = "" + localDepth + "\t" + nodes.size();
		return ret;
	}
	/**
	 * Sets the list of index keys to newNodes. 
	 * @param newNodes The new list of index keys for this hash bucket.
	 */
	public void setNodes(ArrayList<Node> newNodes) {
		nodes = newNodes;
	}
}
