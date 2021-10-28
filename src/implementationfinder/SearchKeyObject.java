package implementationfinder;

import java.util.ArrayList;
import java.util.List;

public class SearchKeyObject {
	private String classname;
	private List<Integer> argsindex = new ArrayList<>();
	
	private int initializertype;
	
	public SearchKeyObject(String cname, List indexlist, int type) {
		classname = cname;
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
	
	public void InitIsSimple(int type) {
		initializertype = type;
	}
	
	public int getIsSimple() {
		return initializertype;
	}
}
