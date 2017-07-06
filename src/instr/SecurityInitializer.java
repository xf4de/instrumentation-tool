package instr;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;



/**
 *
 * @author Leonardo
 */
public class SecurityInitializer {
    
    public static final String POLICY_FILE_NAME="Security.policy";
    public static final String DEFAULT_FILE_POLICY="agentassets/defaultFilePolicy";
    
    public static class SecurityType{
        public static final Integer FILE_SECURITY=0;
        public static final Integer POLICY_SECURITY=1;
        public static final Integer PATH_SECURITY=2;
    }
    
    Pair<Integer,String> settings;
    
    public SecurityInitializer(Pair<Integer,String> p){
        if (p != null){ // if rules parsed correctly or if there are rules
            settings=p;
            setSecurityManager();
        }
    }
    private void setSecurityManager(){
        String policy=new String();
        
        if (settings.getKey().equals(SecurityType.FILE_SECURITY)){
            try {
                // load the default file limiting policy to the policy String
                policy=new String(Files.readAllBytes(Paths.get(DEFAULT_FILE_POLICY)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }else if(settings.getKey().equals(SecurityType.POLICY_SECURITY)){
            // load the given policy to the policy string
            policy=settings.getValue();
        }else if(settings.getKey().equals(SecurityType.PATH_SECURITY)){
            try {
                // load the file at the given path
                policy=new String(Files.readAllBytes(Paths.get(settings.getValue())));
            } catch (IOException ex) {ex.printStackTrace();}
        }
        
        // write a file that'll be the new security policy than set it as policy
        try {
            Files.write(Paths.get(POLICY_FILE_NAME), policy.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {} 
        catch (IOException ex){ex.printStackTrace();}
        
        // Set security manager with policy
        System.setProperty("java.security.policy",POLICY_FILE_NAME);
        System.setSecurityManager(new SecurityManager());
        
        System.out.println("[INFO] Successfully set Security Manager");
    }
}
