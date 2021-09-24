package handler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Statement;

public class FieldVisitor extends ASTVisitor {	
	private List<FieldDeclaration> fieldstmtlist = new ArrayList<>();
	
	public void patternSearch(FieldDeclaration node) {	
		Pattern PatternWhen = Pattern.compile("when\\(([^}]*)\\)\\.");
		Matcher MatcherWhen = PatternWhen.matcher(node.toString());
		if(MatcherWhen.find()) {
			//find no when statement in field(Cayenne)
		}
		
		Pattern PatternMock = Pattern.compile("mock\\(");
		Matcher MatcherMock = PatternMock.matcher(node.toString());
		if(MatcherMock.find()) {
			//find no mock statement in field(Cayenne)
		}
	}
	
	public boolean visit(FieldDeclaration node) {
		fieldstmtlist.add(node);
		patternSearch(node);
		return super.visit(node);
	}
	
	public List<FieldDeclaration> getFieldStmt() {
		return fieldstmtlist;
	}
}
