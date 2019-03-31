package simpledb.buffer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import simpledb.file.*;

/**
 * Manages the pinning and unpinning of buffers to blocks.
 * @author Edward Sciore
 *
 */
class LRUBufferMgr extends AbsBufferMgr{
	// CS4432-Project1: Adding hashmap to keep track of blocks existing in bufferpool and 
	// queue for an LRU replacement policy of unpinned buffers.
	
   private Buffer[] bufferpool;
   private HashMap<Block, Buffer> existingBlockMap; 
   private int numAvailable;
   private Queue<Buffer> possibleBuffers;
   /** CS4432-Project1: Added initialization of an empty hashmap and queue filled with the empty buffers.
    * 
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
   LRUBufferMgr(int numbuffs) {
      bufferpool = new Buffer[numbuffs];
      numAvailable = numbuffs;
      existingBlockMap = new HashMap<Block, Buffer>(); // 4432
      possibleBuffers = new LinkedList<Buffer>();
      for (int i=0; i<numbuffs; i++) {
         bufferpool[i] = new Buffer(i);
         possibleBuffers.add(bufferpool[i]); // CS 4432
      }
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
   
   /** CS4432-Project1: Added a put statement to the hashmap to keep track of the buffer 
    * for blk, now that it is pinned in the buffer pool for efficient access thereafter.
    * 
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
      existingBlockMap.put(blk, buff);
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
   
   /** CS4432-Project1: Adds this buffer to the end of the queue now that it is unpinned, and thus, available to be replaced.
    * 
    * Unpins the specified buffer.
    * @param buff the buffer to be unpinned
    */
   synchronized void unpin(Buffer buff) {
      buff.unpin();
      if (!buff.isPinned()) {
         numAvailable++;
         possibleBuffers.add(buff);
      }
   }
   
   /**
    * Returns the number of available (i.e. unpinned) buffers.
    * @return the number of available buffers
    */
   int available() {
      return numAvailable;
   }
   /** CS4432-Project1: Changed the scan for an existing buffer to using the hashmap to find it efficiently if it exists.
    * Additionally, the queue is updated so that this most recently accessed block would exist at the end if unpinned.
    * Finds buffer that this block exists in if it does exist and returns it. 
    * @param blk Block to find in bufferpool if it exists.
    * @return Buffer in bufferpool that block exists in. Null is returned if the block does not exist in the buffer pool.
    */
   private Buffer findExistingBuffer(Block blk) {
	   Buffer bufferToAccess = existingBlockMap.get(blk);
	   if(bufferToAccess != null) {
		   possibleBuffers.remove(bufferToAccess);
		   if(bufferToAccess.isPinned())
			   possibleBuffers.add(bufferToAccess);
		   return bufferToAccess; 
	   }
      /*for (Buffer buff : bufferpool) {
         Block b = buff.block();
         if (b != null && b.equals(blk))
            return buff;
      }*/
      return null;
   }
   /** CS4432-Project1: Changed the scan for an available buffer to instead use an LRU replacement policy.
    * Finds the least recently used unpinned buffer and returns it.
    * @return Available unpinned buffer that was the least recently used, or null if all buffers are pinned
    */
   private Buffer chooseUnpinnedBuffer() {
	   // CS4432-Project1: While there are potential available buffers, find the first valid one and return
	   while(possibleBuffers.size() > 0) { 
		   if(possibleBuffers.peek().isPinned()) { // CS4432-Project1: If pinned, shouldn't be in queue, so remove it.
			   possibleBuffers.poll();
		   }
		   else { // CS4432-Project1: If unpinned buffer return the buffer and update the hashmap if it has a block currently.
			   Buffer toRemove = possibleBuffers.poll();
			   if(toRemove.block() != null) {
				   existingBlockMap.remove(toRemove.block());
			   }
			   return toRemove;
		   }
	   }

      /*for (Buffer buff : bufferpool)
         if (!buff.isPinned())
         return buff;*/
      return null;
   }
   /** CS4432-Project1: Changed the toString by overwriting it to list the toStrings for each buffer in the bufferpool
    * @return String of toStrings of buffers in bufferpool with header describing buffer manager type.
    */
   public String toString() {
	   String output = "LRU Buffer Manager\n";
	   for(Buffer b: bufferpool) {
		   output += b.toString() + "\n";
	   }
	   return output;
   }
}
