package implementationfinder;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class MethodScanner extends ASTVisitor {
	private ImplementationFinder finder;
	private List<SearchKeyVar> varkeylist = new ArrayList<>();
	
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
	
	private List<String> ExtractMethodName(Statement s){
		List<String> methodlist = new ArrayList<>();
		
		List<SearchKey> keylist = finder.getKeyList();
		for(SearchKey key:keylist) {
			String classname = key.getClassName();
			if(s instanceof VariableDeclarationStatement) {
				Type type = ((VariableDeclarationStatement) s).getType();
				if(type.toString().equals(classname)) {
					String method = key.getMethodName();
					methodlist.add(method);
				}
			}
		}
		
		return methodlist;
	}
	
	private String ExtractVarName(List fraglist) {
		String varname = null;
		
		for(int i = 0;i < fraglist.size();i++) {
			if(fraglist.get(i) instanceof VariableDeclarationFragment) {
				VariableDeclarationFragment vardecfrag = 
						(VariableDeclarationFragment) fraglist.get(i);
				varname = vardecfrag.getName().toString();
			}
		}
		
		return varname;
	}
	
	private void VarFinder(MethodDeclaration node) {
		Block body = node.getBody();
		if(body == null)
			return;
		
		List<Statement> stmtlist = node.getBody().statements();
		for(Statement s:stmtlist) {
			List<String> methodlist = ExtractMethodName(s);
			if(methodlist.size() == 0)
				continue;			
			String classname = ((VariableDeclarationStatement) s).getType().toString();
			List fraglist = ((VariableDeclarationStatement) s).fragments();
			String varname = ExtractVarName(fraglist);
			if(varname == null)
				continue;
			for(int i = 0;i < methodlist.size();i++) {
				SearchKeyVar key = new SearchKeyVar(varname, methodlist.get(i));
				varkeylist.add(key);
			}
		}
	}
	
	private void StatementFinder(MethodDeclaration node) {
	}
	
	public MethodScanner(ImplementationFinder f) {
		finder = f;
	}
	
	public boolean visit(MethodDeclaration node) {
		VarFinder(node);
		return super.visit(node);
	}
}
