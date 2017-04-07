package model;

import java.util.ArrayList;
import java.util.List;

public class Feed {

	final List<FeedMessage> entries = new ArrayList<>();

	final String link;
	final String pubDate;
	final String title;

	public Feed(final String title, final String link, final String pubDate) {
		this.title = title;
		this.link = link;
		this.pubDate = pubDate;
	}

	public String getLink() {
		return link;
	}

	public List<FeedMessage> getMessages() {
		return entries;
	}

	public String getPubDate() {
		return pubDate;
	}

	public String getTitle() {
		return title;
	}

	@Override
	public String toString() {
		return "Feed [link=" + link + ", pubDate=" + pubDate + ", title=" + title + "]";
	}

}