package com;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.Statement;

public class StatementFilter {
	public void stmFilter(Statement s) {
		if(s.getNodeType() == Statement.EXPRESSION_STATEMENT) {
			Pattern PatternWhen = Pattern.compile("when\\(([^}]*)\\)\\.");
			Matcher matcher = PatternWhen.matcher(s.toString());
			if(matcher.find()) {
				if(s.toString().contains("doReturn")) {
				}
				else if(s.toString().contains("thenReturn")) {
				}
			}
		}
	}
}
