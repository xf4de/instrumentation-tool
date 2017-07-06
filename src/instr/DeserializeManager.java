package instr;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Leonardo
 */
public class DeserializeManager {
    boolean status;
    
    public DeserializeManager(List<Rule> rules,Pair <String,String> pair) {
        if (pair != null) status=true;
        if (!status) {
            System.out.println("[INFO] SerialKiller disabled");
        }else{
            Rule rule = new Rule();
            rule.setClassname(pair.getKey());
            
            if (rule.getMethod_inside_new() == null) {
                ArrayList<Pair<String, SubstNew>> a = new ArrayList<Pair<String, SubstNew>>();
                rule.setMethod_inside_new(a);
            }
            Pair<String, SubstNew> insidenew = new Pair<String, SubstNew>(pair.getValue(), new SubstNew("ObjectInputStream", "{$_= new org.nibblesec.tools.SerialKiller($1, \"agentassets/serialkiller.conf\"); System.out.println(\"[PATCH] SerialKiller Substitution applied\");}"));
            rule.getMethod_inside_new().add(insidenew);
            
            //adding rule
            rules.add(rule);
            
            System.out.println("[INFO] SerialKiller enabled");
        }
    }
    public boolean getStatus(){return this.status;}
    
}
