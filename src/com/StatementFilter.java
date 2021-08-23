package com;

import org.eclipse.jdt.core.dom.Statement;

public class StatementFilter {
	public void stmFilter(Statement s) {
		if(s.getNodeType() == Statement.EXPRESSION_STATEMENT) {
		}
	}
}
