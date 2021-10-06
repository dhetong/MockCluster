package common;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jface.text.Document;

import handler.FieldVisitor;
import handler.MethodVisitor;
import implementationfinder.ImplementationFinder;
import implementationfinder.InsertPosInfo;
import implementationfinder.MethodScanner;
import output.CSVWriter;
import patternnodeinfo.MockInfo;
import patternnodeinfo.MockInitInfo;

public class Main {		
	public static void main(String[] args) throws CoreException, Exception{
		System.out.println("Detection Begin");
		
		FilterFile walkdir = new FilterFile();
		List<File> files = walkdir.walk(Constant.TESTPROJECT);
		
		ImplementationFinder finder = new ImplementationFinder();
		
		for(File file: files) {
			JavaToAST jdt = new JavaToAST();
			CompilationUnit cunit = jdt.getCompilationUnit(file);
			
			MethodVisitor mvisitor = new MethodVisitor();
			cunit.accept(mvisitor);
			
			//find no mock and when statement in field(Cayenne)
			FieldVisitor fvisitor = new FieldVisitor();
			cunit.accept(fvisitor);
			
			List<MockInfo> mockinfolist = mvisitor.getMockInfoList();
			List<MockInitInfo> mockinitinfolist = mvisitor.getMockInitInfoList();
			HashMap<String,List<Statement>> stmtdict = mvisitor.getStmtDict();
			HashMap<String,List> paradict = mvisitor.getParaDict();
			
			List<FieldDeclaration> fieldstmtlist = fvisitor.getFieldStmt();
			
			LinkObject linker = new LinkObject();
			
			linker.InitializeLinker(mockinfolist, mockinitinfolist);
			linker.InitToWhen();
			linker.ReturnValueFilter();
			linker.MockValueMather();
			linker.ObjectValueMatcher(stmtdict, paradict, fieldstmtlist);
			
			finder.UpdateKeyList(linker.getSimplePattern());
//			finder.UpdateKeyList(linker.getObjectPattern());
//			finder.UpdateKeyList(linker.getMockPattern());
			
//			CSVWriter writer = new CSVWriter();
//			writer.WriteSimpleFile(linker.getSimplePattern(), file.toString());
//			writer.WriteObjectFile(linker.getObjectPattern(), file.toString());
//			writer.WriteMockFile(linker.getMockPattern(), linker.getLinkSet(), file.toString());
		}
		
		for(File file: files) {
			JavaToAST jdt = new JavaToAST();
			CompilationUnit cunit = jdt.getCompilationUnit(file);
			String source = FileUtils.readFileToString(file);
			Document document = new Document(source);
			
			MethodScanner scanner = new MethodScanner(finder);
			cunit.accept(scanner);
			List<InsertPosInfo> insertposlist = scanner.getInsertInfo();
		}
	}
}
