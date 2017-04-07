package builder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.FeedMessage;
import model.RSS;

public class SimpleCacheManager {

	private static SimpleCacheManager instance;
	private static Object monitor = new Object();
	private final FeedMessageUtil util = new FeedMessageUtil();

	private Map<String, List<RSS>> cache = Collections.synchronizedMap(new HashMap<String, List<RSS>>());

	private SimpleCacheManager() {
	}

	public void put(String cacheKey, List<RSS> value) {
		cache.put(cacheKey, value);
	}
	
	public void sortCollectionByDate(List<FeedMessage> messages) {
		Collections.sort(messages, (m1, m2) -> util.getDate(m2.getPubDate()).compareTo(util.getDate(m1.getPubDate())));
	}

	public List<RSS> get(String cacheKey) {
		List<RSS> list = cache.get(cacheKey);
		return list;
	}

	public void clear(String cacheKey) {
		cache.put(cacheKey, null);
	}

	public void clear() {
		cache.clear();
	}

	public static SimpleCacheManager getInstance() {
		if (instance == null) {
			synchronized (monitor) {
				if (instance == null) {
					instance = new SimpleCacheManager();
				}
			}
		}
		return instance;
	}

}
