package simpledb.index.extensible;

import java.util.ArrayList;

import simpledb.file.Block;
import simpledb.record.TableInfo;
import simpledb.tx.Transaction;

public class HashBlock {
	
	private ArrayList<Node> nodes;
	private int localDepth;
	
	public HashBlock(int localDepth) {
		this.localDepth=localDepth;
		nodes = new ArrayList<Node>();
	}
	
	public void increaseDepth() {
		this.localDepth++;
	}
	
	public int getLocalDepth() {
		return this.localDepth;
	}
	
	public ArrayList<Node> getNodes(){
		return this.nodes;
	}
	public String toString() {
		String ret = "" + localDepth + "\t" + nodes.size();
		return ret;
	}
	public void setNodes(ArrayList<Node> newNodes) {
		nodes = newNodes;
	}
}
