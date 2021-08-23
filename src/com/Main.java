package com;

import java.io.File;
import java.util.List;

import org.eclipse.core.runtime.CoreException;

public class Main {		
	public static void main(String[] args) throws CoreException, Exception{
		System.out.println("Detection Begin");
		
		FilterFile walkdir = new FilterFile();
		List<File> files = walkdir.walk(Constant.TESTPROJECT);
		for(File file: files) {
			JavaToAST jdt = new JavaToAST();
			jdt.getCompilationUnit(file);
		}
	}
}
