package com;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import com.ReturnType;

public class LinkObject {	
	private List<LinkedList> simplevaluepattern = new ArrayList<>();
	private List<LinkedList> mockvaluepattern = new ArrayList<>();
	private List<LinkedList> objectvaluepattern = new ArrayList<>();
	private List<LinkedList> methodvaluepattern = new ArrayList<>();
	
	private List<LinkedList> mocklinkset = new ArrayList<>();
	
	private List<MockInfo> mockinfolist = new ArrayList<>();
	private List<MockInitInfo> mockinitinfolist = new ArrayList<>();
	
	public void InitializeLinker(List<MockInfo> infolist, List<MockInitInfo> initinfolist) {
		mockinfolist.addAll(infolist);
		mockinitinfolist.addAll(initinfolist);
	}
	
	public void InitToWhen() {		
		for(int index_init = 0;index_init < mockinitinfolist.size();index_init++) {			
			for(int index_mock = 0;index_mock < mockinfolist.size();index_mock++) {
				MockInitInfo mockinitinfo = mockinitinfolist.get(index_init);
				MockInfo mockinfo = mockinfolist.get(index_mock);
				String initfield = mockinitinfo.getField();
				String whenfield = mockinfo.getField();
				if(initfield.equals(whenfield)) {
					String when_name = mockinfo.getObject();
					String init_name = mockinitinfo.getName();
					
					if(init_name.equals(when_name)) {
						mockinitinfolist.get(index_init).initHasWhen(true);
						mockinitinfo.initHasWhen(true);
						
						LinkedList<Info> pattern_list = new LinkedList<>();
						pattern_list.addFirst(mockinitinfo);
						pattern_list.addLast(mockinfo);
						
						mocklinkset.add(pattern_list);
					}
				}
			}
		}
	}
	
	//identify when pattern using simple value as return value
	public void ReturnValueFilter() {
		for(int index = 0;index < mocklinkset.size();index++) {
			LinkedList<Info> patternlist = mocklinkset.get(index);
			int tail = patternlist.size()-1;
			MockInfo mockinfo = (MockInfo) patternlist.get(tail);
			
			if(mockinfo.getType() == ReturnType.STR_TYPE ||
					mockinfo.getType() == ReturnType.BOOLEAN_TYPE ||
					mockinfo.getType() == ReturnType.NUM_TYPE ||
					mockinfo.getType() == ReturnType.QUALIFIED_NAME_TYPE ||
					mockinfo.getType() == ReturnType.NULL_TYPE ||
					mockinfo.getType() == ReturnType.CLASS_INSTANCE_CREATION_TYPE ||
					mockinfo.getType() == ReturnType.TYPE_LITERAL_TYPE ||
					mockinfo.getType() == ReturnType.ARRAY_CREATION_TYPE ||
					mockinfo.getType() == ReturnType.CAST_EXPRESSION_TYPE) {
				//cast expression and array creation type need no check in cayenne
				((MockInfo) patternlist.get(tail)).initReturnMock(false); //return object is not a mock object
				simplevaluepattern.add(patternlist);
			}
			else if(mockinfo.getType() == ReturnType.SIMPLE_NAME_TYPE) {
				//identify whether when pattern using mock object as return value
				String content = mockinfo.getContent();
				String field = mockinfo.getField();
				boolean flag = false; //mark whether the return object can match a mock object
				
				for(int index_init = 0;index_init < mockinitinfolist.size();index_init++) {
					String mock_name = mockinitinfolist.get(index_init).getName();
					String mock_field = mockinitinfolist.get(index_init).getField();
					if(!mock_field.equals(field))
						continue;
					if(!mock_name.equals(content))
						continue;
					
					patternlist.add(mockinitinfolist.get(index_init)); //add return info to pattern list
					((MockInfo) patternlist.get(tail)).initReturnMock(true); //return object is a mock object
					flag = true;
					mockvaluepattern.add(patternlist);
					break;
				}
				
				if(flag == false) {
					((MockInfo) patternlist.get(tail)).initReturnMock(false);//return object is not a mock object
					objectvaluepattern.add(patternlist);
				}
			}
			else if(mockinfo.getType() == ReturnType.METHOD_TYPE) {
				//temporary, code after finishing SIMPLE_NAME_TYPE
				((MockInfo) patternlist.get(tail)).initReturnMock(false);
				methodvaluepattern.add(patternlist);
			}
			else {
				System.out.println("error");
			}
		}
	}
	
