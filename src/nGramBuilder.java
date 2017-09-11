import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class nGramBuilder 
{
	Map<String, nGram> myNGrams = new LinkedHashMap<>();
	int wordCount = 0;
	String previousString = null;
	
	public nGramBuilder()
	{

	}
	
	//Add a status string to our NGramBuilder
	public void addString(String[] str)
	{		
		wordCount += (str.length - 2);
		for(String s : str)
		{
			if(!myNGrams.containsKey(s))
				myNGrams.put(s, new nGram(s));
			else
				myNGrams.get(s).incrementOccurences();
			
			if(previousString != null)	
				if(!myNGrams.get(previousString).checkChildren(s))
					myNGrams.get(previousString).addChild(new nGram(s));
				else
					myNGrams.get(previousString).incrementChild(s);
			
			previousString = s;
		}
		
	}
	
	//Train the model using MLE format
	public void trainModel()
	{
		Iterator<Entry<String, nGram>> it = myNGrams.entrySet().iterator();
		
		while(it.hasNext())
		{
			nGram current = it.next().getValue();
			current.generateMLE(wordCount);
			current.generateChildBounds();
		}

	}
	
	//Generate a tweet given given stored NGrams
	public String generateTweet()
	{
		String tweet = "";
		String lastWord = "";
		String currentWord = "START";
		nGram currentNGram = null;
		
		while(!lastWord.equals("END"))
		{
			currentNGram = myNGrams.get(currentWord);
			currentWord = currentNGram.getRandomChild();

			if(!currentWord.equals("END"))
				tweet += " "+currentWord;
			
			lastWord = currentWord;

		}
		
		return tweet;
	}

}
