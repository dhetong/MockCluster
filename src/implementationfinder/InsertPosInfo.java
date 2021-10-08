package implementationfinder;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Statement;

public class InsertPosInfo {
	private String methodname;
	private String varname;
	private String classname;
	
	private Statement stmt;
	private MethodInvocation invoked;
	
	private boolean insertafter;
	private boolean insertbefore;
	
	public InsertPosInfo(Statement s, MethodInvocation m) {
		stmt = s;
		invoked = m;
	}
	
	public void InitBefore(boolean b) {
		insertbefore = b;
	}
	
	public void InitAfter(boolean a) {
		insertafter = a;
	}
	
	public Statement getInsertPos() {
		return stmt;
	}
	
	public MethodInvocation getInsertContent() {
		return invoked;
	}
	
	public boolean getAfter() {
		return insertafter;
	}
	
	public boolean getBefore() {
		return insertbefore;
	}
	
	public void UpdateClassName(String cname) {
		classname = cname;
	}
	
	public String getClassName() {
		return classname;
	}
}
