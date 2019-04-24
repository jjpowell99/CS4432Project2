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

/** cs4432-Project2: Created class to implement extensible hash indexing.
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
	private static Map<Integer, HashBlock> index=new HashMap<Integer, HashBlock>(); // Map for hashing search keys into hash buckets.
	public final static int MAX_BUCKET_SIZE = 64; // Max number of keys per hash bucket.

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

	/**
	 * Positions the index before the first index record
	 * having the specified search key.
	 * The method hashes the search key to determine the bucket,
	 * and then opens a table scan on the file
	 * corresponding to the bucket.
	 * The table scan for the previous bucket (if any) is closed.
	 * @see simpledb.index.Index#beforeFirst(simpledb.query.Constant)
	 */
	public void beforeFirst(Constant searchkey) {
		close();
		this.searchkey = searchkey;
		int bucket = searchkey.hashCode() % NUM_BUCKETS;
		String tblname = idxname + bucket;
		TableInfo ti = new TableInfo(tblname, sch);
		ts = new TableScan(ti, tx);
	}

	/**
	 * Moves to the next record having the search key.
	 * The method loops through the table scan for the bucket,
	 * looking for a matching record, and returning false
	 * if there are no more such records.
	 * @see simpledb.index.Index#next()
	 */
	public boolean next() {
		// TODO Auto-generated method stub
		while (ts.next())
			if (ts.getVal("dataval").equals(searchkey))
				return true;
		return false;
	}

	/**
	 * Retrieves the dataRID from the current record
	 * in the table scan for the bucket.
	 * @see simpledb.index.Index#getDataRid()
	 */
	public RID getDataRid() {
		// TODO Auto-generated method stub
		int blknum = ts.getInt("block");
		int id = ts.getInt("id");
		return new RID(blknum, id);
	}

	@Override
	/** 
	 * Inserts an index record having the specified
	 * dataval and dataRID values into the HashBlock based on dataval's hashcode.
	 * @param dataval the dataval in the new index record.
	 * @param datarid the dataRID in the new index record.
	 */
	public void insert(Constant dataval, RID datarid) {
		int hash = dataval.hashCode() % NUM_BUCKETS;
		System.out.println(dataval + ", " + hash);
		//System.out.println(this); // Print the state of the hash index, before inserting the above printed dataval.
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
	 * Deletes the index key with datarid RID
	 * 
	 * @param dataval Dataval of the index key to delete.
	 * @param datarid RID of the index key to delete.
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
	/**
	 * Returns a string describing the state of the extensible hash index by reporting the global depth 
	 * as well as the local depth and number of index records in each hash bucket. Some index records may 
	 * be reported twice for buckets with a local depth lower than the global depth.
	 */
	public String toString() {
		String ret = "Hash\tLocal Depth\tNumber of records\tGlobal Depth = " + globalDepth() +"\n";
		for(int i = 0; i < NUM_BUCKETS; i++) {
			ret += i + "\t\t" + index.get(i).toString() + "\n";
		}
		return ret;
	}
}
