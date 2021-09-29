package common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.Type;

import handler.AssignmentVisitor;
import handler.ThenPatternHandler;
import patternnodeinfo.MockInfo;
import patternnodeinfo.MockInitInfo;

public class StatementFilter {
	public MockInfo whenstmFilter(Statement s) {
		if(s.getNodeType() == Statement.EXPRESSION_STATEMENT) {
			Pattern PatternWhen = Pattern.compile("when\\(([^}]*)\\)\\.");
			Matcher matcher = PatternWhen.matcher(s.toString());
			if(matcher.find()) {
				if(s.toString().contains("doReturn")) {
				}
				else if(s.toString().contains("thenReturn")) {
					ThenPatternHandler thenhandler = new ThenPatternHandler();
					s.accept(thenhandler);
					
					return thenhandler.getMockInfo();
				}
			}
		}
		return null;
	}
	
	public MockInitInfo mockstmFilter(Statement s) {
		if(s.getNodeType() == Statement.VARIABLE_DECLARATION_STATEMENT) {
			Pattern PatternMock = Pattern.compile("mock\\(");
			Matcher matcher = PatternMock.matcher(s.toString());
			if(matcher.find()) {
				Type class_type = (Type) ((VariableDeclarationStatement) s).getType();
				
				MockInitInfo mockinitinfo = TypeFilter(class_type, s);
				return mockinitinfo;
			}
		}
		else if(s.getNodeType() == Statement.EXPRESSION_STATEMENT) {
			Pattern PatternMock = Pattern.compile("mock\\(");
			Matcher matcher = PatternMock.matcher(s.toString());
			if(matcher.find()) {
				if(s.toString().contains("when(")) {
				}
				else {
					AssignmentVisitor assginmentvisitor = new AssignmentVisitor();
					s.accept(assginmentvisitor);
					
					if(assginmentvisitor.isNull() == true) {
						MockInitInfo mockinitinfo = new MockInitInfo(assginmentvisitor.getName(), 
								assginmentvisitor.getClassName());
						return mockinitinfo;
					}
				}
			}
		}
		return null;
	}
	
	private MockInitInfo TypeFilter(Type class_type, Statement s) {
		if(class_type instanceof SimpleType) {
			VariableDeclarationFragment frag = (VariableDeclarationFragment)
					((VariableDeclarationStatement) s).fragments().get(0);
			
			MockInitInfo mockinitinfo =  new MockInitInfo(frag.getName().toString(), 
					((SimpleType) class_type).getName().toString());
			
			return mockinitinfo;
		}
		else {
			// RowReader<?>
			return null;
		}
	}
}
