package handler;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class CompilationUnitVisitor extends ASTVisitor {
	public boolean visit(CompilationUnit node) {
		return false;
	}
}
