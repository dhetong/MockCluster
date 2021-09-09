package handler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;

public class FieldVisitor extends ASTVisitor {
	public void patternSearch(FieldDeclaration node) {
		Pattern PatternWhen = Pattern.compile("when\\(([^}]*)\\)\\.");
		Matcher matcher = PatternWhen.matcher(node.toString());
		if(matcher.find()) {
			
		}
	}
	
	public boolean visit(FieldDeclaration node) {
		return super.visit(node);
	}
}
