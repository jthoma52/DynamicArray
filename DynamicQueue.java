import java.util.NoSuchElementException;

public class DynamicQueue<T> {
	
	//declare vars
	protected DynamicArray<T> front;
	protected DynamicArray<T> rear;
	
	//returns the front of the array
	protected DynamicArray<T> getFront()
	{
		return front;
	}
	//returns the rear of the array
	protected DynamicArray<T> getRear()
	{
		return rear;
	}
	//constructor
	public DynamicQueue()
	{
		//initialize vars
		front = new DynamicArray<T>();
		rear = new DynamicArray<T>();
	}
	//add to the queue at the rear
	public void enqueue(T x)
	{
		rear.add(x);
	}
	//remove item from the front
	public T dequeue()
	{
		//if the queue is empty, throw and exception
		if(isEmpty())
			throw new NoSuchElementException();
		
		//if theres nothing in the front, copy it over in reverse form the rear.
		if(front.size()==0)
		{
			int p = rear.size(); //make it a constant at first because it changes!
			for(int i = 0; i < p; i++)
			{
				front.add(rear.get(p-i-1));
				rear.remove();
			}
		}
		T myObj = front.get(front.size()-1); //save it here to return later
		front.remove();
		return myObj;
		
		
	}
	//returns if array is empty or not
	public boolean isEmpty()
	{
		return (front.size() + rear.size() ==0);
	}
	//returns the size of the array
	public int size()
	{
		return front.size() + rear.size();
	}
	//returns a string representation of the DynamicQueue
	public String toString()
	{
		String s="[";
		for(int i = 0; i < front.size(); i++)
		{
			s+= front.get(i).toString() + ", ";
		}
		for(int i = 0; i < rear.size();i++)
			s+= rear.get(i).toString() + ", ";
		return s.substring(0, s.length()-2) + "]";
	}
	
	//returns a string representation for debugging.
	protected String toStringForDebugging()
	{
		return "front.toString: " + front.toString() + "\nrear.toString: " + rear.toString();
	}

}
