package model;

import java.util.List;

public class RSS {

	List<FeedMessage> feedMessages;
	FeedMessage latestFeedMessage;
	String latestMessageDateTime;
	String rssLink;
	String rssName;

	public List<FeedMessage> getFeedMessages() {
		return feedMessages;
	}

	public String getRssLink() {
		return rssLink;
	}

	public String getRssName() {
		return rssName;
	}

	public void setFeedMessages(final List<FeedMessage> feedMessages) {
		this.feedMessages = feedMessages;
	}

	public void setLatestFeedMessage(final FeedMessage latestFeedMessage) {
		this.latestFeedMessage = latestFeedMessage;
	}

	public void setLatestMessageDateTime(final String latestMessageDateTime) {
		this.latestMessageDateTime = latestMessageDateTime;
	}

	public void setRssLink(final String rssLink) {
		this.rssLink = rssLink;
	}

	public void setRssName(final String rssName) {
		this.rssName = rssName;
	}

	public FeedMessage getLatestFeedMessage() {
		return latestFeedMessage;
	}

	public String getLatestMessageDateTime() {
		return latestMessageDateTime;
	}

}
