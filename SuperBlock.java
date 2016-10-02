
public class SuperBlock {
	
	protected int number; //as in SB1, SB2, whatever
	protected int maxNumberOfDataBlocks; //number of blocks this superblock can have
	protected int maxNumberOfElementsPerBlock; //number of Elements per block each block can have 
	protected int currentNumberOfDataBlocks; // current number of Blocks in SuperBlock
	
	//constructor
	public SuperBlock(int n, int mnodb, int mnoepb, int cnodb)
	{
		//initiate all vars
		number = n;
		maxNumberOfDataBlocks = mnodb;
		maxNumberOfElementsPerBlock = mnoepb;
		currentNumberOfDataBlocks = cnodb;
	}
	
	//gets the block number
	public int getNumber()
	{
		return number;
	}
	//gets the max number of data blocks in the superblock
	public int getMaxNumberOfDataBlocks()
	{
		return maxNumberOfDataBlocks;
	}
	//gets the number of elements per each block in the super block
	public int getMaxNumberOfElementsPerBlock()
	{
		return maxNumberOfElementsPerBlock;
	}
	//gets the current number of datablocks in the super block
	public int getCurrentNumberOfDataBlocks()
	{
		return currentNumberOfDataBlocks;
	}
	//increments the current number of data blocks
	public int incrementCurrentNumberOfDataBlocks()
	{
		return currentNumberOfDataBlocks++;
	}
	//decrease the num of data blocks
	public int decrementCurrentNumberOfDataBlocks()
	{
		return currentNumberOfDataBlocks--;
	}
	
	//a nice toString to debug issues
	public String toStringForDebugging()
	{
		return String.format("- maxNumberOfDataBlocks: %d\n- maxNumberOfElementsPerBlock: %d\n-"
				+ " currentNumberOfDataBlocks: %d\n", maxNumberOfDataBlocks, maxNumberOfElementsPerBlock,
				currentNumberOfDataBlocks);
	}
	
	
	
}
