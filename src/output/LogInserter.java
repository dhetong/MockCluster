package output;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

import implementationfinder.InsertPosInfo;

public class LogInserter {	
	private AST ast;

	private ListRewrite lrw;
	
	public LogInserter(CompilationUnit cunit) {
		ast = cunit.getAST();
	}
	
	private List ArgExtractor(List args) {
		List invokedpartargs = new ArrayList();
		
		for(int index = 0;index < args.size();index++) {
			if(args.get(index) instanceof SimpleName) {
				SimpleName arg = ast.newSimpleName(args.get(index).toString());
				invokedpartargs.add(arg);
			}
			else if(args.get(index) instanceof StringLiteral){
				StringLiteral arg = ast.newStringLiteral();
				arg.setLiteralValue(args.get(index).toString());
				invokedpartargs.add(arg);
			}
			else if(args.get(index) instanceof ClassInstanceCreation){
				ClassInstanceCreation arg = ast.newClassInstanceCreation();
				
				String tname = ((ClassInstanceCreation) args.get(index)).getType().toString();
				SimpleName typename = ast.newSimpleName(tname);
				SimpleType type = ast.newSimpleType(typename);
				arg.setType(type);
				
				List ciargs = ArgExtractor(((ClassInstanceCreation)args.get(index)).arguments());
				arg.arguments().addAll(ciargs);
				invokedpartargs.add(arg);
			}
			else {
				System.out.println(args.get(index).toString());
			}
		}
		
		return invokedpartargs;
	}
	
	public void RewriteFile(CompilationUnit cunit, File file, Document document, List<InsertPosInfo> insertinfolist){
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
			invokedpart.setExpression(ast.newSimpleName(m_invoked.getExpression().toString()));
			invokedpart.setName(ast.newSimpleName(m_invoked.getName().toString()));
			List invokedpartargs = ArgExtractor(m_invoked.arguments());
			invokedpart.arguments().addAll(invokedpartargs);
			
			InfixExpression arg = ast.newInfixExpression();
			arg.setOperator(Operator.PLUS);
			arg.setLeftOperand(strpart);
			arg.setRightOperand(invokedpart);
			insertinvocation.arguments().add(arg);
			ExpressionStatement printstatement = ast.newExpressionStatement(insertinvocation);
			
			Block block = info.getInsertBlock();
			if (block == null  || block.statements().size()==0) {
				System.out.println("error");
		    	continue;
			}
			
			ListRewrite listrewrite = rewriter.getListRewrite(block, Block.STATEMENTS_PROPERTY);
			
			System.out.println(block.toString());
			System.out.println(info.getInsertPos().toString());
			System.out.println("------------------------------------");
			//switch Statement, 3 cases
			
			if(info.getAfter() == true)
				listrewrite.insertAfter(printstatement, info.getInsertPos(), null);
			
			if(info.getBefore() == true)
				listrewrite.insertBefore(printstatement, info.getInsertPos(), null);
		}
		
		TextEdit edits = rewriter.rewriteAST(document,null);
	}
}
