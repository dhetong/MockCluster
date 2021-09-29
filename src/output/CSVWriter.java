package output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import patternnodeinfo.FieldObjectInfo;
import patternnodeinfo.Info;
import patternnodeinfo.ListIndexInfo;
import patternnodeinfo.MockInfo;
import patternnodeinfo.MockInitInfo;
import patternnodeinfo.ObjectInfo;
import patternnodeinfo.ObjectInitType;
import patternnodeinfo.ParaInfo;
import patternnodeinfo.ReturnType;

import com.opencsv.*;

public class CSVWriter {
	private File mockcsv = new File("mock.csv");
	private File objectcsv = new File("object.csv");
	
	private String MockInitInfoPrint(MockInitInfo mockinitinfo) {
		String field = mockinitinfo.getField();
		String name = mockinitinfo.getName();
		String classname = mockinitinfo.getClassName();
		
		String printstr = "Location: " + field + '\n' + 
				"Variable Name: " + name + '\n' + 
				"Mocked Class: " + classname;
		return printstr;
	}
	
	private String ReturnTypePrint(int type) {
		String typeprint = null;
		if(type == ReturnType.STR_TYPE)
			typeprint = "String";
		else if(type == ReturnType.BOOLEAN_TYPE)
			typeprint = "Boolean";
		else if(type == ReturnType.SIMPLE_NAME_TYPE)
			typeprint = "simplename";
		else if(type == ReturnType.NUM_TYPE)
			typeprint = "number";
		else if(type == ReturnType.METHOD_TYPE)
			typeprint = "method";
		else if(type == ReturnType.QUALIFIED_NAME_TYPE)
			typeprint = "qualified name";
		else if(type == ReturnType.ARRAY_CREATION_TYPE)
			typeprint = "array creation";
		else if(type == ReturnType.CAST_EXPRESSION_TYPE)
			typeprint = "cast expression";
		else if(type == ReturnType.NULL_TYPE)
			typeprint = "null";
		else if(type == ReturnType.CLASS_INSTANCE_CREATION_TYPE)
			typeprint = "class instance creation";
		else if(type == ReturnType.TYPE_LITERAL_TYPE)
			typeprint = "type literal";
		return typeprint;
	}
	
	private String MockInfoPrint(MockInfo mockinfo) {
		String field = mockinfo.getField();
		String invokedobject = mockinfo.getObject();
		String invokedmethod = mockinfo.getMethod();
		String returntype = ReturnTypePrint(mockinfo.getType());
		String returncontent = mockinfo.getContent();
		
		String printstr = "Location: " + field + '\n' +
				"Invoked object: " + invokedobject + '\n' +
				"Invoked method: " + invokedmethod + '\n' +
				"Return value type: " + returntype + '\n' +
				"Return content: " + returncontent;
		return printstr;
	}
	
	public void WriteSimpleFile(List<LinkedList> simplevaluepattern, String filename) throws Exception {
		if(simplevaluepattern.size() == 0)
			return;
		
		File simplecsv = new File("simple.csv");
		try {
			FileWriter output = new FileWriter(simplecsv, true);
			com.opencsv.CSVWriter writer = new com.opencsv.CSVWriter(output);
		
			String[] header = {"Index", "Statements", "Field", "AST Location", filename};
			writer.writeNext(header);
		
			for(int index = 0;index < simplevaluepattern.size();index++) {			
				String stmtlist = "";
				String fieldlist = "";
				String positionlist = "";
				
				LinkedList<Info> pattern_list = simplevaluepattern.get(index);
				for(int i = 0;i < pattern_list.size();i++) {
					if(pattern_list.get(i) instanceof MockInitInfo) {
						MockInitInfo mockinitinfo = (MockInitInfo) pattern_list.get(i);
						int ASTPosition = mockinitinfo.getPosition();
						String stmt = mockinitinfo.getStmt();
						String field = mockinitinfo.getField();
						
						stmtlist = stmtlist + stmt + "\r";
						fieldlist = fieldlist + field + "\r";
						positionlist = positionlist + Integer.toString(ASTPosition) + "\r";
					}
					if(pattern_list.get(i) instanceof MockInfo) {
						MockInfo mockinfo = (MockInfo) pattern_list.get(i);
						int ASTPosition = mockinfo.getPosition();
						String stmt = mockinfo.getStmt();
						String field = mockinfo.getField();
						
						stmtlist = stmtlist + stmt + "\r";
						fieldlist = fieldlist + field + "\r";
						positionlist = positionlist + Integer.toString(ASTPosition) + "\r";
					}	
				}			
				String[] data = {Integer.toString(index), 
						stmtlist, fieldlist, positionlist};
				
				writer.writeNext(data);
			}
			writer.close();
		}
		catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	}
	
