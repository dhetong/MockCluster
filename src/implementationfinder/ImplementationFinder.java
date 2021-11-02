package implementationfinder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

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
	private List<SearchKeyObject> objectkeylist = new ArrayList<>();
	
	public List<SearchKey> getKeyList(){
		return keylist;
	}
	
	public List<SearchKeyObject> getObjectKeyList(){
		return objectkeylist;
	}
	
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
	
	private boolean IndexCheck(List list_0, List list_1) {		
		if(list_0.size() != list_1.size())
			return false;
		for(int i = 0;i < list_0.size();i++) {
			if(list_0.get(i) != list_1.get(i))
				return false;
		}
		
		return true;
	}
	
	private boolean IsEqualObject(SearchKeyObject key0, SearchKeyObject key1) {
		if(key0.getInitType() != key1.getInitType())
			return false;
		if(!key0.getClassName().equals(key1.getClassName()))
			return false;
		
		List indexlist0 = key0.getArgIndex();
		List indexlist1 = key1.getArgIndex();
		
		if(!IndexCheck(indexlist0, indexlist1))
			return false;
		
		if(key0.getInitType() == InitializerType.METHOD_INVOCATION) {
			if(!key0.getMethodName().equals(key1.getMethodName()))
				return false;
		}
		
		return true;
	}
	
	private boolean InListObject(SearchKeyObject key) {
		for(int index = 0;index < objectkeylist.size();index++) {
			SearchKeyObject tmp = objectkeylist.get(index);
			if(IsEqualObject(key, tmp))
				return true;
		}
		
		return false;
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
	
	private List getSimpleIndex(List args) {
		List<Integer> index_list = new ArrayList<>();
		
		for(int index = 0;index < args.size();index++) {
			if(args.get(index) instanceof StringLiteral) {
				index_list.add(index);
			}
			else if(args.get(index) instanceof NumberLiteral) {
				index_list.add(index);
			}
		}
		
		return index_list;
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
					VariableDeclarationStatement s = (VariableDeclarationStatement) objectinfo.getStmt_ast();
					String cname = s.getType().toString();
					VariableDeclarationFragment frag = (VariableDeclarationFragment) s.fragments().get(0);
					if(frag.getInitializer() instanceof ClassInstanceCreation) {
						ClassInstanceCreation instancecreation = (ClassInstanceCreation) frag.getInitializer();
						List argsindex = getSimpleIndex(instancecreation.arguments());
						String fieldname = objectinfo.getField();
					
						SearchKeyObject key = new SearchKeyObject(cname, argsindex, InitializerType.INSTANCE_CREATION);
						key.UpdateFieldName(fieldname);
						key.UpdateStatement(s);
						if(InListObject(key) == true)
							continue;
						objectkeylist.add(key);
					}
					else if(frag.getInitializer() instanceof MethodInvocation){
						MethodInvocation methodinvocation = (MethodInvocation) frag.getInitializer();
						String mname = methodinvocation.getName().toString();
						List argsindex = getSimpleIndex(methodinvocation.arguments());
						String fieldname = objectinfo.getField();
						
						SearchKeyObject key = new SearchKeyObject(cname, mname, argsindex, InitializerType.METHOD_INVOCATION);
						key.UpdateFieldName(fieldname);
						key.UpdateStatement(s);
						if(InListObject(key) == true)
							continue;
						objectkeylist.add(key);
					}
					else {
						System.out.println(s.toString());
					}
				}
				else if(flag == 2) {
					//the return value is a String
					MockInitInfo mockinitinfo = (MockInitInfo) pattern.get(0);
					MockInfo mockinfo = (MockInfo) pattern.get(objectid-1);
					
					String classname = mockinitinfo.getClassName();
					String methodname = mockinfo.getMethod();
					String fieldname = mockinfo.getField();
					String returntype = "String";
					
					SearchKey key = new SearchKey(classname, methodname, fieldname);
					key.InitReturnType(returntype);
					if(InList(key) == true)
						continue;
					
					keylist.add(key);
				}
				else {
					System.out.println("ERROR");
				}
			}
			else {
				System.out.println("ERROR");
			}
		}
	}
}
