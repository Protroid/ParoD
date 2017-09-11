public class ParoD 
{
	public static void main (String args[])
	{
		String userName = "Enter a Username Here!"; //Do not include the @ sign
		System.out.println("Hi there! Welcome to ParoD! I read tweets and use Machine Learning to figure out what a user might tweet next!");
		
		TwitterScanner ParoD = new TwitterScanner(userName);
		
		ParoD.findUserTweets();
		ParoD.generateNGrams();
		
		System.out.println(userName+"; "+ParoD.generateTweet());
		System.out.println(userName+"; "+ParoD.generateTweet());


	}

}
