package org.timesheet.integration;

public class Test {

	public static void main(String[] args) {
		SimpleMap<String, String> map = new SimpleMap<>();
		map.put("NL", "Netherlands");
		map.put("UK", "Ukraine");
		map.put("DE", "Germany");
		map.put("UK", "United Kingdom");

		System.out.println(map.get("NL"));
		System.out.println(map.get("UK"));
		System.out.println(map.get("DE"));
		System.out.println(map.get("FR"));
	}
}
