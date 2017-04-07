package org.timesheet.integration;

public class MyKeyValuePair<K, V> {
	private K key;
	private V value;
	MyKeyValuePair<K, V> next = null;

	public MyKeyValuePair(K key, V value) {
		super();
		this.key = key;
		this.value = value;
	}

	MyKeyValuePair<K, V> getNext() {
		return next;
	}

	void setNext(MyKeyValuePair<K, V> next) {
		this.next = next;
	}

	// getters and setters
	public K getKey() {
		return key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}
}
