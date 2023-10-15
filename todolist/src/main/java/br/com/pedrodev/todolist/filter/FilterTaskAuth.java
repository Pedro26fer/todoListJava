package br.com.pedrodev.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.pedrodev.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

                var serveletPath = request.getServletPath();

                if(serveletPath.startsWith("/tasks/")){

                    var authorization = request.getHeader("Authorization");


                    if(authorization == null){
                        response.sendError(401);
                    }
                    else{

                        var auth_encode = authorization.substring("Basic".length()).trim();
    
                        byte[] authDecoded = Base64.getDecoder().decode(auth_encode);
        
                        var authString = new String (authDecoded);
                        String[] credentials = authString.split(":");
                        String username = credentials[0];
                        String password = credentials[1];
        
                        var user = this.userRepository.findByUsername(username);
        
                        if(user == null){
                            response.sendError(401);
                        }
                        else{
                            
                            var passwordVerified = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                            if(passwordVerified.verified){
                                request.setAttribute("idUser", user.getId());
                                filterChain.doFilter(request, response);
                            }
                            else{
                                response.sendError(401);
                            }
                        }
                    }

                   
                }

                else{
                    filterChain.doFilter(request, response);
                }


    }

}