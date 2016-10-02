
public class DynamicArray<T> {

	protected Object[] arrayOfBlocks;
	protected final int DEFAULTCAPACITY = 4;
	protected int sizeOfArrayOfBlocks; //number of Blocks in arrayOfBlocks
	protected int size; // number of elements in dynamic array
	protected int numberOfEmptyDataBlocks;
	protected int numberOfNonEmptyDataBlocks;
	protected int numberOfDataBlocks; //number of Blocks in arrayOfBlocks
	protected int indexOfLastNonEmptyDataBlock;
	protected int indexOfLastDataBlock;
	protected int numberOfSuperBlocks;
	protected SuperBlock lastSuperBlock; // right-most SuperBlock
	
	//constructor
	public DynamicArray()
	{
		//initiate all variables
		arrayOfBlocks = new Object[DEFAULTCAPACITY]; //start array at default capacity (4)
		sizeOfArrayOfBlocks = 1; //theres always one block on start.
		numberOfEmptyDataBlocks = 1; //that one block is empty.
		numberOfDataBlocks = 1; //same as sizeOfArrayOfBlocks
		numberOfNonEmptyDataBlocks =0; //etc etc..
		indexOfLastNonEmptyDataBlock=-1;
		size = 0; //no elements in here yet
		numberOfSuperBlocks =1; //first SB
		indexOfLastDataBlock = 0;
		lastSuperBlock = new SuperBlock(0,1,1,1); //start the first superblock
		arrayOfBlocks[0] = new Block<T>(0,1); //assign the actual first empty block
	}
	//Returns block at index i.
	@SuppressWarnings("unchecked")
	protected Block<T> getBlock(int index)
	{
		//if its out of range, throw exception
		if(index < 0 || index > sizeOfArrayOfBlocks-1)
			throw new IllegalArgumentException();
		return (Block<T>)arrayOfBlocks[index];  //return proper element and cast as a block
	}
	//returns size - the number of elements that been added.
	public int size()
	{
		return size;
	}
	//returns the log base 2 of n.
	protected static double log2(int n)
	{
		return (Math.log(n) / Math.log(2));
	}
	
	//tells us if the last data block is full. used in grow and remove
	public boolean lastDataBlockIsFull()
	{
		return getBlock(indexOfLastDataBlock).size() == getBlock(indexOfLastDataBlock).getCapacity();
		
	}
	//tells us if the last superblock is full. used in grow and remove
	public boolean lastSuperBlockIsFull()
	{
		return lastSuperBlock.getCurrentNumberOfDataBlocks() == lastSuperBlock.getMaxNumberOfDataBlocks();
	}
	//Grows the DynamicArray one space.
	protected void grow()
	{
		if(getBlock(indexOfLastDataBlock).size()==0) //takes care of the initially empty block.
		{
			//adjusts appropriate vars
			numberOfEmptyDataBlocks--;
			numberOfNonEmptyDataBlocks++;
			indexOfLastNonEmptyDataBlock++;
		}
		if(!lastDataBlockIsFull()) //if the last data block is not full.
		{
			getBlock(indexOfLastDataBlock).grow(); //grows the Block to be able to hold another element.

		}
		else if(lastDataBlockIsFull()) //if the last data block is full
		{
			
			if(indexOfLastDataBlock != arrayOfBlocks.length-1) //if there is still space to add another block..
			{
				indexOfLastDataBlock++;
				sizeOfArrayOfBlocks++;
				if(!lastSuperBlockIsFull()) //if there is still room to add a block in the superblock
				{
					//throw in a new superblock, account for it in superblock, and finally add the space to the block
					arrayOfBlocks[indexOfLastDataBlock] = new Block<T>(indexOfLastDataBlock, lastSuperBlock.getMaxNumberOfElementsPerBlock());
					lastSuperBlock.incrementCurrentNumberOfDataBlocks();
					getBlock(indexOfLastDataBlock).grow();
					
					//adjust appropriate vars
					numberOfDataBlocks++;
					numberOfNonEmptyDataBlocks++;
					indexOfLastNonEmptyDataBlock++;
					
				}
				
				else if(lastSuperBlockIsFull())  //if the last superblock is full, make a new one via...
				{
					
					int k = numberOfSuperBlocks; //k is the number of the SB were about to make..
					int mnodb = ((int)Math.pow(2, (int)(k/2))); //The max number of data blocks the new superblock can have
					int mnoepb; //the max number of elements per block the new superblock can have
					
					//apply the given formulas to figure out the numElements per block
					if(k%2==1)
						mnoepb = ((int)Math.pow(2, ((int)(k/2))+1));
					else
						mnoepb = ((int)Math.pow(2, ((int)(k/2))));
					
					//make the new superblock, add a new block in, adjust in superblock, and finally make dynamicarray grow.
					lastSuperBlock = new SuperBlock(k,mnodb,mnoepb,0);
					arrayOfBlocks[indexOfLastDataBlock] = new Block<T>(indexOfLastDataBlock, lastSuperBlock.getMaxNumberOfElementsPerBlock());
					lastSuperBlock.incrementCurrentNumberOfDataBlocks();
					getBlock(indexOfLastDataBlock).grow();
					
					//adjust vars accordingly
					numberOfDataBlocks++;
					numberOfNonEmptyDataBlocks++;
					indexOfLastNonEmptyDataBlock++;
					numberOfSuperBlocks++;	
					}
			}
			else if(indexOfLastDataBlock == arrayOfBlocks.length-1) //if the whole array is full..
			{
				expandArray(); //double the capacity of the array.
				grow(); //nice simple recursion. Why copy code when weve already handled it?
			}
		}
	}
	//calls grow, sets the element in the new spot, increments size.
	public void add(T x)
	{
		grow(); //allows more space for a new element
		getBlock(indexOfLastDataBlock).setElement(getBlock(indexOfLastDataBlock).size()-1, x); //sets the element
		size++; //incriments size, because we added something
	}
	
