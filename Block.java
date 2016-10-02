/*
 * This class passes all of the 14 test cases givem in Block_Tests.java
 * as of 09/10/16
 * 
 */



public class Block<T> {
	
	protected final int number; // Block number, as in Block1.
	protected final T[] arrayOfElements; //Holds actual elements
	protected final int capacity;  //number of elements that can fit in this block
	protected int size; //number of elements actually in the block
	
	@SuppressWarnings("unchecked")
	public Block(int n, int c)
	{
		//initiate all variables
		number = n;
		capacity = c;
		arrayOfElements = (T[])new Object[capacity];
		size = 0;
	}
	//return block number
	public int getNumber(){ return number; }
	
	//return block capacity
	public int getCapacity(){ return capacity; }
	
	//return block size
	public int size(){ return size; }
	
	//Increase space allocated for storing elements. increments size.
	public void grow()
	{
		size++;
	}
	//decrements size, sets the end element to null
	public void shrink()
	{
		size--;
		arrayOfElements[size] = null;
	}
	//gets the element at that index.
	public T getElement(int index)
	{
		return arrayOfElements[index];
	}
	
	//sets element at index i to x.
	public void setElement(int i, T x)
	{
		arrayOfElements[i] = x;
	}
	//Nice toString representation of block.
	public String toString()
	{
		String s = "";
		for(int i =0; i < size; i++)
		{
			s+= arrayOfElements[i].toString() + " ";
		}
		return s.substring(0,s.length()-1);
	}
	
	//Nice toString method for debugging.
	protected String toStringForDebugging()
	{
		String s = "";
		for(int i =0; i < size; i++)
		{
			s+= arrayOfElements[i].toString() + " ";
		}
		s+= String.format("-capacity = %d size = %d", capacity, size);
		return s;
	
	}
	
	
	
	

}
