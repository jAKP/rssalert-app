package builder;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

import javafx.scene.control.Hyperlink;
import model.FeedMessage;
import read.RSSFeedParser;

@SuppressWarnings("deprecation")
public class FeedMessageUtil {
	private static final String CET = "CET";
	private static final String DELIM = ";";
	private static final File FILE = new File("H://rss.txt");
	private static final String FORMAT = "EEE, d MMM yyyy HH:mm:ss Z";
	private static final String FORMAT1 = "EEE, MMM d, yyyy, HH:mm Z";
	private static final String FORMAT2 = "M/d/yyyy HH:mm:SS a Z";
	private static final String REQ_FORMAT = "EEE, d MMM yyyy HH:mm:ss Z";
	public Map<String, String> RSS_MAP = null;

	public void createTempFolder() throws IOException {
		if (!FeedMessageUtil.FILE.exists()) {
			FeedMessageUtil.FILE.createNewFile();
		}
	}

	public void deleteFile() {
		try {
			final PrintWriter writer = new PrintWriter(FeedMessageUtil.FILE);
			writer.print("");
			writer.close();
		} catch (final IOException e) {
			throw new UserDefinedException(e.getMessage(), "Please check");
		}

	}

	public List<FeedMessage> getAllFeedMessages() {
		final List<FeedMessage> messages = new ArrayList<>();
		for (final Map.Entry<String, String> entry : RSS_MAP.entrySet()) {
			messages.addAll(new RSSFeedParser(entry.getKey(), RSS_MAP.get(entry.getKey())).readFeed().getMessages());
		}
		sortCollectionByDate(messages);
		return messages;
	}

	public Date getDate(final String inputdate) {
		final SimpleDateFormat fmt = new SimpleDateFormat(FeedMessageUtil.FORMAT);
		final SimpleDateFormat fmt1 = new SimpleDateFormat(FeedMessageUtil.FORMAT1);
		final SimpleDateFormat fmt2 = new SimpleDateFormat(FeedMessageUtil.FORMAT2);
		fmt.setTimeZone(TimeZone.getTimeZone(FeedMessageUtil.CET));
		try {
			return fmt.parse(inputdate);
		} catch (final ParseException e) {
			try {
				return fmt1.parse(inputdate);
			} catch (final ParseException e1) {
				try {
					return fmt2.parse(inputdate);
				} catch (final ParseException e2) {
				}
			}
		}
		return null;
	}

	public List<FeedMessage> getFeedMessages(final String feedName) {
		List<FeedMessage> messages = new RSSFeedParser(feedName, RSS_MAP.get(feedName)).readFeed().getMessages();
		if (!messages.isEmpty()) {
			sortCollectionByDate(messages);
		}
		return messages;
	}

	public void sortCollectionByDate(List<FeedMessage> messages) {
		Collections.sort(messages, (m1, m2) -> getDate(m2.getPubDate()).compareTo(getDate(m1.getPubDate())));
	}

	public FeedMessage getLatestFeedMsg(final String feedName) {
		List<FeedMessage> messages = getFeedMessages(feedName);
		if (!messages.isEmpty()) {
			sortCollectionByDate(messages);
			return messages.get(0);
		}
		return null;
	}

	public String getFeedName(final String string) {
		return string.split(FeedMessageUtil.DELIM)[0];
	}

	public String getFeedURL(final String string) {
		return string.split(FeedMessageUtil.DELIM)[1];
	}

	public String getFormattedDateString(final Date inputdate) {
		final SimpleDateFormat fmt = new SimpleDateFormat(FeedMessageUtil.REQ_FORMAT);
		return fmt.format(inputdate);
	}

	public Map<String, String> getRSS_MAP() {
		return RSS_MAP;
	}

	public List<String> getRSS_NameList() {
		final List<String> rssLst = new ArrayList<>();
		for (final Map.Entry<String, String> entry : RSS_MAP.entrySet()) {
			rssLst.add(entry.getKey());
		}
		return rssLst;
	}

	public List<String> getRSS_URLList() {
		List<String> rssLst = null;
		for (final Map.Entry<String, String> entry : RSS_MAP.entrySet()) {
			rssLst = new ArrayList<>();
			rssLst.add(entry.getValue());
		}
		return rssLst;
	}

	public void openBrowser(final Hyperlink link) {
		try {
			Desktop.getDesktop().browse(new URI(link.getText().trim()));
		} catch (final Exception e) {
			throw new UserDefinedException(e.getMessage(), "Please check");
		}
	}

	public void readRssMapFromFile() {
		RSS_MAP = new HashMap<>();
		try (BufferedReader br = Files.newBufferedReader(FeedMessageUtil.FILE.toPath())) {
			String read = null;
			while ((read = br.readLine()) != null) {
				RSS_MAP.put(read.split(FeedMessageUtil.DELIM)[0].trim(), read.split(FeedMessageUtil.DELIM)[1].trim());
			}
		} catch (final Exception e) {
			throw new UserDefinedException(e.getMessage(), "Please check");
		}
	}

	public List<FeedMessage> removeDuplicates(final List<FeedMessage> list) {
		return new ArrayList<>(new LinkedHashSet<>(list));
	}

	public void setRSS_MAP(final Map<String, String> rSS_MAP) {
		RSS_MAP = rSS_MAP;
	}

	public void writeFile(final String line) {
		try (BufferedWriter writer = Files.newBufferedWriter(FeedMessageUtil.FILE.toPath(), Charset.forName("UTF-8"),
				StandardOpenOption.APPEND)) {
			writer.write(line);
			writer.newLine();
		} catch (final Exception e) {
			throw new UserDefinedException(e.getMessage(), "Please check");
		}
	}

	public boolean checkDateTodayOrYesterday(Date inputDate) {
		DateMidnight today = new DateMidnight();
		DateMidnight yesterday = (new DateMidnight()).minusDays(1);

		DateTime dateTime = new DateTime(inputDate);
		if (dateTime.toDateMidnight().equals(today) || dateTime.toDateMidnight().equals(yesterday)) {
			return true;
		}

		return false;
	}

}
