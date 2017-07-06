package instr;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Leonardo
 */
public class JsonParser {
    String configfile;
    
    // constructor with path to file to parse 
    JsonParser(String path) throws IOException{
        configfile=new String(Files.readAllBytes(Paths.get(path)));
    }
    
    public ArrayList<Rule> parseRules(){
        // lista is the ArrayList that will contain parsed rules
        ArrayList<Rule> lista =new ArrayList<Rule>();
        JSONObject obj= new JSONObject(configfile);
        
        if (obj.has("rules")) {
            JSONArray rules = obj.getJSONArray("rules");

            for (int i = 0; i < rules.length(); i++) {
                JSONObject current = rules.getJSONObject(i);
                Rule rule = new Rule();
                rule.setClassname(current.getString("classname"));
                JSONArray actions = current.getJSONArray("actions");
                for (int j = 0; j < actions.length(); j++) {
                    JSONObject curAct = actions.getJSONObject(j);
                    switch (curAct.getString("type")) {
                        case ("METHOD-REPLACE"):
                            // create list in case it isn't already present
                            if (rule.getMethods_repl() == null) {
                                ArrayList<Pair<String, String>> a = new ArrayList<Pair<String, String>>();
                                rule.setMethods_repl(a);
                            }
                            Pair<String, String> methodrepl = new Pair<String, String>(curAct.getString("name"), curAct.getString("code"));
                            rule.getMethods_repl().add(methodrepl);
                            break;
                        case ("METHOD-INSERT-BEFORE"):
                            if (rule.getMethods_insertBefore() == null) {
                                ArrayList<Pair<String, String>> a = new ArrayList<Pair<String, String>>();
                                rule.setMethods_insertBefore(a);
                            }
                            Pair<String, String> methodinsertb = new Pair<String, String>(curAct.getString("name"), curAct.getString("code"));
                            rule.getMethods_insertBefore().add(methodinsertb);
                            break;
                        case ("METHOD-INSERT-AFTER"):
                            if (rule.getMethods_insertAfter() == null) {
                                ArrayList<Pair<String, String>> a = new ArrayList<Pair<String, String>>();
                                rule.setMethods_insertAfter(a);
                            }
                            Pair<String, String> methodinserta = new Pair<String, String>(curAct.getString("name"), curAct.getString("code"));
                            rule.getMethods_insertAfter().add(methodinserta);
                            break;
                        case ("METHOD-INSIDE-CALL"):
                            if (rule.getMethod_inside_call() == null) {
                                ArrayList<Pair<String, SubstCall>> a = new ArrayList<Pair<String, SubstCall>>();
                                rule.setMethod_inside_call(a);
                            }
                            Pair<String, SubstCall> insidecall = new Pair<String, SubstCall>(curAct.getString("method-name"), new SubstCall(curAct.getString("classname"), curAct.getString("method-call-name"), curAct.getString("code")));
                            rule.getMethod_inside_call().add(insidecall);
                            break;
                        case ("METHOD-INSIDE-NEW"):
                            if (rule.getMethod_inside_new() == null) {
                                ArrayList<Pair<String, SubstNew>> a = new ArrayList<Pair<String, SubstNew>>();
                                rule.setMethod_inside_new(a);
                            }
                            Pair<String, SubstNew> insidenew = new Pair<String, SubstNew>(curAct.getString("method-name"), new SubstNew(curAct.getString("classname"), curAct.getString("code")));
                            rule.getMethod_inside_new().add(insidenew);
                            break;
                        case ("FIELD-REPLACE"):
                            if (rule.getFields_repl() == null) {
                                ArrayList<Pair<String, String>> a = new ArrayList<Pair<String, String>>();
                                rule.setFields_repl(a);
                            }
                            Pair<String, String> fieldsrpl = new Pair<String, String>(curAct.getString("name"), curAct.getString("code"));
                            rule.getFields_repl().add(fieldsrpl);
                            break;

                        default: // in case of parsing not matching type String
                            System.out.println("[+WARNING+] Invalid config file type entry at rule " + i + ", action " + j);
                            break;
                    }
                }
                lista.add(rule);
            }
        }else
            System.out.println("[INFO] Rule substitution disabled");
        return lista;
    }
    
    public Pair<Integer, String> parseSecurity() {
        JSONObject obj = new JSONObject(configfile);

        Pair<Integer, String> result = null;
        if (obj.has("security")) {
            JSONObject security = obj.getJSONObject("security");
            
            switch (security.getString("type")) {
                case ("default"):
                    result = new Pair(SecurityInitializer.SecurityType.FILE_SECURITY, new String());
                    break;
                case ("custom"):
                    if (security.has("path")) {
                        result = new Pair(SecurityInitializer.SecurityType.PATH_SECURITY,security.get("path"));
                    }else
                        result = new Pair(SecurityInitializer.SecurityType.POLICY_SECURITY, security.get("code"));
                    break;
                default:
                    System.out.println("[+WARNING+] Invalid config file security type etry");
                    result = null;
                    break;
            }
        } else {
            System.out.println("[INFO] Security manager disabled");
        }

        // will be null in case of no field security in json object
        return result;

    }
    
    public Pair <String,String> parseDeserialize(){
        JSONObject obj = new JSONObject(configfile);
        if (obj.has("serialkiller")){
            JSONObject serialk= obj.getJSONObject("serialkiller");
            Pair <String,String> risposta= new Pair<String,String>(serialk.getString("class"), serialk.getString("method"));
            return risposta;
        }
        return null;
    }
    
}
