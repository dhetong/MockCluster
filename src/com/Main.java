package com;

import java.io.File;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.CompilationUnit;

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
			
			LinkObject linker = new LinkObject();
			linker.InitToWhen(mockinfolist, mockinitinfolist);

			//find no mock and when statement in field(Cayenne) 
//			FieldVisitor fvisitor = new FieldVisitor();
//			cunit.accept(fvisitor);
		}
	}
}
