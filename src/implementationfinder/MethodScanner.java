package implementationfinder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

//need to consider the impact of mocking pattern

public class MethodScanner extends ASTVisitor {
	private ImplementationFinder finder;
	private List<SearchKeyVar> varkeylist = new ArrayList<>();
	
	private List<InsertPosInfo> insertposlist = new ArrayList<>();
	
	private List<String> methodlist = new ArrayList<>();
	private List<String> typelist = new ArrayList<>();
	
	private void ExtractMethodName(Statement s){
		methodlist.clear();
		typelist.clear();
		
		if(s instanceof VariableDeclarationStatement) {
			Pattern PatternMock = Pattern.compile("mock\\("); //remove the mock declaration statement
			Matcher matcher = PatternMock.matcher(s.toString());
			if(!matcher.find()) {
				List<SearchKey> keylist = finder.getKeyList();
				for(SearchKey key:keylist) {
					String classname = key.getClassName();
					Type type = ((VariableDeclarationStatement) s).getType();
					if(type.toString().equals(classname)) {
						String method = key.getMethodName();
						methodlist.add(method);
						String returntype = key.getReturnType();
						typelist.add(returntype);
					}
				}
			}
		}		
	}
	
	private List<String> ExtractMethodName_withfield(Statement s){
		List<String> methodlist = new ArrayList<>();
		return methodlist;
	}
	
	private String ExtractVarName(List fraglist) {
		String varname = null;
		
		for(int i = 0;i < fraglist.size();i++) {
			if(fraglist.get(i) instanceof VariableDeclarationFragment) {
				VariableDeclarationFragment vardecfrag = 
						(VariableDeclarationFragment) fraglist.get(i);
				varname = vardecfrag.getName().toString();
			}
		}
		
		return varname;
	}
	
	private boolean InVarList(SearchKeyVar key) {
		boolean flag = false;
		for(SearchKeyVar k:varkeylist) {
			if(k.getVarName().equals(key.getVarName()) && 
					k.getMethodName().equals(key.getMethodName()))
				flag = true;
		}
		return flag;
	}
	
	private void VarFinder(MethodDeclaration node) {
		Block body = node.getBody();
		if(body == null)
			return;
		
		List<Statement> stmtlist = node.getBody().statements();
		for(Statement s:stmtlist) {
			ExtractMethodName(s);
			if(methodlist.size() == 0) //select the statement which declares the variable(the statement cannot be a mock pattern)
				continue;
			
			String classname = ((VariableDeclarationStatement) s).getType().toString();
			List fraglist = ((VariableDeclarationStatement) s).fragments();
			String varname = ExtractVarName(fraglist);
			if(varname == null)
				continue;
			
			for(int i = 0;i < methodlist.size();i++) {
				SearchKeyVar key = new SearchKeyVar(varname, methodlist.get(i));
				key.UpdateClassName(classname);
				key.UpdateReturnType(typelist.get(i));
				if(InVarList(key) == false)
					varkeylist.add(key);
			}
		}
	}
	
	private boolean ParentStmtType(InsertPosInfo info) {
		boolean flag = true;
		
		if(info.getInsertPos() instanceof ReturnStatement||
				info.getInsertPos() instanceof IfStatement||
				info.getInsertPos() instanceof WhileStatement)
			flag = false;
		
		return flag;
	}
	
