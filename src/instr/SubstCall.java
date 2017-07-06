package instr;

/**
 *
 * @author Leonardo
 */
public class SubstCall {
    String className;
    String name;
    String source;
    
    public SubstCall(String className, String name, String source){
        this.className=className;
        this.source=source;
        this.name=name;
    };

    public String getClassName() {
        return className;
    }

    public String getName() {
        return name;
    }

    public String getSource() {
        return source;
    }

    @Override
    public String toString() {
        return "SubstCall{" + "className=" + className + ", name=" + name + ", source=" + source + '}';
    }
    
}
