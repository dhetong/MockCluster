package handler;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;

import com.MockInfo;
import com.StatementFilter;

public class MethodVisitor extends ASTVisitor {
	private List<MockInfo> mockinfolist = new ArrayList<>();
	
	public void patternSearch(MethodDeclaration node) {
		StatementFilter filter = new StatementFilter();
		Block body = node.getBody();
		if(body == null)
			return;
		
		for(Statement s : (List<Statement>) body.statements()) {
			MockInfo mockinfo = filter.stmFilter(s);
			
			if(mockinfo != null) {
				mockinfo.initField(node.getName().toString());
				mockinfolist.add(mockinfo);
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
}
