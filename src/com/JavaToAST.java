package com;

import org.apache.commons.io.FileUtils;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import org.eclipse.jface.text.Document;

import java.io.*;

public class JavaToAST {
	private ASTParser astParser = ASTParser.newParser(AST.JLS8); 
	
	public CompilationUnit getCompilationUnit(File javaFile) throws IOException {
		final String source = FileUtils.readFileToString(javaFile);
    	Document document = new Document(source);
    	
    	astParser.setResolveBindings(true);
    	astParser.setSource(document.get().toCharArray());
    	astParser.setKind(ASTParser.K_COMPILATION_UNIT);
    	astParser.setBindingsRecovery(true);
    	
    	CompilationUnit cu = (CompilationUnit) astParser.createAST(null);
    	return cu;
	}
}
