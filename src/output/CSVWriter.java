package output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.List;

import patternnodeinfo.Info;
import patternnodeinfo.MockInfo;
import patternnodeinfo.MockInitInfo;

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
	
	private String MockInfoPrint(MockInfo mockinfo) {
		String printstr = "";
		return printstr;
	}
	
	public void WriteSimpleFile(List<LinkedList> simplevaluepattern) throws Exception {
		File simplecsv = new File("simple.csv");
		BufferedWriter writetext = new BufferedWriter(new FileWriter(simplecsv));
		for(int index = 0;index < simplevaluepattern.size();index++) {
			LinkedList<Info> pattern_list = simplevaluepattern.get(index);
			for(int i = 0;i < pattern_list.size();i++) {
				if(pattern_list.get(i) instanceof MockInitInfo) {
					String mockinitstr = MockInitInfoPrint((MockInitInfo)pattern_list.get(i));
					writetext.write(mockinitstr);
				}
				if(pattern_list.get(i) instanceof MockInfo) {
					
				}
			}
		}
	}
}
