package implementationfinder;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Statement;

public class InsertPosInfo {
	private String methodname;
	private String varname;
	private String classname;
	
	private Statement stmt;
	private MethodInvocation invoked;
	
	private MethodDeclaration mdec;
	private Block insertblock;
	
	private boolean insertafter;
	private boolean insertbefore;
	
	public InsertPosInfo(Statement s, MethodInvocation m, Block b) {
		stmt = s;
		invoked = m;
		insertblock = b;
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
	
	public void InitMethodDec(MethodDeclaration node) {
		mdec = node;
	}
	
	public MethodDeclaration getMethodDec() {
		return mdec;
	}
	
	public Block getInsertBlock() {
		return insertblock;
	}
}
