package com;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Statement;

import handler.FieldVisitor;
import handler.MethodVisitor;

public class Main {		
	public static void main(String[] args) throws CoreException, Exception{
		System.out.println("Detection Begin");
		
		FilterFile walkdir = new FilterFile();
		List<File> files = walkdir.walk(Constant.TESTPROJECT);
		for(File file: files) {
			JavaToAST jdt = new JavaToAST();
			CompilationUnit cunit = jdt.getCompilationUnit(file);
			
			MethodVisitor mvisitor = new MethodVisitor();
			cunit.accept(mvisitor);
			
			List<MockInfo> mockinfolist = mvisitor.getMockInfoList();
			List<MockInitInfo> mockinitinfolist = mvisitor.getMockInitInfoList();
			HashMap<String,List<Statement>> stmtdict = mvisitor.getStmtDict();
			
			LinkObject linker = new LinkObject();
			
			linker.InitializeLinker(mockinfolist, mockinitinfolist);
			linker.InitToWhen();
			linker.ReturnValueFilter();
			linker.MockValueMather();
			linker.ObjectValueMatcher(stmtdict);

			//find no mock and when statement in field(Cayenne) 
//			FieldVisitor fvisitor = new FieldVisitor();
//			cunit.accept(fvisitor);
		}
	}
}
