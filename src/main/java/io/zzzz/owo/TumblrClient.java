package io.zzzz.owo;

import com.tumblr.jumblr.JumblrClient;

public class TumblrClient {

	private JumblrClient jClient = null;
	
	TumblrClient() {
		
	}
	
	public void setJClient(String oauth_consumer_key, String oauth_consumer_secret) {
		this.jClient = new JumblrClient(oauth_consumer_key, oauth_consumer_secret);
	}
	
	public JumblrClient getJClient() {
		return jClient;
	}
	
	public void setJClientToken(JumblrClient jClient, String token, String tokenSecret) {
		jClient.setToken(token, tokenSecret);
	}
}
