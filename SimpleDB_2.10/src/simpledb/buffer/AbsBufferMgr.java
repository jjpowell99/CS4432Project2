package simpledb.buffer;

import simpledb.file.Block;

public abstract class AbsBufferMgr {
	
	abstract int available();
	abstract void flushAll(int txnum);
	abstract Buffer pin(Block blk);
	abstract Buffer pinNew(String filename, PageFormatter fmtr);
	abstract void unpin(Buffer buf);

}
