package handler;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class ThenPatternHandler extends ASTVisitor {
	public boolean visit(MethodInvocation node) {
		return false;
	}
}
