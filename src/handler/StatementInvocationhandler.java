package handler;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class StatementInvocationhandler extends ASTVisitor {
	public boolean visit(MethodInvocation node) {
		List args = node.arguments();
		return super.visit(node);
	}
}
