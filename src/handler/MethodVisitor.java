package handler;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;

import com.StatementFilter;

public class MethodVisitor extends ASTVisitor {
	public void patternSearch(MethodDeclaration node) {
		StatementFilter filter = new StatementFilter();
		Block body = node.getBody();
		if(body == null)
			return;
		
		for(Statement s : (List<Statement>) body.statements()) {
			filter.stmFilter(s);
		}
	}
	
	public boolean visit(MethodDeclaration node) {
		patternSearch(node);
		
		return super.visit(node);
	}
}
