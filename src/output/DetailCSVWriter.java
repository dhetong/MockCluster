package output;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import implementationfinder.InitializerType;
import implementationfinder.SearchKeyObject;
import patternnodeinfo.Info;
import patternnodeinfo.MockInfo;
import patternnodeinfo.MockInitInfo;
import patternnodeinfo.ReturnType;

public class DetailCSVWriter {
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
	
	private String[] MockInfoRowGenerator(MockInfo info, String classname) {
		String vname = info.getObject();
		String method = info.getMethod();
		String returntype = getReturnType(info);
		String returnvalue = info.getContent();
		String[] row = {classname, vname, method, returntype, returnvalue};
		return row;
	}
	
	public void WriteSimpleFile(List<LinkedList> simplevaluepattern, String filename, boolean isWrite) throws Exception {
		if(simplevaluepattern.size() == 0)
			return;
		
		File simplecsv = new File("simple.csv");
		try {
			FileWriter output = new FileWriter(simplecsv, true);
			com.opencsv.CSVWriter writer = new com.opencsv.CSVWriter(output);
			
			String[] header = {"ClassName", "VariableName", "MethodName", "ReturnType", "ReturnValue"};
			if(isWrite == false)
				writer.writeNext(header);
			
			for(int index = 0;index < simplevaluepattern.size();index++) {
				LinkedList<Info> pattern_list = simplevaluepattern.get(index);
				int infoindex = pattern_list.size()-1;
				
				MockInitInfo mockinitinfo = (MockInitInfo) pattern_list.get(0);
				String classname = mockinitinfo.getClassName();
				
				MockInfo mockinfo = (MockInfo) pattern_list.get(infoindex);
				String[] row = MockInfoRowGenerator(mockinfo, classname);
				
				writer.writeNext(row);
			}
			writer.close();
		}
		catch (IOException e) {
		}
	}
	
	private String getInitType(int type) {
		if(type == InitializerType.INSTANCE_CREATION)
			return "InstanceCreation";
		else if(type == InitializerType.METHOD_INVOCATION)
			return "MethodInvocation";
		else
			return "ERORR";
	}
	
	private String getArgType(List args, int index) {
		if(args.get(index) instanceof StringLiteral)
			return "String";
		else if(args.get(index) instanceof NumberLiteral)
			return "Number";
		else
			return "ERROR";
	}
	
	private String getTypeArgs(List argsindex, List args) {
		String output = "";
		
		for(int i = 0;i < argsindex.size();i++) {
			int index = (int) argsindex.get(i);
			String tmp = getArgType(args, index);
			output = output + tmp + "||";
		}
		
		return output;
	}
	
	private String getContentArgs(List argsindex, List args) {
		String output = "";
		
		for(int i = 0;i < argsindex.size();i++) {
			int index = (int) argsindex.get(i);
			String tmp = args.get(index).toString();
			output = output + tmp + "||";
		}
		
		return output;
	}
	
	public void WriteObjectFile(List<SearchKeyObject> objectkeylist) throws Exception {
		if(objectkeylist.size() == 0)
			return;
		
		File objectcsv = new File("object.csv");
		try {
			FileWriter output = new FileWriter(objectcsv, true);
			com.opencsv.CSVWriter writer = new com.opencsv.CSVWriter(output);
			
			String[] header = {"ClassName", "InitializerType", "MethodName", "ArgsType", "Arguments"};
			
			for(int index = 0;index < objectkeylist.size();index++) {
				SearchKeyObject objectkey = objectkeylist.get(index);
				String classname = objectkey.getClassName();
				String inittype = getInitType(objectkey.getInitType());
				String methodname = "";
				
				List argsindex = objectkey.getArgIndex();
				VariableDeclarationStatement stmt = objectkey.getStatement();
				VariableDeclarationFragment frag = (VariableDeclarationFragment) 
						stmt.fragments().get(0);
				String argtypeoutput = "";
				String argcontentoutput = "";
				if(objectkey.getInitType() == InitializerType.INSTANCE_CREATION) {
					ClassInstanceCreation instancecreation = (ClassInstanceCreation) 
							frag.getInitializer();
					List args = instancecreation.arguments();
					argtypeoutput = getTypeArgs(argsindex, args);
					argcontentoutput = getContentArgs(argsindex, args);
					methodname = classname;
				}
				else if(objectkey.getInitType() == InitializerType.METHOD_INVOCATION){
					MethodInvocation methodinvocation = (MethodInvocation) frag.getInitializer();
					List args = methodinvocation.arguments();
					argtypeoutput = getTypeArgs(argsindex, args);
					argcontentoutput = getContentArgs(argsindex, args);
					methodname = objectkey.getMethodName();
				}
				else {
					System.out.println("ERROR");
				}
				String[] row = {classname, inittype, methodname, argtypeoutput, argcontentoutput};
				writer.writeNext(row);
			}
			writer.close();
		}
		catch (IOException e) {
		}
	}
}
