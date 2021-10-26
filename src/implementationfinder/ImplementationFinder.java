package implementationfinder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import patternnodeinfo.Info;
import patternnodeinfo.MockInfo;
import patternnodeinfo.MockInitInfo;
import patternnodeinfo.ObjectInfo;
import patternnodeinfo.ObjectInitType;
import patternnodeinfo.ReturnType;
import patternnodeinfo.ParaInfo;
import patternnodeinfo.FieldObjectInfo;

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
	
	private String getReturnType(MockInfo mockinfo) {
		String type = "";
		
		if(mockinfo.getType() == ReturnType.STR_TYPE)
			type = "String";
		else if(mockinfo.getType() == ReturnType.SIMPLE_NAME_TYPE)
			type = "SimpleName";
		else if(mockinfo.getType() == ReturnType.BOOLEAN_TYPE)
			type = "Boolean";
		else if(mockinfo.getType() == ReturnType.NUM_TYPE)
			type = "Number";
		else if(mockinfo.getType() == ReturnType.METHOD_TYPE)
			type = "Method";
		else if(mockinfo.getType() == ReturnType.QUALIFIED_NAME_TYPE)
			type = "QualifiedName";
		else if(mockinfo.getType() == ReturnType.ARRAY_CREATION_TYPE)
			type = "ArrayCreation";
		else if(mockinfo.getType() == ReturnType.CAST_EXPRESSION_TYPE)
			type = "CastExpression";
		else if(mockinfo.getType() == ReturnType.NULL_TYPE)
			type = "Null";
		else if(mockinfo.getType() == ReturnType.CLASS_INSTANCE_CREATION_TYPE)
			type = "ClassInstanceCreation";
		else if(mockinfo.getType() == ReturnType.TYPE_LITERAL_TYPE)
			type = "TypeLiteral";
		
		return type;
	}
	
	public void UpdateKeyList(List<LinkedList> patterns) {
		for(int index = 0;index < patterns.size();index++) {
			List<Info> pattern = patterns.get(index);
			
			MockInitInfo mockinitinfo = (MockInitInfo) pattern.get(0);
			String classname = mockinitinfo.getClassName();
			MockInfo mockinfo = (MockInfo) pattern.get(1);
			String methodname = mockinfo.getMethod();
			String fieldname = mockinfo.getField();
			String returntype = getReturnType(mockinfo);
			SearchKey key = new SearchKey(classname, methodname, fieldname);
			key.InitReturnType(returntype);
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
	
	private int HasSimple(LinkedList<Info> pattern_list) {
		int flag = 0;		
		int tail = pattern_list.size()-1;
		if(pattern_list.get(tail) instanceof ObjectInfo) {
			ObjectInfo objectinfo = (ObjectInfo) pattern_list.get(tail);
			if(objectinfo.getInfoType() == ObjectInitType.STR_TYPE)
				flag = 2;
			else {
				if(objectinfo.ArgsHasSimple())
					flag = 1;
			}
		}		
		return flag;
	}
	
	public void UpdateKeyListObject(List<LinkedList> patterns) {
		for(int index = 0;index < patterns.size();index++) {
			LinkedList<Info> pattern = patterns.get(index);
			
			int flag = HasSimple(pattern);
			if(flag == 0)
				continue;
			
			int objectid = pattern.size()-1;
			if(pattern.get(objectid) instanceof ObjectInfo) {
				ObjectInfo objectinfo = (ObjectInfo) pattern.get(objectid);
				if(flag == 1) {
					//the return value is an object
					System.out.println(objectinfo.getStmt_ast().toString());
				}
				else {
					//the return value is a String
					System.out.println(objectinfo.getStmt());
					System.out.println(objectinfo.getContent());
				}
			}
			else {
				System.out.println("ERROR");
			}
		}
	}
}
