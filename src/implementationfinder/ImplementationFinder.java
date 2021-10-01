package implementationfinder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import patternnodeinfo.Info;
import patternnodeinfo.MockInfo;
import patternnodeinfo.MockInitInfo;

//need to consider the impact of mocking pattern

public class ImplementationFinder {
	private List<SearchKey> keylist = new ArrayList<>();
	
	private boolean InList(SearchKey key) {
		boolean flag = false;
		
		for(int index = 0;index < keylist.size();index++) {
			SearchKey tmp = keylist.get(index);
			if(tmp.getClassName().equals(key.getClassName()) &&
					tmp.getMethodName().equals(key.getMethodName()) &&
					tmp.getFieldName().equals(key.getFieldName())) {
				flag = true;
				break;
			}
		}
		
		return flag;
	}
	
	public void UpdateKeyList(List<LinkedList> patterns) {
		for(int index = 0;index < patterns.size();index++) {
			List<Info> pattern = patterns.get(index);
			
			MockInitInfo mockinitinfo = (MockInitInfo) pattern.get(0);
			String classname = mockinitinfo.getClassName();
			MockInfo mockinfo = (MockInfo) pattern.get(1);
			String methodname = mockinfo.getMethod();
			String fieldname = mockinfo.getField();
			SearchKey key = new SearchKey(classname, methodname, fieldname);
			if(InList(key) == true)
				continue;
			
			keylist.add(key);
		}
	}
	
	public List<SearchKey> getKeyList(){
		return keylist;
	}
	
	public void PrintList() {
		for(int index = 0;index < keylist.size();index++) {
			SearchKey key = keylist.get(index);
			System.out.println(key.getClassName());
			System.out.println(key.getMethodName());
		}
	}
}
