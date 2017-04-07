package org.timesheet.integration;

public class SimpleMap<K, V> {

	public MyKeyValuePair<K, V>[] myKeyValuePair = new MyKeyValuePair[100];

	// get, put
	public V get(K key) {

		// get index and fetch the corresponding value
		int index = key.hashCode() % myKeyValuePair.length;

		MyKeyValuePair<K, V> myMap = myKeyValuePair[index];
		while (myMap != null && !myMap.getKey().equals(key)) {
			myMap = myMap.getNext();
		}

		if (myMap == null) {
			return null;
		} else {
			return myMap.getValue();
		}
	}

	public void put(K key, V value) {
		// get index and fetch the corresponding value
		int index = key.hashCode() % myKeyValuePair.length;
		MyKeyValuePair<K, V> myMap = myKeyValuePair[index];

		if (myMap != null) {
			boolean insert = false;
			while (!insert) {
				if (key.equals(myMap.getKey())) {
					myMap.setValue(value);
					insert = true;
				} else {
					myMap.setNext(new MyKeyValuePair<K, V>(key, value));
					insert = true;
				}
				myMap = myMap.getNext();
			}
		} else {
			myKeyValuePair[index] = new MyKeyValuePair<K, V>(key, value);
		}

	}

}