	//shrinks the array to half size.
	public void shrinkArray()
	{
		//make new array half the length, copy it all over, and assign the newArray as the old array.
		Object[] newArr = new Object[arrayOfBlocks.length/2];
		for(int i =0; i < arrayOfBlocks.length/2; i++)
		{
			newArr[i] = (Block<T>)arrayOfBlocks[i];
		}
		arrayOfBlocks = newArr;
	}
	//removes the last element from the array.
	public void remove()
	{
		//if theres nothing in the array.
		if(size==0)
			throw new IllegalStateException();
		//If there are many elements in this block.
		if(getBlock(indexOfLastDataBlock).size()>1)    //if theres more than one element left in the last block
		{
			getBlock(indexOfLastDataBlock).shrink();
		}
		else if(getBlock(indexOfLastDataBlock).size()==1)  //if theres only one element in the block
		{
			//shrink it.
			getBlock(indexOfLastDataBlock).shrink();
			
			//adjust vars accordingly
			numberOfEmptyDataBlocks++;
			numberOfNonEmptyDataBlocks--;
			indexOfLastNonEmptyDataBlock--;
		}
		else //if the last data block is empty
		{
			if(getBlock(indexOfLastNonEmptyDataBlock).size() > 1) //if the block were looking at has more than one element in it.
			{
				getBlock(indexOfLastNonEmptyDataBlock).shrink(); //shrink it
			}
			else if(getBlock(indexOfLastNonEmptyDataBlock).size() ==1)  //if the element were looking at has exactly one..
			{
				getBlock(indexOfLastNonEmptyDataBlock).shrink();
				arrayOfBlocks[indexOfLastDataBlock] = null;  //removes the block
				lastSuperBlock.decrementCurrentNumberOfDataBlocks(); //decrements the data block.
				if(lastSuperBlock.getCurrentNumberOfDataBlocks() ==0) //if the last superblock is empty...
				{
					numberOfSuperBlocks--;
					
					int k = numberOfSuperBlocks-1; //k is the SBk that were about to make
					int mnodb = ((int)Math.pow(2, (int)(k/2))); //maxNumberOfDataBlocks it can have
					int mnoepb; //maxNumberOfElementsPerBlock it can have
					//determining ^
					if(k%2==1)
						mnoepb = ((int)Math.pow(2, ((int)(k/2))+1));
					else
						mnoepb = ((int)Math.pow(2, ((int)(k/2))));
					//readjust the last super block.
					lastSuperBlock = new SuperBlock(k,mnodb,mnoepb,mnodb);
					
				}
				//adjust vars accordingly.
				indexOfLastNonEmptyDataBlock--;
				numberOfNonEmptyDataBlocks--;
				indexOfLastDataBlock--;
				numberOfDataBlocks--;
				sizeOfArrayOfBlocks--;
				
			}
		}
		//decrement the size
		size--;
		
		//if we went under a fourth size, shrink the array.
		if(numberOfDataBlocks <= arrayOfBlocks.length/4)
			shrinkArray();
		
	}
	//doubles the size of ArrayOfBlocks
	@SuppressWarnings("unchecked")
	protected void expandArray()
	{
		Object[] newArray = new Object[arrayOfBlocks.length*2];
		//copy everything over, assign the newArray to the old one.
		for(int i = 0; i < arrayOfBlocks.length; i++)
		{
			newArray[i] = (Block<T>)arrayOfBlocks[i];
		}
		arrayOfBlocks = newArray;
	}
	
	
	
