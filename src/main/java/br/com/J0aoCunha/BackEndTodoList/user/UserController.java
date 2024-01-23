package br.com.J0aoCunha.BackEndTodoList.user;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;


@RestController
@RequestMapping("/user")
public class UserController {
  
  @Autowired
  private IUserRepository userRepositoy;

  @PostMapping("/")
  public ResponseEntity<?> create( @RequestBody  UserModel userModel) {

   var user = this.userRepositoy.findByUsername(userModel.getUsername());

   if(user != null) {
    return ResponseEntity.badRequest().body("usuario ja existente");
   }

   var passWordHashred = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());

   userModel.setPassword(passWordHashred);

   var userCreated = this.userRepositoy.save(userModel);
   return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);

  }
}
