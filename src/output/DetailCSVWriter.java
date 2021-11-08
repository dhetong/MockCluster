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
	
	public void WriteObjectFile(List<ObjectOutPutInfo> outputlist) throws Exception {
		if(outputlist.size() == 0)
			return;
		
		File objectcsv = new File("object.csv");
		try {
			FileWriter output = new FileWriter(objectcsv, true);
			com.opencsv.CSVWriter writer = new com.opencsv.CSVWriter(output);
			
			String[] header = {"FieldName", "ClassName", "InitializerType", "MethodName",
					"ArgsType", "Arguments"};
			writer.writeNext(header);
			
			for(int i = 0;i < outputlist.size();i++) {
				ObjectOutPutInfo tmp = outputlist.get(i);
				String fieldname = tmp.getFieldName();
				String classname = tmp.getClassName();
				String inittype = tmp.getInitType();
				String methodname = tmp.getMethodName();
				String argstype = tmp.getArgsType();
				String argscontent = tmp.getArgsContent();
				String[] row = {fieldname, classname, inittype, methodname, argstype, argscontent};
				writer.writeNext(row);
			}
			
			writer.close();
		}
		catch (IOException e) {
		}
	}
}
