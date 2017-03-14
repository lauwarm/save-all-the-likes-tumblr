package io.zzzz.owo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.Photo;
import com.tumblr.jumblr.types.PhotoPost;
import com.tumblr.jumblr.types.Post;

public class CollectLinks {
	
	private List<Post> likes = null;
	
	public String getFirstLikedPost(JumblrClient jClient) {
		Map<String, Object> params = new HashMap<String,Object>(); 
		params.put("type", "photo");
		params.put("limit", 1);
		params.put("after", 1171843200);
		List<Post> likes = jClient.userLikes(params);
		return String.valueOf(likes.get(0).getLikedTimestamp());
	}
	
	public ArrayList<String> collectLinks(String currentTimestamp, JumblrClient jClient) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", "photo");
		params.put("limit", 1);
		params.put("before", currentTimestamp);
		
		likes = jClient.userLikes(params);
		ArrayList<String> likedUrls = new ArrayList<String>();
		
		try {
			PhotoPost photos = (PhotoPost) likes.get(0);
			Iterator<Photo> photoIterator = photos.getPhotos().iterator();
			
			while(photoIterator.hasNext()) {
				likedUrls.add(photoIterator.next().getOriginalSize().getUrl());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return likedUrls;
	}
	
	public List<Post> getLikes() {
		return likes;
	}
}
