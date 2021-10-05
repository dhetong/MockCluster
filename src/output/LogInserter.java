package output;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.Document;

public class LogInserter {
	private List<Statement> stmtlist = new ArrayList<>();
	
	private Document document;
	private File file;
	
	private ASTRewrite rewriter;
	private ListRewrite lrw;
	
	public LogInserter(AST ast, Document d, File f) {
		this.rewriter = ASTRewrite.create(ast);
		this.document = d;
		this.file = f;
	}
	
	public void UpdateInsertList() {
	}
	
	public void RewriteFile(){
	}
}