	private boolean InvokedMatcher(Statement s, MethodDeclaration node) {
		boolean flag = false;
		
		for(SearchKeyVar key:varkeylist) {
			String varname = key.getVarName();
			String methodname = key.getMethodName();
			String classname = key.getClassName();
			String returntype = key.getReturnType();
			String invoked = varname + "\\." + methodname + "\\(";
			
			Pattern invokedpattern = Pattern.compile(invoked);
			Matcher invokedmatcher = invokedpattern.matcher(s.toString());
			if(invokedmatcher.find() && 
					!s.toString().contains("when") && 
					!s.toString().contains("assert")) {
				if(s instanceof ExpressionStatement) {
					MethodInvocationVisitor invokedvisitor = 
							new MethodInvocationVisitor(varname, methodname, classname, returntype, s);
					s.accept(invokedvisitor);
					
					List<InsertPosInfo> listtmp = invokedvisitor.getInsertPos();
					for(int index  = 0;index < listtmp.size();index++) {
						if(this.ParentStmtType(listtmp.get(index))) {
							listtmp.get(index).InitAfter(true);
							listtmp.get(index).InitBefore(false);
						}
						else {
							listtmp.get(index).InitAfter(false);
							listtmp.get(index).InitBefore(true);
						}
						
						listtmp.get(index).InitMethodDec(node);
					}					
					insertposlist.addAll(listtmp);
				}
				else if(s instanceof ReturnStatement) {
					MethodInvocationVisitor invokedvisitor = 
							new MethodInvocationVisitor(varname, methodname, classname, returntype, s);
					s.accept(invokedvisitor);
					
					List<InsertPosInfo> listtmp = invokedvisitor.getInsertPos();
					for(int index  = 0;index < listtmp.size();index++) {
						if(this.ParentStmtType(listtmp.get(index))) {
							listtmp.get(index).InitAfter(true);
							listtmp.get(index).InitBefore(false);
						}
						else {
							listtmp.get(index).InitAfter(false);
							listtmp.get(index).InitBefore(true);
						}
						
						listtmp.get(index).InitMethodDec(node);
					}					
					insertposlist.addAll(listtmp);
				}
				else if(s instanceof VariableDeclarationStatement){
					MethodInvocationVisitor invokedvisitor = 
							new MethodInvocationVisitor(varname, methodname, classname, returntype, s);
					s.accept(invokedvisitor);
					
					List<InsertPosInfo> listtmp = invokedvisitor.getInsertPos();
					for(int index  = 0;index < listtmp.size();index++) {
						if(this.ParentStmtType(listtmp.get(index))) {
							listtmp.get(index).InitAfter(true);
							listtmp.get(index).InitBefore(false);
						}
						else {
							listtmp.get(index).InitAfter(false);
							listtmp.get(index).InitBefore(true);
						}
						
						listtmp.get(index).InitMethodDec(node);
					}					
					insertposlist.addAll(listtmp);
				}
				else if(s instanceof IfStatement){
					MethodInvocationVisitor invokedvisitor = 
							new MethodInvocationVisitor(varname, methodname, classname, returntype, s);
					s.accept(invokedvisitor);
					
					List<InsertPosInfo> listtmp = invokedvisitor.getInsertPos();
					for(int index  = 0;index < listtmp.size();index++) {
						if(this.ParentStmtType(listtmp.get(index))) {
							listtmp.get(index).InitAfter(true);
							listtmp.get(index).InitBefore(false);
						}
						else {
							listtmp.get(index).InitAfter(false);
							listtmp.get(index).InitBefore(true);
						}
						
						listtmp.get(index).InitMethodDec(node);
					}					
					insertposlist.addAll(listtmp);
				}
				else if(s instanceof ForStatement){
					MethodInvocationVisitor invokedvisitor = 
							new MethodInvocationVisitor(varname, methodname, classname, returntype, s);
					s.accept(invokedvisitor);
					
					List<InsertPosInfo> listtmp = invokedvisitor.getInsertPos();
					for(int index  = 0;index < listtmp.size();index++) {
						if(this.ParentStmtType(listtmp.get(index))) {
							listtmp.get(index).InitAfter(true);
							listtmp.get(index).InitBefore(false);
						}
						else {
							listtmp.get(index).InitAfter(false);
							listtmp.get(index).InitBefore(true);
						}
						
						listtmp.get(index).InitMethodDec(node);
					}					
					insertposlist.addAll(listtmp);
				}
				else if(s instanceof EnhancedForStatement){
					MethodInvocationVisitor invokedvisitor = 
							new MethodInvocationVisitor(varname, methodname, classname, returntype, s);
					s.accept(invokedvisitor);
					
					List<InsertPosInfo> listtmp = invokedvisitor.getInsertPos();
					for(int index  = 0;index < listtmp.size();index++) {
						if(this.ParentStmtType(listtmp.get(index))) {
							listtmp.get(index).InitAfter(true);
							listtmp.get(index).InitBefore(false);
						}
						else {
							listtmp.get(index).InitAfter(false);
							listtmp.get(index).InitBefore(true);
						}
						
						listtmp.get(index).InitMethodDec(node);
					}					
					insertposlist.addAll(listtmp);
				}
				else if(s instanceof TryStatement){
					MethodInvocationVisitor invokedvisitor = 
							new MethodInvocationVisitor(varname, methodname, classname, returntype, s);
					s.accept(invokedvisitor);
					
					List<InsertPosInfo> listtmp = invokedvisitor.getInsertPos();
					for(int index  = 0;index < listtmp.size();index++) {
						if(this.ParentStmtType(listtmp.get(index))) {
							listtmp.get(index).InitAfter(true);
							listtmp.get(index).InitBefore(false);
						}
						else {
							listtmp.get(index).InitAfter(false);
							listtmp.get(index).InitBefore(true);
						}
						
						listtmp.get(index).InitMethodDec(node);
					}					
					insertposlist.addAll(listtmp);
				}
//				else if(s instanceof SwitchStatement){
//					MethodInvocationVisitor invokedvisitor = 
//							new MethodInvocationVisitor(varname, methodname, classname, returntype, s);
//					s.accept(invokedvisitor);
//					
//					List<InsertPosInfo> listtmp = invokedvisitor.getInsertPos();
//					for(int index  = 0;index < listtmp.size();index++) {
//						if(this.ParentStmtType(listtmp.get(index))) {
//							listtmp.get(index).InitAfter(true);
//							listtmp.get(index).InitBefore(false);
//						}
//						else {
//							listtmp.get(index).InitAfter(false);
//							listtmp.get(index).InitBefore(true);
//						}
//						
//						listtmp.get(index).InitMethodDec(node);
//					}					
//					insertposlist.addAll(listtmp);
//				}
				else if(s instanceof WhileStatement){
					MethodInvocationVisitor invokedvisitor = 
							new MethodInvocationVisitor(varname, methodname, classname, returntype, s);
					s.accept(invokedvisitor);
					
					List<InsertPosInfo> listtmp = invokedvisitor.getInsertPos();
					for(int index  = 0;index < listtmp.size();index++) {
						if(this.ParentStmtType(listtmp.get(index))) {
							listtmp.get(index).InitAfter(true);
							listtmp.get(index).InitBefore(false);
						}
						else {
							listtmp.get(index).InitAfter(false);
							listtmp.get(index).InitBefore(true);
						}
						
						listtmp.get(index).InitMethodDec(node);
					}					
					insertposlist.addAll(listtmp);
				}
				else if(s instanceof SynchronizedStatement){
					MethodInvocationVisitor invokedvisitor = 
							new MethodInvocationVisitor(varname, methodname, classname, returntype, s);
					s.accept(invokedvisitor);
					
					List<InsertPosInfo> listtmp = invokedvisitor.getInsertPos();
					for(int index  = 0;index < listtmp.size();index++) {
						if(this.ParentStmtType(listtmp.get(index))) {
							listtmp.get(index).InitAfter(true);
							listtmp.get(index).InitBefore(false);
						}
						else {
							listtmp.get(index).InitAfter(false);
							listtmp.get(index).InitBefore(true);
						}
						
						listtmp.get(index).InitMethodDec(node);
					}					
					insertposlist.addAll(listtmp);
				}
				else {
//					System.out.println(varname);
//					System.out.println(methodname);
//					System.out.println(s.toString());
//					System.out.println("--------------------------------------");
				}
			}
		}
		return flag;
	}
	
	private void StatementFinder(MethodDeclaration node) {
		Block body = node.getBody();
		if(body == null)
			return;
		
		List<Statement> stmtlist = node.getBody().statements();
		for(Statement s:stmtlist) {
			InvokedMatcher(s, node);
		}
	}
	
	public MethodScanner(ImplementationFinder f) {
		finder = f;
	}
	
	public List<InsertPosInfo> getInsertInfo(){
		return insertposlist;
	}
	
	public boolean visit(MethodDeclaration node) {
		VarFinder(node);
		StatementFinder(node);
		return super.visit(node);
	}
}
