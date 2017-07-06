package instr;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.List;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CannotCompileException;
import javassist.CtField;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import javassist.expr.NewExpr;
/**
 *
 * @author Leonardo
 */
public class Transformer implements ClassFileTransformer {
    
    List<Rule> rules;
    
    public Transformer(List<Rule> rules){
        this.rules=rules;
    }
    
    @Override
    public byte[] transform(ClassLoader loader, String className, 
            Class<?> classBeingRedefined, ProtectionDomain protectionDomain, 
            byte[] classfileBuffer) throws IllegalClassFormatException {
        
        byte[] modifiedClassByteCode = classfileBuffer;
        
        for (Rule rule : rules) {
            if (className.endsWith(rule.getClassname())) {
                try {
                    ClassPool classPool = ClassPool.getDefault();
                    CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(
                            classfileBuffer));
                    
                    //Methods
                    if (rule.getMethod_inside_call() != null 
                            || rule.getMethod_inside_new() != null
                            || rule.getMethods_insertAfter() != null
                            || rule.getMethods_insertBefore() != null
                            || rule.getMethods_repl()!= null ){

                        CtMethod[] methods = ctClass.getDeclaredMethods();
                        for (CtMethod method : methods) {
                            //Replace method body
                            if(rule.getMethods_repl()!=null){
                                for (Pair<String, String> met : rule.getMethods_repl()) {
                                    if (method.getName().endsWith(met.getKey())){
                                        method.setBody(met.getValue());
                                    }
                                }
                            }
                            //insert before
                            if (rule.getMethods_insertBefore() != null) {
                                for (Pair<String, String> met : rule.getMethods_insertBefore()) {
                                    if (method.getName().endsWith(met.getKey())){
                                        method.insertBefore(met.getValue());
                                    }
                                }
                            }
                            //insert after
                            if (rule.getMethods_insertAfter() != null) {
                                for (Pair<String, String> met : rule.getMethods_insertAfter()) {
                                    if (method.getName().endsWith(met.getKey())){
                                        method.insertAfter(met.getValue());
                                    }
                                }
                            }
                            
                            //method inside call
                            if(rule.getMethod_inside_call()!=null){
                                for (Pair<String,SubstCall> met:rule.getMethod_inside_call()) {
                                    if (method.getName().endsWith(met.getKey())){
                                        method.instrument(new ExprEditor() {
                                            @Override
                                            public void edit(MethodCall m) throws CannotCompileException {
                                                if (m.getClassName().endsWith(met.getValue().getClassName())
                                                        && m.getMethodName().equals(met.getValue().getName())) {
                                                    m.replace(met.getValue().getSource());
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                            //method inside new
                            if(rule.getMethod_inside_new()!=null){
                                for (Pair<String,SubstNew> met:rule.getMethod_inside_new()) {
                                    if (method.getName().endsWith(met.getKey())){
                                        method.instrument(new ExprEditor() {
                                            @Override
                                            public void edit(NewExpr m) throws CannotCompileException {
                                                if (m.getClassName().endsWith(met.getValue().getClassName())) {
                                                    m.replace(met.getValue().getSource());
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                            
                        }
                    }
                    //fields
                    if (rule.getFields_repl() != null) {
                        CtField[] fields = ctClass.getDeclaredFields();
                        for (Pair<String, String> fie : rule.getFields_repl()) {
                            for (CtField field : fields) {
                                if (field.getName().equals(fie.getKey())) {
                                    ctClass.removeField(field);
                                    CtField f = CtField.make(fie.getValue(), ctClass);
                                    ctClass.addField(f);
                                }
                            }
                        }
                    }
                    
                    modifiedClassByteCode = ctClass.toBytecode();
                    ctClass.detach();
                } catch (Throwable ex) {
                    System.out.println("Exception: " + ex);
                }
            }
        }
        return modifiedClassByteCode;
    }
    
}
