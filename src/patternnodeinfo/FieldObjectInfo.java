package patternnodeinfo;

public class FieldObjectInfo extends Info {
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
