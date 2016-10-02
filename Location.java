

public class Location
{
	//protected variables.
	protected final int blockIndex;
	protected final int elementIndex;
	
	//constructor
	public Location(int blockIndex, int elementIndex)
	{
		//initiate vars
		this.blockIndex = blockIndex;
		this.elementIndex = elementIndex;
	}
	//returns the block index
	public int getBlockIndex()
	{
		return blockIndex;
	}
	//returns the element index in said block
	public int getElementIndex()
	{
		return elementIndex;
	}
	//returns a string for debugging purposes.
	public String toStringForDebugging()
	{
		return String.format("blockIndex: %d elementIndex: %d ", blockIndex, elementIndex);
	}
}