package io.zzzz.owo;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author lauwarm
 *
 */
public class SaveAllTheLikesTumblr {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("SpringConfig.xml");
		
		FilePath fp			= (FilePath) 		context.getBean("filepathBean");
		ReadConfigFile rcf	= (ReadConfigFile)	context.getBean("readconfigfileBean");
		TumblrClient tc		= (TumblrClient)	context.getBean("tumblrclientBean");
		CollectLinks cl		= (CollectLinks)	context.getBean("collectlinksBean");
		DownloadFile df		= (DownloadFile)	context.getBean("downloadfileBean");
		
		tc.setJClient(rcf.getOauthConsumerKey(), rcf.getOauthConsumerSecret());
		tc.setJClientToken(tc.getJClient(), rcf.getOauthClientToken(), rcf.getOauthClientTokenSecret());
		int limitSoft = 0;
		int limitHard = 0;
		
		try {
			cl.getFirstLikedPost(tc.getJClient());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("api rate limit exceeded!");
			System.exit(2);
		}
		
		if(rcf.getCurrentTimestamp().isEmpty()) {
			try {
				rcf.updateJSON(rcf.getJObj(), "currentTimestamp", String.valueOf(Instant.now().getEpochSecond()));
				rcf.reloadJSON(rcf.getJObj());
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("could not update config file! [currentTimestamp]");
				System.exit(2);
			}
		}
		if(rcf.getFirstTimestamp().isEmpty()) {
			try {
				rcf.updateJSON(rcf.getJObj(), "firstTimestamp", cl.getFirstLikedPost(tc.getJClient()));
				rcf.reloadJSON(rcf.getJObj());
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("could not update config file! [firstTimestamp]");
				System.exit(2);
			}
		}
		
		try {
			while (Long.parseLong(rcf.getCurrentTimestamp()) > Long.parseLong(rcf.getFirstTimestamp())) {
				ArrayList<String> tmpArr = new ArrayList<String>();
				tmpArr = cl.collectLinks(rcf.getCurrentTimestamp(), tc.getJClient());
				Iterator<String> tmpItr = tmpArr.iterator();
				
				while(tmpItr.hasNext()) {
					try {
						df.downloadFile(tmpItr.next(), fp.getDownloadFilePath());
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("error while downloading file!");
						System.exit(2);
					}
				}
				
				rcf.updateJSON(rcf.getJObj(), "currentTimestamp", cl.getLikes().get(0).getLikedTimestamp().toString());
				rcf.reloadJSON(rcf.getJObj());
				
				System.out.println(new Date(Long.parseLong(rcf.getCurrentTimestamp())*1000));
				TimeUnit.SECONDS.sleep(2);
				
				limitSoft++;
				limitHard++;
				
				if(limitHard == 4500) {
					System.out.println("You're about to exceed the Tumblr API daily limitation. Restart in 24 hours!");
					rcf.updateJSON(rcf.getJObj(), "currentTimestamp", cl.getLikes().get(0).getLikedTimestamp().toString());
					System.exit(1);
				}
				if(limitSoft == 500) {
					System.out.println("You're about to exceed the Tumblr API limitation! BREAK for 30 minutes.");
					TimeUnit.MINUTES.sleep(30);
					limitSoft = 0;
				}
			}
		} catch (Exception e) {
			System.out.println("error during while - try to restart the program");
			try {
				rcf.updateJSON(rcf.getJObj(), "currentTimestamp", cl.getLikes().get(0).getLikedTimestamp().toString());
				rcf.updateJSON(rcf.getJObj(), "firstTimestamp", "");
				e.printStackTrace();
				System.exit(2);
			} catch (Exception e1){
				e1.printStackTrace();
				System.out.println("error during updating config file! [e1]");
			}
		}
		
		try {
			rcf.updateJSON(rcf.getJObj(), "currentTimestamp", "");
			rcf.updateJSON(rcf.getJObj(), "firstTimestamp", cl.getLikes().get(0).getLikedTimestamp().toString());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error during updating config file! [e]");
			System.exit(2);
		}
		
		System.out.println("all liked posts downloaded. (hopefully)");
		System.exit(0);
	}
	
}
