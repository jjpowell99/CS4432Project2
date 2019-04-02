package simpledb.buffer;

import simpledb.file.*;

/**
 * Manages the pinning and unpinning of buffers to blocks.
 * @author Edward Sciore
 *
 */
class BasicBufferMgr extends AbsBufferMgr{
   private Buffer[] bufferpool;
   private int numAvailable;
   
   /**
    * Creates a buffer manager having the specified number 
    * of buffer slots.
    * This constructor depends on both the {@link FileMgr} and
    * {@link simpledb.log.LogMgr LogMgr} objects 
    * that it gets from the class
    * {@link simpledb.server.SimpleDB}.
    * Those objects are created during system initialization.
    * Thus this constructor cannot be called until 
    * {@link simpledb.server.SimpleDB#initFileAndLogMgr(String)} or
    * is called first.
    * @param numbuffs the number of buffer slots to allocate
    */
   BasicBufferMgr(int numbuffs) {
      bufferpool = new Buffer[numbuffs];
      numAvailable = numbuffs;
      for (int i=0; i<numbuffs; i++)
         bufferpool[i] = new Buffer(i);
   }
   
   /**
    * Flushes the dirty buffers modified by the specified transaction.
    * @param txnum the transaction's id number
    */
   synchronized void flushAll(int txnum) {
      for (Buffer buff : bufferpool)
         if (buff.isModifiedBy(txnum))
         buff.flush();
   }
   
   /**
    * Pins a buffer to the specified block. 
    * If there is already a buffer assigned to that block
    * then that buffer is used;  
    * otherwise, an unpinned buffer from the pool is chosen.
    * Returns a null value if there are no available buffers.
    * @param blk a reference to a disk block
    * @return the pinned buffer
    */
   synchronized Buffer pin(Block blk) {
      Buffer buff = findExistingBuffer(blk);
      if (buff == null) {
         buff = chooseUnpinnedBuffer();
         if (buff == null)
            return null;
         buff.assignToBlock(blk);
      }
      if (!buff.isPinned())
         numAvailable--;
      buff.pin();
      return buff;
   }
   
   /**
    * Allocates a new block in the specified file, and
    * pins a buffer to it. 
    * Returns null (without allocating the block) if 
    * there are no available buffers.
    * @param filename the name of the file
    * @param fmtr a pageformatter object, used to format the new block
    * @return the pinned buffer
    */
   synchronized Buffer pinNew(String filename, PageFormatter fmtr) {
      Buffer buff = chooseUnpinnedBuffer();
      if (buff == null)
         return null;
      buff.assignToNew(filename, fmtr);
      numAvailable--;
      buff.pin();
      return buff;
   }
   
   /**
    * Unpins the specified buffer.
    * @param buff the buffer to be unpinned
    */
   synchronized void unpin(Buffer buff) {
      buff.unpin();
      if (!buff.isPinned())
         numAvailable++;
   }
   
   /**
    * Returns the number of available (i.e. unpinned) buffers.
    * @return the number of available buffers
    */
   int available() {
      return numAvailable;
   }
   /**
    * CS4432-Project1: Added timing to test time needed to find existing buffer.
    * @param blk Block to find if existing in buffer pool.
    * @return Buffer containing block if it exists. Otherwise, null.
    */
   private Buffer findExistingBuffer(Block blk) {
	  long startTime = System.nanoTime();
	  long endTime;

      for (Buffer buff : bufferpool) {
         Block b = buff.block();
         if (b != null && b.equals(blk)) {
        	 endTime = System.nanoTime();
        	 System.out.println("Time for find existing buffer: " + (endTime-startTime) + " ns");
            return buff;
         }
      }
 	  endTime = System.nanoTime();
 	  System.out.println("Time for find that buffer doesn't exist in pool: " + (endTime-startTime) + " ns");
      return null;
   }
   /**
    * CS4432-Project1: Added timing to test time needed to choose an available buffer.
    * @return First unpinned buffer found in scan of buffer pool.
    */
   private Buffer chooseUnpinnedBuffer() {
		  long startTime = System.nanoTime();
		  long endTime;
      for (Buffer buff : bufferpool)
         if (!buff.isPinned()) {
        	 endTime = System.nanoTime();
        	 System.out.println("Time choose unpinned buffer: " + (endTime-startTime) + " ns");
             return buff;

         }
 	 endTime = System.nanoTime();
 	 System.out.println("Time determine no available buffers: " + (endTime-startTime) + " ns");
      return null;
   }
   
   /**CS4432-Project1:
    * Added a toString method. 
    * @returns a string
    * saying which buffer manager this is, and then
    * a list of all the frames in it's bufferpool.
    */
   public String toString() {
	   String output = "Basic Buffer Manager\n";
	   for(Buffer b: bufferpool) {
		   output += b.toString() + "\n";
	   }
	   return output;
   }
}
