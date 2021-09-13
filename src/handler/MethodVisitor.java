package handler;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;

import com.MockInfo;
import com.MockInitInfo;
import com.StatementFilter;

public class MethodVisitor extends ASTVisitor {
	private List<MockInfo> mockinfolist = new ArrayList<>();
	private List<MockInitInfo> mockinitinfolist = new ArrayList<>();
	
	public void patternSearch(MethodDeclaration node) {
		StatementFilter filter = new StatementFilter();
		Block body = node.getBody();
		if(body == null)
			return;
		
		for(Statement s : (List<Statement>) body.statements()) {
//			MockInfo mockinfo = filter.whenstmFilter(s);
//			
//			if(mockinfo != null) {
//				mockinfo.initField(node.getName().toString());
//				mockinfolist.add(mockinfo);
//			}
			
			MockInitInfo mockinitinfo = filter.mockstmFilter(s);
			if(mockinitinfo != null) {
				System.out.println(mockinitinfo.getName());
				System.out.println(mockinitinfo.getClassName());
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
}
