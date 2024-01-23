package br.com.J0aoCunha.BackEndTodoList.Filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.J0aoCunha.BackEndTodoList.user.IUserRepository;
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

        var ServletPath = request.getServletPath();

        if(ServletPath.startsWith("/task/")){
          // pegar usuario e senha
        var Authorization = request.getHeader("Authorization");

        var AuthEncoded = Authorization.substring("Basic".length()).trim();

        byte[] AuthDecode = Base64.getDecoder().decode(AuthEncoded);

        var AuthString = new String(AuthDecode);


        String[] Credentials = AuthString.split(":");
        String username = Credentials[0];
        String password = Credentials[1];

          // validar usuario
          var user = this.userRepository.findByUsername(username);
          if(user == null){
            response.sendError(401);
          }else{  
            //validar senha
           var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
           if(passwordVerify.verified){
            request.setAttribute("idUser", user.getId());
            filterChain.doFilter(request, response);
           }else{
            response.sendError(401);
           }
            //segue viagem    
           
          }
      }else{
      filterChain.doFilter(request, response);
    }

  }
}
