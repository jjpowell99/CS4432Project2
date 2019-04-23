package simpledb.index.extensible;

import simpledb.file.Block;
import simpledb.record.TableInfo;
import simpledb.tx.Transaction;

public class HashBlock {
	
	private int localDepth;
	private Block currentblk;
	private TableInfo ti;
	private Transaction tx;
	private int slotsize;
	private int numRecords;
	
	public HashBlock(Block currentblk, TableInfo ti, Transaction tx) {
		this.currentblk=currentblk;
		this.ti=ti;
		this.tx=tx;
		slotsize=ti.recordLength();
		tx.pin(currentblk);
	}
}
