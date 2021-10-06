package implementationfinder;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Statement;

public class MethodInvocationVisitor extends ASTVisitor {
	private String varname;
	private String methodname;
	
	private Statement stmt;
	
	private List<InsertPosInfo> insertposlist = new ArrayList<>();
	
	public MethodInvocationVisitor(String vname, String mname, Statement s) {
		varname = vname;
		methodname = mname;
		stmt = s;
	}
	
	private ASTNode getStmtParent(ASTNode node) {
		if(node instanceof Statement)
			return node;
		else
			return getStmtParent(node.getParent());
	}
	
	public List<InsertPosInfo> getInsertPos(){
		return insertposlist;
	}
	
	public boolean visit(MethodInvocation node) {
		if(node.getExpression() != null) {
			String mname = node.getName().toString();
			String vname = node.getExpression().toString();
			if(methodname.equals(mname) && varname.equals(vname)) {
				ASTNode stmtparent = getStmtParent(node);
				MethodInvocation invoked = node;
				
				InsertPosInfo insertpos = 
						new InsertPosInfo((Statement)stmtparent, invoked);
				insertposlist.add(insertpos);
			}
		}
		return super.visit(node);
	}
}
