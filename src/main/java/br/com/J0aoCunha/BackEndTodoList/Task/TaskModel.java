package br.com.J0aoCunha.BackEndTodoList.Task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;


@Data
@Entity(name = "tb_task")
public class TaskModel {
  
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private String description;

    @Column(length = 50)
    private String title;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String priority;

    private UUID idUser;

    @CreationTimestamp
    private LocalDateTime createdAtl;

   public void settitle(String title)  throws Exception{
        if(title.length() > 50){
        throw new Exception("O titulo não pode ter mais que 50 caracteres");
      }
      this.title = title;
    }
}
