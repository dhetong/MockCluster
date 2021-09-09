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

import com.MockInfo;

public class ThenPatternHandler extends ASTVisitor {
	private String return_content;
	private int return_type;
	private String mock_object;
	private String invoked_method;
	
	private MockInfo mockinfo = null;
	
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
		WhenFilter(whenargs, node);
		
		if(ReturnFilter(returnargs) && WhenFilter(whenargs, node))
			mockinfo = new MockInfo(return_content, return_type, mock_object, invoked_method);
	}
	
	private boolean ReturnFilter(List returnargs) {
		if(returnargs.size() > 1) {
			return false;
		}
		else {
			if(returnargs.get(0) instanceof StringLiteral) {
				//no need
				return_content = returnargs.get(0).toString();
				return_type = 1;
			}
			else if(returnargs.get(0) instanceof SimpleName) {
				//check & tracking
				return_content = returnargs.get(0).toString();
				return_type = 2;
			}
			else if(returnargs.get(0) instanceof BooleanLiteral) {
				//no need
				return_content = returnargs.get(0).toString();
				return_type = 3;
			}
			else if(returnargs.get(0) instanceof NumberLiteral) {
				//no need
				return_content = returnargs.get(0).toString();
				return_type = 4;
			}
			else if(returnargs.get(0) instanceof MethodInvocation) {
				//check and tracking
				return_content = returnargs.get(0).toString();
				return_type = 5;
			}
			else if(returnargs.get(0) instanceof QualifiedName) {
				//no need
				return_content = returnargs.get(0).toString();
				return_type = 6;
			}
			else if(returnargs.get(0) instanceof ArrayCreation) {
				//no need, temporary
				return_content = returnargs.get(0).toString();
				return_type = 7;
			}
			else if(returnargs.get(0) instanceof CastExpression) {
				//no need, temporary
				return_content = returnargs.get(0).toString();
				return_type = 8;
			}
			else if(returnargs.get(0) instanceof NullLiteral) {
				//no need
				return_content = returnargs.get(0).toString();
				return_type = 9;
			}
			else if(returnargs.get(0) instanceof ClassInstanceCreation) {
				//no need
				return_content = returnargs.get(0).toString();
				return_type = 10;
			}
			else if(returnargs.get(0) instanceof TypeLiteral) {
				//no need
				return_content = returnargs.get(0).toString();
				return_type = 11;
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
				
				mock_object = invokedMethod.getExpression().toString();
				invoked_method = invokedMethod.getName().toString();
			}
			else {
				//when(connectionMock.prepareStatement(anyString())).thenThrow(new SQLException("E1")).thenReturn(secondTry)
			}
		}
		return true;
	}
	
	public MockInfo getMockInfo() {
		return mockinfo;
	}
}
