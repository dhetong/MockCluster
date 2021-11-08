package implementationfinder;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import output.ObjectOutPutInfo;

public class ObjectScanner extends ASTVisitor {
	private ImplementationFinder finder;
	private List<ObjectOutPutInfo> actualoutput = new ArrayList<>();
	
	public ObjectScanner(ImplementationFinder f) {
		finder = f;
	}
	
	private boolean StmtChecker(Statement stmt, String fname) {
		if(stmt instanceof VariableDeclarationStatement) {
			VariableDeclarationFragment frag = (VariableDeclarationFragment) 
					((VariableDeclarationStatement) stmt).fragments().get(0);
			String cname = ((VariableDeclarationStatement) stmt).getType().toString();
			
			List<SearchKeyObject> objectlist = finder.getObjectKeyList();
			
			if(frag.getInitializer() instanceof MethodInvocation) {
				String mname =((MethodInvocation) frag.getInitializer()).getName().toString();
				int anum = ((MethodInvocation) frag.getInitializer()).arguments().size();
				for(int i = 0;i < objectlist.size();i++) {
					SearchKeyObject objectinfo = objectlist.get(i);
					
					if(objectinfo.getFieldName().equals(fname))
						continue;
					
					String classname = objectinfo.getClassName();
					String methodname = objectinfo.getMethodName();
					int argsnum = objectinfo.getArgsNum();
					int inittype = objectinfo.getInitType();
					
					if((inittype == InitializerType.METHOD_INVOCATION) && classname.equals(cname) && 
							methodname.equals(mname) && (argsnum == anum))
						return true;
				}
			}
			else if(frag.getInitializer() instanceof ClassInstanceCreation) {
				String mname = cname;
				int anum = ((ClassInstanceCreation) frag.getInitializer()).arguments().size();
				for(int i = 0;i < objectlist.size();i++) {
					SearchKeyObject objectinfo = objectlist.get(i);
					
					if(objectinfo.getFieldName().equals(fname))
						continue;
					
					String classname = objectinfo.getClassName();
					int argsnum = objectinfo.getArgsNum();
					int inittype = objectinfo.getInitType();
					
					if((inittype == InitializerType.INSTANCE_CREATION) && classname.equals(cname) && 
							(argsnum == anum))
						return true;
				}
			}
		}
		return false;
	}
	
	private List getSimpleIndex(List args) {
		List<Integer> index_list = new ArrayList<>();
		
		for(int index = 0;index < args.size();index++) {
			if(args.get(index) instanceof StringLiteral) {
				index_list.add(index);
			}
			else if(args.get(index) instanceof NumberLiteral) {
				index_list.add(index);
			}
			else if(args.get(index) instanceof QualifiedName) {
				index_list.add(index);
			}
		}
		
		return index_list;
	}
	
	private String getArgType(List args, int index) {
		if(args.get(index) instanceof StringLiteral)
			return "String";
		else if(args.get(index) instanceof NumberLiteral)
			return "Number";
		else if(args.get(index) instanceof QualifiedName)
			return "QualifiedName";
		else
			return "ERROR";
	}
	
	private String getTypeArgs(List argsindex, List args) {
		String output = "";
		
		for(int i = 0;i < argsindex.size();i++) {
			int index = (int) argsindex.get(i);
			String tmp = getArgType(args, index);
			output = output + tmp + "||";
		}
		
		return output;
	}
	
	private String getContentArgs(List argsindex, List args) {
		String output = "";
		
		for(int i = 0;i < argsindex.size();i++) {
			int index = (int) argsindex.get(i);
			String tmp = args.get(index).toString();
			output = output + tmp + "||";
		}
		
		return output;
	}
	
	private boolean InOutputList(ObjectOutPutInfo outputinfo) {
		boolean flag = false;
		
		for(int i = 0;i < actualoutput.size();i++) {
			ObjectOutPutInfo tmp = actualoutput.get(i);
			if(!tmp.getFieldName().equals(outputinfo.getFieldName()))
				continue;
			if(!tmp.getClassName().equals(outputinfo.getClassName()))
				continue;
			if(!tmp.getInitType().equals(outputinfo.getInitType()))
				continue;
			if(!tmp.getMethodName().equals(outputinfo.getMethodName()))
				continue;
			if(!tmp.getArgsType().equals(outputinfo.getArgsType()))
				continue;
			if(!tmp.getArgsContent().equals(outputinfo.getArgsContent()))
				continue;
			flag = true;
			break;
		}
		
		return flag;
	}
	
	private void ObjectFinder(MethodDeclaration node){
		Block body = node.getBody();
		if(body == null)
			return;
		
		String fieldname = node.getName().toString();
		List<Statement> stmtlist = node.getBody().statements();
		for(Statement stmt:stmtlist) {
			if(StmtChecker(stmt, fieldname) == true) {
				VariableDeclarationFragment frag = (VariableDeclarationFragment) 
						((VariableDeclarationStatement) stmt).fragments().get(0);
				if(frag.getInitializer() instanceof MethodInvocation) {
					MethodInvocation methodinvocation = (MethodInvocation) frag.getInitializer();
					List args = methodinvocation.arguments();
					List argsindex = getSimpleIndex(args);
					
					String cname = ((VariableDeclarationStatement) stmt).getType().toString();
					String mname = methodinvocation.getName().toString();
					String inittype = "MethodInvocation";
					String argstype = getTypeArgs(argsindex, args);
					String argscontent = getContentArgs(argsindex, args);
					String fname = node.getName().toString();
					
					ObjectOutPutInfo outputinfo = new ObjectOutPutInfo(cname, inittype, mname, argstype,
							argscontent, fname);
					if(InOutputList(outputinfo) == false)
						actualoutput.add(outputinfo);
				}
				else if(frag.getInitializer() instanceof ClassInstanceCreation) {
					ClassInstanceCreation instancecreation = (ClassInstanceCreation) frag.getInitializer();
					List args = instancecreation.arguments();
					List argsindex = getSimpleIndex(args);
					
					String cname = ((VariableDeclarationStatement) stmt).getType().toString();
					String mname = cname;
					String inittype = "InstanceCreation";
					String argstype = getTypeArgs(argsindex, args);
					String argscontent = getContentArgs(argsindex, args);
					String fname = node.getName().toString();
					
					ObjectOutPutInfo outputinfo = new ObjectOutPutInfo(cname, inittype, mname, argstype,
							argscontent, fname);
					if(InOutputList(outputinfo) == false)
						actualoutput.add(outputinfo);
				}
			}
		}
	}
	
	public boolean visit(MethodDeclaration node) {
		ObjectFinder(node);
		return super.visit(node);
	}
}
