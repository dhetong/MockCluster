package patternnodeinfo;

public class MockInfo extends Info {
	private String return_content;
	private int return_type;
	private String mock_object;
	private String invoked_method;
	
	private String field_name;
	
	private boolean isReturnMock; //whether the return argument is a mock object
	
	public MockInfo(String content, int type, String object, String method) {
		return_content = content;
		return_type = type;
		mock_object = object;
		invoked_method = method;
		initInfoType(InfoType.MOCK_WHEN_INFO);
	}
	
	public void initField(String field) {
		field_name = field;
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
	
	public void initReturnMock(boolean ismock) {
		isReturnMock = ismock;
	}
	
	public boolean isReturnMock() {
		return isReturnMock;
	}
}
