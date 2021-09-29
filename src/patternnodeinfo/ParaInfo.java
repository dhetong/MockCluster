package patternnodeinfo;

public class ParaInfo extends Info {
	private String stmt;
	private int position = -1;//this is a parameter
	
	public void InitStmt(String s) {
		stmt = s;
	}
	
	public String getStmt() {
		return stmt;
	}
	
	public int getPosition() {
		return position;
	}
}
