import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class nGram
{
	String myGram = null;
	Map<String, nGram> myChildren = new LinkedHashMap<>();
	int myOccurences = 0;
	double highestChildBound;
	double myMLEProb;
	double myUpperBound;
	double myLowerBound;
	
	public nGram(String gram)
	{
		myGram = gram;
		myOccurences++;
	}
	
	public nGram(String gram, int occurences)
	{
		myGram = gram;
		myOccurences = occurences;
	}
	
	public String getString()
	{
		return myGram;
	}
	
	//Adds a child to this nGram
	public void addChild(nGram child)
	{
		myChildren.put(child.getString(), child);
	}
	
	//Returns true if this NGram has children
	public boolean hasChildren()
	{
		if(myChildren.size() > 0)
			return true;
		else
			return false;
	}
	
	//Returns all children
	public Map<String, nGram> getChildren()
	{
		return myChildren;
	}
	
	//Increment number of occurrences
	public void incrementOccurences()
	{
		myOccurences++;
	}
	
	//Decrement occurrences
	public void decrememntOccurences()
	{
		myOccurences--;
	}
	
	//Get the number of occurrences
	public int getOccurences()
	{
		return myOccurences;
	}
	
	//Get the MLE probability of this NGram
	public double getMLE()
	{
		return myMLEProb;
	}
	
	//Set Lower Bound of NGram
	public void setLowerBound(double bound)
	{
		myLowerBound = bound;
	}
	
	//Get Lower Bound of NGram
	public double getLowerBound()
	{
		return myLowerBound;
	}
	
	//Set Upper Bound of NGram
	public void setUpperBound(double bound)
	{
		myUpperBound = bound;
	}
	
	//Get Upper Bound of NGram
	public double getUpperBound()
	{
		return myUpperBound;
	}
	
	//Generate MLE probablity of this NGram and all of its children
	public void generateMLE(int wCount) 
	{
		myMLEProb = (double) myOccurences / wCount;
		Iterator<Entry<String, nGram>> nGramIt = myChildren.entrySet().iterator();

		while(nGramIt.hasNext())
		{
			nGramIt.next().getValue().generateMLE(myOccurences);
		}
	}
	
	//Generates the upper and lower bounds of children based on MLE
	public void generateChildBounds()
	{
		double lowerBound = 0;
		double upperBound = 0;
		Iterator<Entry<String, nGram>> nGramIt = myChildren.entrySet().iterator();

		while(nGramIt.hasNext())
		{
			String currentNGram = nGramIt.next().getKey();
			upperBound = lowerBound + myChildren.get(currentNGram).getMLE() * 1000;
			myChildren.get(currentNGram).setLowerBound(lowerBound);
			myChildren.get(currentNGram).setUpperBound(upperBound);
			lowerBound = upperBound + 1;
		}
		
		highestChildBound = upperBound;
		
	}

	//Return true if an NGram is a child of this NGram
	public boolean checkChildren(String s) 
	{
		if(myChildren.containsKey(s))
			return true;
		else
			return false;
	}

	//Increase the number of occurences of a specific child
	public void incrementChild(String s) 
	{
		if(myChildren.containsKey(s))
			myChildren.get(s).incrementOccurences();
	}
	
	//Removes a child from the NGram, returns the number of occurrences.
	public void deleteChild(String s)
	{
		if(checkChildren(s))
		{
			myChildren.remove(s);
		}
	}

	//Returns a random child using weighted selection.
	public String getRandomChild() 
	{
		Iterator<Entry<String, nGram>> nGramIt = myChildren.entrySet().iterator();
		int randomValue = (int) (Math.random() * ((highestChildBound) + 1));
		nGram currentNGram = null;
		
		while(nGramIt.hasNext())
		{
			currentNGram = nGramIt.next().getValue();
			if(currentNGram.getUpperBound() >= randomValue)
				return currentNGram.getString();
		}
		
		return currentNGram.getString();
	}
}
