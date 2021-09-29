package patternnodeinfo;

public class MockInitInfo extends Info {
	private String mock_object_name;
	private String mock_class;
	
	private String field_name;
	
	private boolean hasWhen; //whether a mock object has related when patterns
	
	public MockInitInfo(String object_name, String object_class) {
		mock_object_name = object_name;
		mock_class = object_class;
		initInfoType(InfoType.MOCK_INIT_INFO);
		hasWhen = false;
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
	
	public void initHasWhen(boolean haswhen) {
		hasWhen = haswhen;
	}
	
	public boolean hasWhen() {
		return hasWhen;
	}
	
	private String stmt;
	private int position;
	
	public void InitStmt(String s) {
		stmt = s;
	}
	
	public String getStmt() {
		return stmt;
	}
	
	public void InitPosition(int pos) {
		position = pos;
	}
	
	public int getPosition() {
		return position;
	}
}
