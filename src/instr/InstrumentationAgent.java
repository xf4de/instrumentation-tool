package instr;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.List;

/**
 *
 * @author Leonardo
 */
public class InstrumentationAgent {
    
    public static List<Rule> rules; // ArrayList containing replacement rules
    
    // premain method (main of the agent) that will be executed before the main 
    public static void premain(String agentArgs, Instrumentation inst) throws IOException {
        
        // create parser
        JsonParser parser = new JsonParser(agentArgs);
        //parse rules
        rules=parser.parseRules();
        System.out.println("[DEBUG] Parsed rules: ");
        System.out.println(rules);
        // set security manager with parsed security settings
        SecurityInitializer init = new SecurityInitializer(parser.parseSecurity());
        
        // create the deserialize manager
        DeserializeManager dm = new DeserializeManager(rules, parser.parseDeserialize());
        if (dm.getStatus()) {
            System.out.println("[DEBUG] Applied rules: ");
            System.out.println(rules);
        }
        // create bytecode transformer with the parsed transformation rules
        inst.addTransformer(new Transformer(rules));
        System.out.println("--Agent started--");
    }
}
