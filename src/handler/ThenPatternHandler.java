package handler;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.TypeLiteral;

public class ThenPatternHandler extends ASTVisitor {
	public boolean visit(MethodInvocation node) {
		if(node.getName().toString().equals("when")) {
		}
		else {
			InfoExtractorTypeB(node);
		}
		
		return false;
	}
	
	private void InfoExtractorTypeB(MethodInvocation node) {			
		if(node.getName().toString().equals("thenAnswer"))
			return;
		
		MethodInvocation returnNode = node;
		MethodInvocation whenNode = (MethodInvocation) node.getExpression();
		
		List returnargs = returnNode.arguments();
		List whenargs = whenNode.arguments();
		
		ReturnFilter(returnargs);
	}
	
	private void ReturnFilter(List returnargs) {
		if(returnargs.size() > 1) {
		}
		else {
			if(returnargs.get(0) instanceof StringLiteral) {
			}
			else if(returnargs.get(0) instanceof SimpleName) {
			}
			else if(returnargs.get(0) instanceof BooleanLiteral) {
			}
			else if(returnargs.get(0) instanceof NumberLiteral) {
			}
			else if(returnargs.get(0) instanceof MethodInvocation) {
			}
			else if(returnargs.get(0) instanceof QualifiedName) {
			}
			else if(returnargs.get(0) instanceof ArrayCreation) {
			}
			else if(returnargs.get(0) instanceof CastExpression) {
			}
			else if(returnargs.get(0) instanceof NullLiteral) {
			}
			else if(returnargs.get(0) instanceof ClassInstanceCreation) {
			}
			else if(returnargs.get(0) instanceof TypeLiteral) {
			}
			else {
			}
		}
	}
}
