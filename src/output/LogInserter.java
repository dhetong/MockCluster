package output;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.Document;

import implementationfinder.InsertPosInfo;

public class LogInserter {	
	private Document document;

	private ListRewrite lrw;
	
	private void ExpressionExtracter() {
	}
	
	private void NameExtracter() {
	}
	
	private void ArgExtractor() {
	}
	
	public void RewriteFile(CompilationUnit cunit, File file, List<InsertPosInfo> insertinfolist){
		AST ast = cunit.getAST();
		ASTRewrite rewriter = ASTRewrite.create(ast);
		
		for(InsertPosInfo info:insertinfolist) {
			MethodInvocation insertinvocation = ast.newMethodInvocation();
			QualifiedName qName = 
					ast.newQualifiedName(ast.newSimpleName("System"), ast.newSimpleName("out"));
			insertinvocation.setExpression(qName);
			insertinvocation.setName(ast.newSimpleName("println"));
			
			MethodInvocation m_invoked = info.getInsertContent();
			String strpartcontent = "<" + info.getClassName() + "||"
					+ m_invoked.getExpression().toString() + "||"
					+ m_invoked.getName().toString() + "||";
			StringLiteral strpart = ast.newStringLiteral();
			strpart.setLiteralValue(strpartcontent);
			
			MethodInvocation invokedpart = ast.newMethodInvocation();
			
			InfixExpression arg = ast.newInfixExpression();
			arg.setOperator(Operator.PLUS);
			arg.setLeftOperand(strpart);
			arg.setRightOperand(invokedpart);
			insertinvocation.arguments().add(arg);
			System.out.println(insertinvocation.toString());
			
//			System.out.println("Usage of " + name + method + c + ": " + n.m());
		}
	}
}
