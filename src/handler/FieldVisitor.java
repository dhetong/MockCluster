package handler;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;

public class FieldVisitor extends ASTVisitor {
	public boolean visit(FieldDeclaration node) {
		return super.visit(node);
	}
}
