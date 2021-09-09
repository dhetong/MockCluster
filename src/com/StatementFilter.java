package com;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.Statement;

import handler.ThenPatternHandler;

public class StatementFilter {
	public MockInfo stmFilter(Statement s) {
		if(s.getNodeType() == Statement.EXPRESSION_STATEMENT) {
			Pattern PatternWhen = Pattern.compile("when\\(([^}]*)\\)\\.");
			Matcher matcher = PatternWhen.matcher(s.toString());
			if(matcher.find()) {
				if(s.toString().contains("doReturn")) {
				}
				else if(s.toString().contains("thenReturn")) {
					ThenPatternHandler thenhandler = new ThenPatternHandler();
					s.accept(thenhandler);
					
					return thenhandler.getMockInfo();
				}
			}
		}
		return null;
	}
}
