package implementationfinder;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class SearchKeyObject {
	private String classname;
	private List<Integer> argsindex = new ArrayList<>();
	private String fieldname;
	
	private String methodname;
	
	private int initializertype;
	
	private VariableDeclarationStatement stmt;
	
	public SearchKeyObject(String cname, List indexlist, int type) {
		classname = cname;
		for(int i = 0;i < indexlist.size();i++) {
			argsindex.add((Integer) indexlist.get(i));
		}
		initializertype = type;
	}
	
	public SearchKeyObject(String cname, String mname, List indexlist, int type) {
		classname = cname;
		methodname = mname;
		for(int i = 0;i < indexlist.size();i++) {
			argsindex.add((Integer) indexlist.get(i));
		}
		initializertype = type;
	}
	
	public void InitClassName(String cname) {
		classname = cname;
	}
	
	public String getClassName() {
		return classname;
	}
	
	public void UpdateArgIndex(int index) {
		argsindex.add(index);
	}
	
	public List getArgIndex() {
		return argsindex;
	}
	
	public void UpdateInitType(int type) {
		initializertype = type;
	}
	
	public int getInitType() {
		return initializertype;
	}
	
	public void UpdateMethodName(String mname) {
		methodname = mname;
	}
	
	public String getMethodName() {
		return methodname;
	}
	
	public void UpdateFieldName(String fname) {
		fieldname = fname;
	}
	
	public String getFieldName() {
		return fieldname;
	}
	
	public void UpdateStatement(VariableDeclarationStatement s) {
		stmt = s;
	}
	
	public VariableDeclarationStatement getStatement() {
		return stmt;
	}
}
