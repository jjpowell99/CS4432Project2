package simpledb.buffer;

import simpledb.file.Block;

/** CS4432-Project1:
 * This class, AbsBufferMgr exists as an abstract class to allow
 * for the program to be tested with BasicBufferMgr and our
 * additional LRUBufferMgr, so that the efficiency can be
 * compared. The class includes stub methods for everything
 * required by the two buffer managers.
 */
public abstract class AbsBufferMgr {
	
	abstract int available();
	abstract void flushAll(int txnum);
	abstract Buffer pin(Block blk);
	abstract Buffer pinNew(String filename, PageFormatter fmtr);
	abstract void unpin(Buffer buf);

}
