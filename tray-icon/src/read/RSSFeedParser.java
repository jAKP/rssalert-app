package read;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

import builder.FeedMessageUtil;
import builder.UserDefinedException;
import javafx.scene.control.Hyperlink;
import model.Feed;
import model.FeedMessage;

public class RSSFeedParser {
	static final String AUTHOR = "author";
	static final String CHANNEL = "channel";
	static final String DESCRIPTION = "description";
	static final String ITEM = "item";
	static final String LANGUAGE = "language";
	static final String LINK = "link";
	static final String PUB_DATE = "pubDate";
	static final String TITLE = "title";

	String name = null;
	URL url = null;
	private final FeedMessageUtil util = new FeedMessageUtil();

	public RSSFeedParser(final String feedUrl) {
		try {
			// System.out.println((new SimpleDateFormat("yyyy/MM/dd
			// HH:mm:ss")).format(new Date()) + "calling " + feedUrl);

			setProxy();
			url = new URL(replaceHttps(feedUrl));
		} catch (final MalformedURLException e) {
			throw new UserDefinedException(e.getMessage(), "Please check");
		}
	}

	public RSSFeedParser(final String feedName, final String feedUrl) {
		try {
			setProxy();
			url = new URL(replaceHttps(feedUrl));
			name = feedName;
		} catch (final MalformedURLException e) {
			throw new UserDefinedException(e.getMessage(), "Please check");
		}
	}

	private String getCharacterData(final XMLEvent event) throws XMLStreamException {
		String result = "";
		if (event instanceof Characters) {
			result = event.asCharacters().getData();
		}
		return result;
	}

	private InputStream read() {
		try {
			return url.openStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Feed readFeed() {
		Feed feed = null;
		try {
			boolean isFeedHeader = true;
			String title = "";
			String link = "";
			String pubdate = "";

			final XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			inputFactory.setProperty("javax.xml.stream.isCoalescing", true);

			final InputStream in = read();
			final XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
			// System.err.println(eventReader);
			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				if (event.isStartElement()) {
					final String localPart = event.asStartElement().getName().getLocalPart();
					switch (localPart) {
					case ITEM:
						if (isFeedHeader) {
							isFeedHeader = false;
							feed = new Feed(title, link, pubdate);
						}
						event = eventReader.nextEvent();
						break;
					case TITLE:
						title = getCharacterData(eventReader.nextEvent());
						break;
					case LINK:
						link = getCharacterData(eventReader.nextEvent());
						break;
					case LANGUAGE:
						getCharacterData(eventReader.nextEvent());
						break;
					case PUB_DATE:
						pubdate = getCharacterData(eventReader.nextEvent());
						break;
					}
				} else if (event.isEndElement()) {
					if (event.asEndElement().getName().getLocalPart() == RSSFeedParser.ITEM) {
						Date date = util.getDate(pubdate.trim());
						if (util.checkDateTodayOrYesterday(date)) {
							final FeedMessage message = new FeedMessage();
							message.setLink(new Hyperlink(link));
							message.setTitle(title);
							message.setName(name);
							message.setPubDate(util.getFormattedDateString(date));
							if (feed != null) {
								feed.getMessages().add(message);
							}
							event = eventReader.nextEvent();
							continue;
						}
					}
				}
			}
		} catch (final Exception e) {
			return null;
		}
		return feed;
	}

	private String replaceHttps(final String string) {
		if (string.startsWith("https")) {
			return string.replaceFirst("https", "http");
		}
		return string;
	}

	private void setProxy() {
		System.setProperty("http.proxyHost", "proxy");
		System.setProperty("http.proxyPort", "8080");
	}
}