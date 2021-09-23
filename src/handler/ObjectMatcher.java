package handler;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class ObjectMatcher extends ASTVisitor {
	private String fieldmatch;
	private String namematch;
	
	public ObjectMatcher(String field, String name){
		fieldmatch = field;
		namematch = name;
	}
	
	public boolean visit(MethodDeclaration node) {
		return super.visit(node);
	}
}
