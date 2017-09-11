import java.util.List;
import twitter4j.*;

public class TwitterScanner 
{
	String userName;
	List<Status> statuses = null;
	nGramBuilder myNGrams = null;
	
	public TwitterScanner(String u)
	{
		userName = u;
		myNGrams = new nGramBuilder();
	}
	
	//Get tweets from the specified User.
	public void findUserTweets()
	{
	    Twitter twitter = TwitterFactory.getSingleton();
		try 
		{
			statuses = twitter.getUserTimeline(userName, new Paging(1, 3500));
		} 
		catch (TwitterException e) 
		{
			System.out.println("Oh my, this user doesn't exist!");
		}
	}
	
	//Include strings into the NGramBuilder, and then train the model.
	public void generateNGrams()
	{
		String[] statusText;
		for(Status s : statuses)
		{
			if(!s.getText().substring(0, 2).equals("RT"))
			{
				statusText = trimStatus(s.getText());
				myNGrams.addString(statusText);
			}
		}
		
		myNGrams.trainModel();
	}
	
	//Generate tweets based on a user
	public String generateTweet()
	{
		String possibleTweet = "";
		
		while(true)
		{
			possibleTweet = myNGrams.generateTweet();
			if(possibleTweet.length() < 140)
				return possibleTweet;
		}

	}

	//Trim each status to follow a similar format;
	//All text is lowercase
	//Statuses begin with START and end with END
	//@ and # are ignored for trimming
	private String[] trimStatus(String text) 
	{
		String lowerCase = text.toLowerCase();
		lowerCase = "START "+lowerCase+" END";
		String[] myText = lowerCase.split(" "); //" |\\r?\\n"

		for(int i = 0; i < myText.length; i++)
		{
			if(!myText[i].equals(""))
			{
				if(isURL(myText[i]))
					myText[i] = "URL";
				else if (isAtUser(myText[i]))
				{
					//maybe something later...
				}
				else if (isHashtag(myText[i]))
				{
					//maybe something later...
				}
				else
				{
					//"[()]", ""
					//.replaceAll("[^a-zA-Z\\s]", "").replaceAll("\\s+", "");
					myText[i] = myText[i].replaceAll("[()]", "");
				}	
			}
		}
		
		return myText;
	}

	//Verifies if a string is a hashtag
	private boolean isHashtag(String string) 
	{
		if(string.charAt(0)=='#')
			return true;
		else
			return false;
	}

	//Verifies if a string is a url
	private boolean isURL(String string) 
	{
		if(string.length() < 4)
			return false;
		
		if(string.substring(0, 4).equals("http"))
			return true;
		else
			return false;
	}

	//Verifies if a string is @ a specific user
	private boolean isAtUser(String string) 
	{
		if(string.charAt(0)=='@')
			return true;
		else
			return false;
	}
}
