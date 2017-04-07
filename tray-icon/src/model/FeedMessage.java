package model;

public class FeedMessage extends Extra {
	String name;
	String title;
	String pubDate;

	public String getName() {
		return name;
	}

	public String getPubDate() {
		return pubDate;
	}

	public String getTitle() {
		return title;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setPubDate(final String pubDate) {
		this.pubDate = pubDate;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "FeedMessage [title=" + title + ", name=" + name + ", pubdate=" + pubDate + "]";
	}

}