	public void MockValueMather() {
		for(int index = 0;index < mockvaluepattern.size();index++) {
			LinkedList<Info> patternlist = mockvaluepattern.get(index);
			int tail = patternlist.size()-1;
			MockInitInfo mockinitinfo = (MockInitInfo) patternlist.get(tail);
			if(mockinitinfo.hasWhen() == false) {
				continue;
			}
			String field = mockinitinfo.getField();
			String name = mockinitinfo.getName();
			ListIndexInfo indexinfo = new ListIndexInfo();
			for(int index_list = 0;index_list < mocklinkset.size();index_list++) {
				LinkedList<Info> patternlist_set = mocklinkset.get(index_list);
				MockInitInfo mockinitinfo_set = (MockInitInfo) patternlist_set.get(0);
				String field_set = mockinitinfo_set.getField();
				String name_set = mockinitinfo_set.getName();
				
				if(!field.equals(field_set))
					continue;
				if(!name.equals(name_set))
					continue;
				indexinfo.InsertIndex(index_list);
			}
			mockvaluepattern.get(index).addLast(indexinfo);
		}
	}
	
	public void ObjectValueMatcher(HashMap<String,List<Statement>> stmtdict, 
			HashMap<String,List> paradict,
			List<FieldDeclaration> fieldstmtlist) {
		for(int index = 0;index < objectvaluepattern.size();index++) {
			LinkedList<Info> patternlist = objectvaluepattern.get(index);
			int tail = patternlist.size()-1;
			MockInfo mockinfo = (MockInfo) patternlist.get(tail);
			String field = mockinfo.getField();
			String name = mockinfo.getContent();
			
			boolean flag = false;
			
			if(stmtdict.keySet().contains(field)) {
				List<Statement> stmtlist = stmtdict.get(field);
				for(Statement s:stmtlist) {
					if(s.getNodeType() == Statement.VARIABLE_DECLARATION_STATEMENT) {
						if(s.toString().contains(name)) {
							VarDecHandler((VariableDeclarationStatement) s);
							flag = true;
						}
					}
				}
			}
			
			if(paradict.keySet().contains(field)) {
				List paralist = paradict.get(field);
				for(int index_p = 0;index_p < paralist.size();index_p++) {
					if(paralist.get(index_p).toString().contains(name)) {
						flag = true;
					}
				}
			}
			
			for(int index_f = 0;index_f < fieldstmtlist.size();index_f++) {
				FieldDeclaration node = fieldstmtlist.get(index_f);
				if(node.toString().contains(name)) {
					flag = true;
				}
			}
		}
	}
	
	private void VarDecHandler(VariableDeclarationStatement s) {
		Type type = s.getType();
		List fraglist = s.fragments();
		
		if(type instanceof SimpleType) {
			String cname = type.toString();
		}
		else if(type instanceof ParameterizedType) {
			String cname = type.toString();
		}
		
		for(int index = 0;index < fraglist.size();index++) {
			if(fraglist.get(index) instanceof VariableDeclarationFragment) {
				VariableDeclarationFragment vardecfrag = 
						(VariableDeclarationFragment) fraglist.get(index);
			}
			else {
				//all of the fragment nodes are VariableDeclarationFragment
			}
		}
	}
	
	public void MockInfoLink() {
		List<LinkedList> linkset = new ArrayList<>();
		
		for(int index_f = 0;index_f < mocklinkset.size();index_f++) {
			boolean hasMockReturn = false; //mark whether the return value of when pattern is a mock object
			
			LinkedList<Info> patternlist_head = mocklinkset.get(index_f);
			int index_head = patternlist_head.size()-1;
			if(patternlist_head.get(index_head) instanceof MockInitInfo)
				continue;
			MockInfo mockinfo = (MockInfo) patternlist_head.get(index_head);
			
			for(int index_b = 0;index_b < mocklinkset.size();index_b++) {
				if(index_b == index_f)
					continue;
				LinkedList<Info> patternlist_tail = mocklinkset.get(index_b);
				if(patternlist_tail.get(0) instanceof MockInfo)
					continue;
				MockInitInfo mockinitinfo = (MockInitInfo) patternlist_tail.get(0);
				
				if(isLinked(mockinfo, mockinitinfo)) {
					hasMockReturn = true;
				}
			}
			
			if(hasMockReturn == false) {
				// if the return value is not a mock object
				
			}
		}
	}
	
	public boolean isLinked(MockInfo head, MockInitInfo tail) {
		if(head.getType() != ReturnType.SIMPLE_NAME_TYPE)
			return false;
		if(!head.getContent().equals(tail.getName()))
			return false;
		return true;
	}
}
