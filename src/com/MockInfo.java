package com;

public class MockInfo {
	private String return_content;
	private int return_type;
	private String mock_object;
	private String invoked_method;
	
	private String field_name;
	
	private int link_index = -1;
	
	public MockInfo(String content, int type, String object, String method) {
		return_content = content;
		return_type = type;
		mock_object = object;
		invoked_method = method;
	}
	
	public void initField(String field) {
		field_name = field;
	}
	
	public void linkIndex(int index) {
		link_index = index;
	}
	
	public String getContent() {
		return return_content;
	}
	
	public int getType() {
		return return_type;
	}
	
	public String getObject() {
		return mock_object;
	}
	
	public String getMethod() {
		return invoked_method;
	}
	
	public String getField() {
		return field_name;
	}
}
