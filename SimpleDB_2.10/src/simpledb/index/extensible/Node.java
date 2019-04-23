package simpledb.index.extensible;

import simpledb.query.Constant;
import simpledb.record.RID;

public class Node {
	
	private RID rid;
	private Constant dataval;
	
	public Node(Constant dataval, RID rid) {
		this.dataval=dataval;
		this.rid=rid;
	}
	
	public RID getRid() {
		return this.rid;
	}
	
	public Constant getDataval() {
		return this.dataval;
	}
	
	public boolean equals(Node n) {
		return this.dataval.equals(n.getDataval());
	}
	
	public String toString() {
		String ridS = rid.toString();
		String datavalS = dataval.toString();
		return "rid: "+ridS+" data: "+datavalS;
	}
}
