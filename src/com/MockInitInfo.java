package com;

public class MockInitInfo extends Info {
	private String mock_object_name;
	private String mock_class;
	
	private String field_name;
	
	public MockInitInfo(String object_name, String object_class) {
		mock_object_name = object_name;
		mock_class = object_class;
		initInfoType(InfoType.MOCK_INIT_INFO);
	}
	
	public String getName() {
		return mock_object_name;
	}
	
	public String getClassName() {
		return mock_class;
	}
	
	public void initField(String field) {
		field_name = field;
	}
	
	public String getField() {
		return field_name;
	}
}
