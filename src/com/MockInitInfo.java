package com;

public class MockInitInfo {
	private String mock_object_name;
	private String mock_class;
	
	public MockInitInfo(String object_name, String object_class) {
		mock_object_name = object_name;
		mock_class = object_class;
	}
	
	public String getName() {
		return mock_object_name;
	}
	
	public String getClassName() {
		return mock_class;
	}
}
