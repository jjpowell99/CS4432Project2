/*
 * notes:
 * do it with binary
 * only use 20,000 records not 100,000
 * make a max bucket size
 * 
 */


package simpledb.index.extensible;

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
 * Implementation of an extensible hash for part 2 of project 2.
 */

//change insert, constructor, and before first

public class ExtensibleHash implements Index{
	
	public int NUM_BUCKETS = 2; //cs4432-project2: start with 2 buckets
	private String idxname;
	private Schema sch;
	private Transaction tx;
	private Constant searchkey = null;
	private TableScan ts = null;
	private TableInfo ti;
	private Map<Integer, HashBlock> index;
	
	
	
	/**
	 * cs4432-project2:
	 * constructor for ExtensibleHash
	 * @param idxname - index name
	 * @param sch - schema
	 * @param tx - transaction
	 */
	public ExtensibleHash(String idxname, Schema sch, Transaction tx) {
		this.idxname = idxname;
		this.sch = sch;
		this.tx = tx;
		
		this.index=new HashMap<Integer, HashBlock>();
		index.put(0, new HashBlock(1));
		index.put(1, new HashBlock(1));
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
		// TODO Auto-generated method stub
		beforeFirst(dataval);
		ts.insert();
		ts.setInt("block", datarid.blockNumber());
		ts.setInt("id", datarid.id());
		ts.setVal("dataval", dataval);
	}

	/**
	 * delete
	 * deletes something
	 * @param dataval
	 * @param datarid
	 */
	public void delete(Constant dataval, RID datarid) {
		// TODO Auto-generated method stub
		beforeFirst(dataval);
		while(next())
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
	 * globalDepth
	 * calculates the global depth of the Extensible Hash
	 * @return the global depth of the hash as an int
	 */
	public int globalDepth() {
		return (int) (Math.log10(NUM_BUCKETS)/Math.log10(2));
	}
	
}
