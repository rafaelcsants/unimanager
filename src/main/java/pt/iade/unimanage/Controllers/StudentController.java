package pt.iade.unimanage.Controllers;

import java.rmi.AccessException;
import java.util.List;
import java.util.logging.Logger;

import com.google.common.net.MediaType;

import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import pt.iade.unimanage.models.Enrolment;
import pt.iade.unimanage.models.Student;
import pt.iade.unimanage.models.StudentRepository;
import pt.iade.unimanage.models.Unit;

public class StudentController {
    private Logger logger = LoggerFactory.getLogger(StudentController.class);

 @GetMapping(path = "", produces= MediaType.APPLICATION_JSON_VALUE)
 public List <Student> getStudents() {
 logger.info("Sending all students");
 return StudentRepository.getStudents();
 }
 
 @ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
 private static final long serialVersionUID = 5405519104069955535L;
 public NotFoundException(String id, String elemType, String idName) {
 super(elemType+" with "+idName+" "+id+" not found.");
 }
}

@GetMapping(path = "{number}",
 produces= MediaType.APPLICATION_JSON_VALUE)
 public Student getStudent(@PathVariable("number") int number)
 throws NotFoundException{
 logger.info("Sending student with number "+number);
 Student student = StudentRepository.getStudent(number);
 if (student != null) return student;
 else throw new NotFoundException(""+number, "Student", "number");
 }


 @GetMapping(path = "{number}/enrolments",
 produces= MediaType.APPLICATION_JSON_VALUE)
 public List<Enrolment> getEnrolments(@PathVariable("number") int number) throws NotFoundException{
 logger.info("Sending enrolments of student with number "+number);
 Student student = StudentRepository.getStudent(number);
 if (student != null) return student.getEnrolments();
 else throw new NotFoundException(""+number, "Student", "number");
 }

 @GetMapping(path = "{number}/enrolments/{unitId}",
 produces= MediaType.APPLICATION_JSON_VALUE)
 public Enrolment getEnrolment(@PathVariable("number") int number,
 @PathVariable("unitId") int unitId) throws NotFoundException{
 logger.info("Sending enrolment with id "+unitId+
 " of student with number "+number);
 Student student = StudentRepository.getStudent(number);
 if (student != null) {
 Enrolment enr = student.getEnrolmentByUnitId(unitId);
 if (enr != null) return enr;
 else throw new NotFoundException(""+unitId, "Unit", "id");
 } else throw new NotFoundException(""+number, "Student", "number");
 
 }

 @PutMapping(path = "{number}/enrolments/{unitId}",
 produces= MediaType.APPLICATION_JSON_VALUE)
 public Enrolment setGrade(@PathVariable("number") int number,
 @PathVariable("unitId") int unitId,
 @RequestBody double grade)
 throws NotFoundException{
 logger.info("Setting grade of enrolment with id "+unitId+
 " of student with number "+number);
 Student student = StudentRepository.getStudent(number);
 if (student != null) {
 Enrolment enr = student.getEnrolmentByUnitId(unitId);
 if (enr != null) {
 enr.setGrade(grade);
 return enr;
 } else throw new NotFoundException(""+unitId, "Unit", "id");
 } else throw new NotFoundException(""+number, "Student", "number");
 }

}
