/*
 * notes:
 * do it with binary
 * only use 20,000 records not 100,000
 * make a max bucket size
 * 
 */

package simpledb.index.extensible;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import simpledb.file.Block;
import simpledb.index.Index;
import simpledb.query.Constant;
import simpledb.query.TableScan;
import simpledb.record.RID;
import simpledb.record.Schema;
import simpledb.record.TableInfo;
import simpledb.tx.Transaction;

/**
 * ExtensibleHash class
 * 
 * @author Andrew Nolan
 * @author Jackson Powell
 *
 *         Implementation of an extensible hash for part 2 of project 2.
 */

//change insert, constructor, and before first

public class ExtensibleHash implements Index {

	public static int NUM_BUCKETS = 2; // cs4432-project2: start with 2 buckets
	private String idxname;
	private Schema sch;
	private Transaction tx;
	private Constant searchkey = null;
	private TableScan ts = null;
	private TableInfo ti;
	private static Map<Integer, HashBlock> index=new HashMap<Integer, HashBlock>();;
	public final static int MAX_BUCKET_SIZE = 16;

	/**
	 * cs4432-project2: constructor for ExtensibleHash
	 * 
	 * @param idxname - index name
	 * @param sch     - schema
	 * @param tx      - transaction
	 */
	public ExtensibleHash(String idxname, Schema sch, Transaction tx) {
		this.idxname = idxname;
		this.sch = sch;
		this.tx = tx;
		
		if(index.isEmpty()) {
			index.put(0, new HashBlock(1));
			index.put(1, new HashBlock(1));
		}
	}

	@Override
	public void beforeFirst(Constant searchkey) {

		close();
		this.searchkey = searchkey;
		int bucket = searchkey.hashCode() % NUM_BUCKETS;
		String tblname = idxname + bucket;
		TableInfo ti = new TableInfo(tblname, sch);
		ts = new TableScan(ti, tx);
	}

	@Override
	public boolean next() {
		// TODO Auto-generated method stub
		while (ts.next())
			if (ts.getVal("dataval").equals(searchkey))
				return true;
		return false;
	}

	@Override
	public RID getDataRid() {
		// TODO Auto-generated method stub
		int blknum = ts.getInt("block");
		int id = ts.getInt("id");
		return new RID(blknum, id);
	}

	@Override
	public void insert(Constant dataval, RID datarid) {
		int hash = dataval.hashCode() % NUM_BUCKETS;
		System.out.println(dataval.hashCode() + ", " + hash);
		System.out.println(this);
		HashBlock hb = index.get(hash);
		ArrayList<Node> nodes = hb.getNodes();
		if (nodes.size() > MAX_BUCKET_SIZE) { // If no more room in bucket, split
			// If local depth > global, double directory
			if (hb.getLocalDepth() == globalDepth()) {
				// Copy mapped hash blocks when expanding directory. 
				for(int i = 0; i < NUM_BUCKETS; i++) {
					index.put(i+NUM_BUCKETS, index.get(i));
				}
				NUM_BUCKETS *= 2;
			}
			// Update block for hash that needs to split
			HashBlock newHB = new HashBlock(hb.getLocalDepth() + 1);
			index.put(hash, newHB);
			ArrayList<Node> tempNodes = new ArrayList<Node>();
			tempNodes.addAll(nodes);
			hb.setNodes(new ArrayList<Node>());
			hb.increaseDepth();
			for (Node n : tempNodes) { // Re-insert all values from split block back into the index.
				insert(n.getDataval(), n.getRid());
			}

		} else { // Otherwise just add value.
			nodes.add(new Node(dataval, datarid));
		}
	}

	/**
	 * delete deletes something
	 * 
	 * @param dataval
	 * @param datarid
	 */
	public void delete(Constant dataval, RID datarid) {
		// TODO Auto-generated method stub
		beforeFirst(dataval);
		while (next())
			if (getDataRid().equals(datarid)) {
				ts.delete();
				return;
			}
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		if (ts != null)
			ts.close();
	}

	/**
	 * globalDepth calculates the global depth of the Extensible Hash
	 * 
	 * @return the global depth of the hash as an int
	 */
	public int globalDepth() {
		return (int) (Math.log10(NUM_BUCKETS) / Math.log10(2));
	}
	public String toString() {
		String ret = "Hash\tLocal Depth\tNumber of records\n";
		for(int i = 0; i < NUM_BUCKETS; i++) {
			ret += i + "\t" + index.get(i).toString() + "\n";
		}
		return ret;
	}
}
