package instr;

/**
 *
 * @author Leonardo
 */
public class SubstNew {
    String className;
    String source;
    
    public SubstNew(String className, String source){
        this.className=className;
        this.source=source;
    }

    public String getClassName() {
        return className;
    }

    public String getSource() {
        return source;
    }

    @Override
    public String toString() {
        return "SubstNew{" + "className=" + className + ", source=" + source + '}';
    }
}
