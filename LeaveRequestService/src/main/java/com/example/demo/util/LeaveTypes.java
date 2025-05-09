package com.example.demo.util;

import java.util.HashMap;
import java.util.Map;

public class LeaveTypes {

	public static Map<String, Integer> leaves() {
		Map<String, Integer> leaves = new HashMap<>();
		leaves.put("casual", 10);
		leaves.put("sick", 8);
		leaves.put("vacation", 15);
		leaves.put("personal", 5);
		leaves.put("emergency", 1);
		return leaves;
	}
}
