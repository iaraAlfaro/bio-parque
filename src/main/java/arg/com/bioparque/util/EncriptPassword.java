package arg.com.bioparque.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncriptPassword {
        
    public String encriptar(String password){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}
