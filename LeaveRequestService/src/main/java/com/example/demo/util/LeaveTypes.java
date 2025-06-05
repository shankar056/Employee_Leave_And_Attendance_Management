package com.example.demo.util;

import java.util.HashMap;
import java.util.Map;

public class LeaveTypes {

	public static Map<String, Integer> leaves() {
		Map<String, Integer> leaves = new HashMap<>();
		leaves.put("casual", 10);
		leaves.put("sick", 8);
		leaves.put("paid", 4);
		return leaves;
	}
}
