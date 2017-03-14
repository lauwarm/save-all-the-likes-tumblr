package io.zzzz.owo;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ReadConfigFile {

	private String oauthConsumerKey = null;
	private String oauthConsumerSecret = null;
	private String oauthClientToken = null;
	private String oauthClientTokenSecret = null;
	private String currentTimestamp = null;
	private String firstTimestamp = null;
	
	private JSONObject jObj = null;
	
	ReadConfigFile() throws IOException, ParseException {
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(new FileReader("./config"));
		jObj = (JSONObject) obj;
		
		try {
			setOauthConsumerKey((String) jObj.get("oauth_consumer_key"));
			setOauthConsumerSecret((String) jObj.get("oauth_consumer_secret"));
			setOauthClientToken((String) jObj.get("oauth_client_token"));
			setOauthClientTokenSecret((String) jObj.get("oauth_client_token_secret"));
			
			setCurrentTimestamp((String) jObj.get("currentTimestamp"));
			setFirstTimestamp((String) jObj.get("firstTimestamp"));
		} catch(Exception e) {
			System.out.println("Parse Error");
			e.printStackTrace();
		}
		
		//updateJSON(jObj, "firstTimestamp", "10");
	}
	
	@SuppressWarnings("unchecked")
	public void updateJSON(JSONObject jObj, String key, String value) throws IOException {
		jObj.put(key, value);
		FileWriter out = new FileWriter("./config");
		out.write(jObj.toJSONString());
		out.flush();
		out.close();
	}
	
	public void reloadJSON(JSONObject jObj) {
		try {
			setOauthConsumerKey((String) jObj.get("oauth_consumer_key"));
			setOauthConsumerSecret((String) jObj.get("oauth_consumer_secret"));
			setOauthClientToken((String) jObj.get("oauth_client_token"));
			setOauthClientTokenSecret((String) jObj.get("oauth_client_token_secret"));
			
			setCurrentTimestamp((String) jObj.get("currentTimestamp"));
			setFirstTimestamp((String) jObj.get("firstTimestamp"));
		} catch(Exception e) {
			System.out.println("Parse Error");
			e.printStackTrace();
		}
	}
	
	public JSONObject getJObj() {
		return jObj;
	}

	public String getOauthConsumerKey() {
		return oauthConsumerKey;
	}	
	public void setOauthConsumerKey(String oauthConsumerKey) {
		this.oauthConsumerKey = oauthConsumerKey;
	}

	public String getOauthConsumerSecret() {
		return oauthConsumerSecret;
	}
	public void setOauthConsumerSecret(String oauthConsumerSecret) {
		this.oauthConsumerSecret = oauthConsumerSecret;
	}

	public String getOauthClientToken() {
		return oauthClientToken;
	}
	public void setOauthClientToken(String oauthClientToken) {
		this.oauthClientToken = oauthClientToken;
	}
	
	public String getOauthClientTokenSecret() {
		return oauthClientTokenSecret;
	}
	public void setOauthClientTokenSecret(String oauthClientTokenSecret) {
		this.oauthClientTokenSecret = oauthClientTokenSecret;
	}

	public String getCurrentTimestamp() {
		return currentTimestamp;
	}
	public void setCurrentTimestamp(String currentTimestamp) {
		this.currentTimestamp = currentTimestamp;
	}
	
	public String getFirstTimestamp() {
		return firstTimestamp;
	}
	public void setFirstTimestamp(String firstTimestamp) {
		this.firstTimestamp = firstTimestamp;
	}

}
