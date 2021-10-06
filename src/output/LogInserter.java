package output;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.Document;

public class LogInserter {
	private List<Statement> stmtlist = new ArrayList<>();
	
	private Document document;

	private ListRewrite lrw;
	
	public LogInserter(AST ast, Document d, File f) {
		this.document = d;
	}
	
	public void RewriteFile(CompilationUnit cunit, File file){
		AST ast = cunit.getAST();
		ASTRewrite rewriter = ASTRewrite.create(ast);
		
		for(Statement s:stmtlist) {
			MethodInvocation insertinvocation = ast.newMethodInvocation();
			QualifiedName qName = 
					ast.newQualifiedName(ast.newSimpleName("System"), ast.newSimpleName("out"));
			insertinvocation.setExpression(qName);
			insertinvocation.setName(ast.newSimpleName("println"));
		}
	}
}