	private boolean HasSimple(LinkedList<Info> pattern_list) {
		boolean flag = false;		
		int tail = pattern_list.size()-1;
		if(pattern_list.get(tail) instanceof ObjectInfo) {
			ObjectInfo objectinfo = (ObjectInfo) pattern_list.get(tail);
			if(objectinfo.getInfoType() == ObjectInitType.STR_TYPE)
				flag = true;
			else {
				if(objectinfo.ArgsHasSimple())
					flag = true;
			}
		}		
		return flag;
	}
	
	public void WriteObjectFile(List<LinkedList> objectvaluepattern, String filename) throws Exception {
		if(objectvaluepattern.size() == 0)
			return;
		
		File objectcsv = new File("object.csv");
		try {
			FileWriter output = new FileWriter(objectcsv, true);
			com.opencsv.CSVWriter writer = new com.opencsv.CSVWriter(output);
			
			String[] header = {"Index", "Statements", "Field", "AST Location", filename};
			writer.writeNext(header);
			
			for(int index = 0;index < objectvaluepattern.size();index++) {				
				String stmtlist = "";
				String fieldlist = "";
				String positionlist = "";
				
				LinkedList<Info> pattern_list = objectvaluepattern.get(index);
				
				if(HasSimple(pattern_list) == false)
					continue;
				
				for(int i = 0;i < pattern_list.size();i++) {
					if(pattern_list.get(i) instanceof MockInitInfo) {	
						MockInitInfo mockinitinfo = (MockInitInfo) pattern_list.get(i);
						int ASTPosition = mockinitinfo.getPosition();
						String stmt = mockinitinfo.getStmt();
						String field = mockinitinfo.getField();
						
						stmtlist = stmtlist + stmt + "\r";
						fieldlist = fieldlist + field + "\r";
						positionlist = positionlist + Integer.toString(ASTPosition) + "\r";
					}
					else if(pattern_list.get(i) instanceof MockInfo) {	
						MockInfo mockinfo = (MockInfo) pattern_list.get(i);
						int ASTPosition = mockinfo.getPosition();
						String stmt = mockinfo.getStmt();
						String field = mockinfo.getField();
						
						stmtlist = stmtlist + stmt + "\r";
						fieldlist = fieldlist + field + "\r";
						positionlist = positionlist + Integer.toString(ASTPosition) + "\r";
					}
					else if(pattern_list.get(i) instanceof ObjectInfo) {
						ObjectInfo objectinfo = (ObjectInfo) pattern_list.get(i);
						int ASTPosition = objectinfo.getPosition();
						String stmt = objectinfo.getStmt();
						String field = objectinfo.getField();
						
						stmtlist = stmtlist + stmt + "\r";
						fieldlist = fieldlist + field + "\r";
						positionlist = positionlist + Integer.toString(ASTPosition) + "\r";
					}
					else if(pattern_list.get(i) instanceof ParaInfo) {
						ParaInfo parainfo = (ParaInfo) pattern_list.get(i);
						int ASTPosition = parainfo.getPosition();
						String stmt = parainfo.getStmt();
						String field = "parameter of method";
						
						stmtlist = stmtlist + stmt + "\r";
						fieldlist = fieldlist + field + "\r";
						positionlist = positionlist + Integer.toString(ASTPosition) + "\r";
					}
					else if(pattern_list.get(i) instanceof FieldObjectInfo) {
						FieldObjectInfo fieldobjectinfo = (FieldObjectInfo) pattern_list.get(i);
						int ASTPosition = fieldobjectinfo.getPosition();
						String stmt = fieldobjectinfo.getStmt();
						String field = "global";
						
						stmtlist = stmtlist + stmt + "\r";
						fieldlist = fieldlist + field + "\r";
						positionlist = positionlist + Integer.toString(ASTPosition) + "\r";
					}
				}
				String[] data = {Integer.toString(index), 
						stmtlist, fieldlist, positionlist};
				
				writer.writeNext(data);
			}
			writer.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean RelatedToSimple(LinkedList<Info> pattern_list, List<LinkedList> mocklinkset) {
		boolean flag = false;
		
		int tail = pattern_list.size()-1;
		if(pattern_list.get(tail) instanceof ListIndexInfo) {
			ListIndexInfo indexinfo = (ListIndexInfo) pattern_list.get(tail);
			List<Integer> indexList = indexinfo.getIndexList();
			for(int i = 0;i < indexList.size();i++) {
				LinkedList<Info> mocklink = mocklinkset.get(indexList.get(i));
				int linktail = mocklink.size()-1;
				if(mocklink.get(linktail) instanceof MockInfo) {
					MockInfo wheninfo = (MockInfo) mocklink.get(linktail);
					if(wheninfo.getType() == ReturnType.STR_TYPE ||
							wheninfo.getType() == ReturnType.BOOLEAN_TYPE ||
							wheninfo.getType() == ReturnType.NUM_TYPE ||
							wheninfo.getType() == ReturnType.QUALIFIED_NAME_TYPE ||
							wheninfo.getType() == ReturnType.NULL_TYPE ||
							wheninfo.getType() == ReturnType.CLASS_INSTANCE_CREATION_TYPE ||
							wheninfo.getType() == ReturnType.TYPE_LITERAL_TYPE ||
							wheninfo.getType() == ReturnType.ARRAY_CREATION_TYPE ||
							wheninfo.getType() == ReturnType.CAST_EXPRESSION_TYPE) {
						flag = true;
						break;
					}
				}
			}
		}
		
		return flag;
	}
	
	public void WriteMockFile(List<LinkedList> mockvaluepattern, 
			List<LinkedList> mocklinkset, String filename) {
		if(mockvaluepattern.size() == 0)
			return;
		
		File mockcsv = new File("mock.csv");
		try {
			FileWriter output = new FileWriter(mockcsv, true);
			com.opencsv.CSVWriter writer = new com.opencsv.CSVWriter(output);
			
			String[] header = {"Index", "Statements", "Field", "AST Location", filename};
			writer.writeNext(header);
			
			for(int index = 0;index < mockvaluepattern.size();index++) {
				String stmtlist = "";
				String fieldlist = "";
				String positionlist = "";
				
				LinkedList<Info> pattern_list = mockvaluepattern.get(index);
				if(RelatedToSimple(pattern_list, mocklinkset) == false)
					continue;
				
				for(int i = 0;i < pattern_list.size();i++) {
					if(pattern_list.get(i) instanceof MockInitInfo) {	
						MockInitInfo mockinitinfo = (MockInitInfo) pattern_list.get(i);
						int ASTPosition = mockinitinfo.getPosition();
						String stmt = mockinitinfo.getStmt();
						String field = mockinitinfo.getField();
						
						stmtlist = stmtlist + stmt + "\r";
						fieldlist = fieldlist + field + "\r";
						positionlist = positionlist + Integer.toString(ASTPosition) + "\r";
					}
					else if(pattern_list.get(i) instanceof MockInfo) {	
						MockInfo mockinfo = (MockInfo) pattern_list.get(i);
						int ASTPosition = mockinfo.getPosition();
						String stmt = mockinfo.getStmt();
						String field = mockinfo.getField();
						
						stmtlist = stmtlist + stmt + "\r";
						fieldlist = fieldlist + field + "\r";
						positionlist = positionlist + Integer.toString(ASTPosition) + "\r";
					}
					else if(pattern_list.get(i) instanceof ListIndexInfo) {
						ListIndexInfo indexinfo = (ListIndexInfo) pattern_list.get(i);
						
						for(Integer linkindex:indexinfo.getIndexList()) {
							LinkedList<Info> mocklink = mocklinkset.get(linkindex);
							int linktail = mocklink.size()-1;
							if(mocklink.get(linktail) instanceof MockInfo) {
								MockInfo wheninfo = (MockInfo) mocklink.get(linktail);
								if(wheninfo.getType() == ReturnType.STR_TYPE ||
										wheninfo.getType() == ReturnType.BOOLEAN_TYPE ||
										wheninfo.getType() == ReturnType.NUM_TYPE ||
										wheninfo.getType() == ReturnType.QUALIFIED_NAME_TYPE ||
										wheninfo.getType() == ReturnType.NULL_TYPE ||
										wheninfo.getType() == ReturnType.CLASS_INSTANCE_CREATION_TYPE ||
										wheninfo.getType() == ReturnType.TYPE_LITERAL_TYPE ||
										wheninfo.getType() == ReturnType.ARRAY_CREATION_TYPE ||
										wheninfo.getType() == ReturnType.CAST_EXPRESSION_TYPE) {
									int ASTPosition = wheninfo.getPosition();
									String stmt = wheninfo.getStmt();
									String field = wheninfo.getField();
									
									stmtlist = stmtlist + stmt + "\r";
									fieldlist = fieldlist + field + "\r";
									positionlist = positionlist + Integer.toString(ASTPosition) + "\r";
								}
							}
							else if(mocklink.get(linktail) instanceof MockInitInfo) {
								MockInitInfo mockinitinfo = (MockInitInfo) pattern_list.get(i);
								int ASTPosition = mockinitinfo.getPosition();
								String stmt = mockinitinfo.getStmt();
								String field = mockinitinfo.getField();
								
								stmtlist = stmtlist + stmt + "\r";
								fieldlist = fieldlist + field + "\r";
								positionlist = positionlist + Integer.toString(ASTPosition) + "\r";
							}
						}
					}
				}
				String[] data = {Integer.toString(index), 
						stmtlist, fieldlist, positionlist};
				
				writer.writeNext(data);
			}
			writer.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
