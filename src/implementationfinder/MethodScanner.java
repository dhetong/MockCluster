package implementationfinder;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class MethodScanner extends ASTVisitor {
	private ImplementationFinder finder;
	
	private boolean ClassMatcher(Statement s) {
		boolean flag = false;
		
		List<SearchKey> keylist = finder.getKeyList();
		for(SearchKey key:keylist) {
			String classname = key.getClassName();
			if(s instanceof VariableDeclarationStatement) {
				Type type = ((VariableDeclarationStatement) s).getType();
				if(type.toString().equals(classname)) {
					flag = true;
					break;
				}
			}
		}
		
		return flag;
	}
	
	private String ExtractVarName(List fraglist) {
		String varname = null;
		return varname;
	}
	
	private void VarFinder(MethodDeclaration node) {
		Block body = node.getBody();
		if(body == null)
			return;
		
		List<Statement> stmtlist = node.getBody().statements();
		for(Statement s:stmtlist) {
			if(ClassMatcher(s) == false)
				continue;
			String classname = ((VariableDeclarationStatement) s).getType().toString();
			List fraglist = ((VariableDeclarationStatement) s).fragments();
		}
	}
	
	public MethodScanner(ImplementationFinder f) {
		finder = f;
	}
	
	public boolean visit(MethodDeclaration node) {
		VarFinder(node);
		return super.visit(node);
	}
}
