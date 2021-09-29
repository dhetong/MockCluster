package handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;

import common.StatementFilter;
import patternnodeinfo.MockInfo;
import patternnodeinfo.MockInitInfo;

public class MethodVisitor extends ASTVisitor {
	private List<MockInfo> mockinfolist = new ArrayList<>();
	private List<MockInitInfo> mockinitinfolist = new ArrayList<>();
	
	private HashMap<String,List<Statement>> stmtdict = new HashMap<>();
	private HashMap<String,List> paradict = new HashMap<>();
	
	public void patternSearch(MethodDeclaration node) {
		StatementFilter filter = new StatementFilter();
		Block body = node.getBody();
		if(body == null)
			return;
		
		//match statements to method
		String field = node.getName().toString();
		List<Statement> stmtlist = node.getBody().statements();
		stmtdict.put(field, stmtlist);
		
		//create argument list for method
		List paralist = node.parameters();
		paradict.put(field, paralist);
		
		for(Statement s : (List<Statement>) body.statements()) {
			MockInfo mockinfo = filter.whenstmFilter(s);
			
			if(mockinfo != null) {
				mockinfo.initField(node.getName().toString());
				
				//add statement into mockinfo
				mockinfo.InitStmt(s.toString());
				mockinfo.InitPosition(s.getStartPosition());
				
				mockinfolist.add(mockinfo);
			}
			
			MockInitInfo mockinitinfo = filter.mockstmFilter(s);
			if(mockinitinfo != null) {
				mockinitinfo.initField(node.getName().toString());
				
				//add statement into mockinitinfo
				mockinitinfo.InitStmt(s.toString());
				mockinitinfo.InitPosition(s.getStartPosition());
				
				mockinitinfolist.add(mockinitinfo);
			}
		}
	}
	
	public boolean visit(MethodDeclaration node) {
		patternSearch(node);
		
		return super.visit(node);
	}
	
	public List<MockInfo> getMockInfoList(){
		return mockinfolist;
	}
	
	public List<MockInitInfo> getMockInitInfoList(){
		return mockinitinfolist;
	}
	
	public HashMap<String,List<Statement>> getStmtDict(){
		return stmtdict;
	}
	
	public HashMap<String,List> getParaDict(){
		return paradict;
	}
}
