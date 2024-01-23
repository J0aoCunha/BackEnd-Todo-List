package br.com.J0aoCunha.BackEndTodoList.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.J0aoCunha.BackEndTodoList.utils.utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/task")
public class TaskController {
  
  @Autowired
  private ITaskRepository Taskrepository;


  @PostMapping("/")
  public ResponseEntity<?> Create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
    
    var idUser = request.getAttribute("idUser");
    taskModel.setIdUser((UUID)idUser);

    var currentDate = LocalDateTime.now();
    if(currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data de inicio / data de termino não pode ser menor que a data atual");
    }

    if(taskModel.getStartAt().isAfter(taskModel.getEndAt())){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio deve ser menor que a data de termino");
    }

    
    var task = this.Taskrepository.save(taskModel);
    return ResponseEntity.status(HttpStatus.CREATED).body(task);
  }


  @GetMapping("/")
  public List<TaskModel> ListTasks(HttpServletRequest request) {
    var idUser = (UUID)request.getAttribute("idUser");
    var tasks = this.Taskrepository.findByIdUser(idUser);

    return  tasks;
  }


  @PutMapping("/{id}")
  public ResponseEntity<?> Update(@RequestBody TaskModel taskModel, @PathVariable UUID id, HttpServletRequest request){
      
      var task = this.Taskrepository.findById(id).orElse(null);
      var idUser = (UUID)request.getAttribute("idUser");

      if(task == null){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa não encontrada");
      }

      if(!task.getIdUser().equals(idUser)){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Você não tem permissão para alterar essa tarefa");
      }

      utils.copyNonNullPropeties(taskModel, task);
      var taskupdated = this.Taskrepository.save(task);
      return  ResponseEntity.status(HttpStatus.OK).body(taskupdated);
  }
}