	// Returns a mask of N 1 bits; this code is provided below and can be used 
	  // as is
	protected int maskOfN(int N) 
	{
	    int POW2ToN = 1 << N; // left shift 1 N places; e.g., 1 << 2 = 100 = 4
	    int mask = POW2ToN - 1; // subtract 1; e.g., 1002 – 12 = 0112 = 3
	    return mask;
	}
	//returns the T at i
	@SuppressWarnings("unchecked")
	public T get(int i)
	{
		int blockIndex = locate(i).getBlockIndex();  //gets the block number
		int elementIndex = locate(i).getElementIndex(); //gets the element number
		return getBlock(blockIndex).getElement(elementIndex); //returns it all with the info above. Can throw exception.
	}
	
	//set index i to obj
	public void set(int index, T obj)
	{
		int blockIndex = locate(index).getBlockIndex(); //get block number
		int elementIndex = locate(index).getElementIndex(); //get element number
		getBlock(blockIndex).setElement(elementIndex, obj); //set it
		
	}
	
	//Locates the item at index i.
	public Location locate(int i)
	{
		int r = i+ 1;
		int k = (int)log2(r); //k is the superblock that i is in.
		int p; //the number of blocks before this superblock
		if(k%2==0) //if were in an even superblock
			p = 2*(int)(Math.pow(2,(int)(k/2))-1); //equation given
		else //if were in an odd superblock
			p = 2*(int)(Math.pow(2,(int)(k/2))-1) + (int)Math.pow(2, (int)(k/2)); //equation given in spec.
		
		int e; //the element in the block
		if(k%2==1) //if were in an odd superblock
			e = maskOfN(((int)(k/2))+1)&r; //make a mask of this ceilinged and & with r
		else //even super block.
			e = maskOfN(((int)(k/2)))&r;
		//shift r ceiling(k/2) bits
		if(k%2 ==1)
			{ r = r >> ((int)(k/2)+1 ); }
		else
			r = r >>(int)(k/2);

	    //make a mask of this and & it with r.
		int b = maskOfN(((int)(k/2)))&r;
		
		return new Location(p+b,e); //found new location!
	}
	
	
	
	 //returns a string for debugging. This is self explanitory.
	 public String toStringForDebugging()
     {
		 StringBuilder p = new StringBuilder(250);
    	 p.append("DynamicArray: ");
    	 for(int i =0; i < size; i++)
    		 p.append(" " + get(i));
    	 p.append("\n");
    	 p.append(String.format("numberOfDataBlocks: %d\n", numberOfDataBlocks));
    	 p.append(String.format("numberOfEmptyDataBlocks: %d\n", numberOfEmptyDataBlocks));
    	 p.append(String.format("numberOfNonEmptyDataBlocks: %d\n", numberOfNonEmptyDataBlocks));
    	 p.append(String.format("indexOfLastNonEmptyDataBlock: %d\n", indexOfLastNonEmptyDataBlock));
    	 p.append(String.format("indexOfLastDataBlock: %d\n", indexOfLastDataBlock));
    	 p.append(String.format("numberOfSuperBlocks: %d\n", numberOfSuperBlocks));
    	 p.append(String.format("lastSuperBlock: SB%d\n", lastSuperBlock.number));
    	 for(int i =0; i < numberOfDataBlocks; i++)
    	 {
    		 p.append(String.format("Block%d: \n", i));
    		 p.append(getBlock(i).toStringForDebugging());
    		 p.append("\n");
    	 }
    	 p.append("SB" + lastSuperBlock.getNumber() + ":\n" + lastSuperBlock.toStringForDebugging());
    	 
    	 
    	 return p.toString();
     }
	 //returns the toString rep of the array. Self-explanitory code
	 public String toString()
	 {
		 if(size==0)
			 return "[]";
		 String s = "[";
		 for(int i = 0; i <size; i++)
		 {
			 s+= get(i) + ", ";
		 }
		 return s.substring(0, s.length()-2) + "]";
	 }
	
	
}
