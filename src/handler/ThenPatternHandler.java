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
		//WhenFilter(whenargs, node);
	}
	
	private boolean ReturnFilter(List returnargs) {
		if(returnargs.size() > 1) {
			return false;
		}
		else {
			if(returnargs.get(0) instanceof StringLiteral) {
				//no need
			}
			else if(returnargs.get(0) instanceof SimpleName) {
				//check & tracking
			}
			else if(returnargs.get(0) instanceof BooleanLiteral) {
				//no need
			}
			else if(returnargs.get(0) instanceof NumberLiteral) {
				//no need
			}
			else if(returnargs.get(0) instanceof MethodInvocation) {
				//check and tracking
			}
			else if(returnargs.get(0) instanceof QualifiedName) {
				//no need
			}
			else if(returnargs.get(0) instanceof ArrayCreation) {
				//no need, temporary
			}
			else if(returnargs.get(0) instanceof CastExpression) {
				//no need, temporary
			}
			else if(returnargs.get(0) instanceof NullLiteral) {
				//no need
			}
			else if(returnargs.get(0) instanceof ClassInstanceCreation) {
				//no need
			}
			else if(returnargs.get(0) instanceof TypeLiteral) {
				//no need
			}
			else {
			}
		}
		return true;
	}
	
	private boolean WhenFilter(List whenargs, MethodInvocation node) {
		if(whenargs.size() > 1) {
			return false;
		}
		else {
			if(whenargs.get(0) instanceof MethodInvocation) {
				MethodInvocation invokedMethod = (MethodInvocation) whenargs.get(0);
			}
			else {
				//when(connectionMock.prepareStatement(anyString())).thenThrow(new SQLException("E1")).thenReturn(secondTry)
			}
		}
		return true;
	}
}
