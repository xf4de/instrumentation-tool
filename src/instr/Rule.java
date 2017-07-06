package instr;

import java.util.List;


/**
 *
 * @author Leonardo
 */
public class Rule {
    String classname;
    List<Pair<String,String>> methods_repl;
    List<Pair<String,String>> methods_insertBefore;
    List<Pair<String,String>> methods_insertAfter;
    List<Pair<String,SubstCall>> method_inside_call;
    List<Pair<String,SubstNew>> method_inside_new;
    List<Pair<String,String>> fields_repl;
    
    public Rule(String classname,List<Pair<String,String>> methods_repl, 
            List<Pair<String,String>> methods_insertBefore, 
            List<Pair<String,String>> methods_insertAfter, 
            List<Pair<String,SubstCall>> method_inside_call, 
            List<Pair<String,SubstNew>> method_inside_new, 
            List<Pair<String,String>> fields_repl){
        this.classname=classname;
        this.methods_repl = methods_repl;
        this.methods_insertBefore = methods_insertBefore;
        this.methods_insertAfter = methods_insertAfter;
        this.method_inside_call = method_inside_call;
        this.method_inside_new = method_inside_new;
        this.fields_repl = fields_repl;
    }
    public Rule() {
        this.classname=null;
        this.methods_repl = null;
        this.methods_insertBefore = null;
        this.methods_insertAfter = null;
        this.method_inside_call = null;
        this.method_inside_new = null;
        this.fields_repl = null;
    }

    public String getClassname() {
        return classname;
    }

    public List<Pair<String, String>> getMethods_repl() {
        return methods_repl;
    }

    public List<Pair<String, String>> getMethods_insertBefore() {
        return methods_insertBefore;
    }

    public List<Pair<String, String>> getMethods_insertAfter() {
        return methods_insertAfter;
    }

    public List<Pair<String, SubstCall>> getMethod_inside_call() {
        return method_inside_call;
    }

    public List<Pair<String, SubstNew>> getMethod_inside_new() {
        return method_inside_new;
    }

    public List<Pair<String, String>> getFields_repl() {
        return fields_repl;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public void setMethods_repl(List<Pair<String, String>> methods_repl) {
        this.methods_repl = methods_repl;
    }

    public void setMethods_insertBefore(List<Pair<String, String>> methods_insertBefore) {
        this.methods_insertBefore = methods_insertBefore;
    }

    public void setMethods_insertAfter(List<Pair<String, String>> methods_insertAfter) {
        this.methods_insertAfter = methods_insertAfter;
    }

    public void setMethod_inside_call(List<Pair<String, SubstCall>> method_inside_call) {
        this.method_inside_call = method_inside_call;
    }

    public void setMethod_inside_new(List<Pair<String, SubstNew>> method_inside_new) {
        this.method_inside_new = method_inside_new;
    }

    public void setFields_repl(List<Pair<String, String>> fields_repl) {
        this.fields_repl = fields_repl;
    }
    
    @Override
    public String toString() {
        String result=new String("{ ");
        result=result.concat("classname "+classname+", ");
        if (methods_repl!=null){result=result.concat("method_repl "+methods_repl.toString()+", ");}
        if (methods_insertBefore!=null){result=result.concat("method_insertbefore "+methods_insertBefore.toString()+", ");}
        if (methods_insertAfter!=null){result=result.concat("method_insertAfter "+methods_insertAfter.toString()+", ");}
        if (method_inside_call!=null){result=result.concat("method_inside_Call "+method_inside_call.toString()+", ");}
        if (method_inside_new!=null){result=result.concat("method_inside_New "+method_inside_new.toString()+", ");}
        if (fields_repl!=null){result=result.concat("field_repl "+fields_repl.toString()+", ");}
        result=result.concat("}");
        return result;
    }
    
}
