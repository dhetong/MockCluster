package handler;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.TypeLiteral;

public class AssignmentVisitor extends ASTVisitor {
	private String mock_object = null;
	private String mock_class = null;
	
	public boolean visit(Assignment node) {
		if(node.getLeftHandSide() instanceof SimpleName) {
			SimpleName v = (SimpleName) node.getLeftHandSide();
			if(node.getRightHandSide() instanceof MethodInvocation) {
				MethodInvocation method = (MethodInvocation) node.getRightHandSide();
				if(method.arguments().get(0) instanceof TypeLiteral) {
					SimpleType type = (SimpleType) ((TypeLiteral) method.arguments().get(0)).getType();
					mock_object = v.toString();
					mock_class = type.toString();
				}
				else {
				}
			}
			else {
				//f=new DefaultValueTransformerFactory(mock(KeySource.class),dbToBytes,objectToBytes)
			}
		}
		else {
			FieldAccess fv = (FieldAccess) node.getLeftHandSide();
			SimpleName v = fv.getName();
			if(node.getRightHandSide() instanceof MethodInvocation) {
				MethodInvocation method = (MethodInvocation) node.getRightHandSide();
				if(method.arguments().get(0) instanceof TypeLiteral) {
					SimpleType type = (SimpleType) ((TypeLiteral) method.arguments().get(0)).getType();
					mock_object = v.toString();
					mock_class = type.toString();
				}
				else {
				}
			}
			else {
			}
		}
		return false;
	}
	
	public String getName() {
		return mock_object;
	}
	
	public String getClassName() {
		return mock_class;
	}
	
	public boolean isNull() {
		if(mock_object == null || mock_class == null)
			return false;
		else
			return true;
	}
}
