package pl.edu.agh.ki.mwo.web.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pl.edu.agh.ki.mwo.model.School;
import pl.edu.agh.ki.mwo.model.SchoolClass;
import pl.edu.agh.ki.mwo.model.Student;
import pl.edu.agh.ki.mwo.persistence.DatabaseConnector;

@Controller
public class StudentsController {

    @RequestMapping(value="/Students")
    public String listStudent(Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";

    	model.addAttribute("students", DatabaseConnector.getInstance().getStudents());
    	model.addAttribute("schools", DatabaseConnector.getInstance().getSchools());
    	model.addAttribute("schoolClasses", DatabaseConnector.getInstance().getSchoolClasses());
    	
        return "studentsList";    
    }
    
    @RequestMapping(value="/AddStudent")
    public String displayAddStudentForm(Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";

       	model.addAttribute("schoolClasses", DatabaseConnector.getInstance().getSchoolClasses());
       	
        return "studentForm";    
    }
    
    @RequestMapping(value="/EditStudent")
    public String editStudent(@RequestParam(value="studentId", required=false) String studentId,
    		Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";
    	
    	Student student = DatabaseConnector.getInstance().getStudentById(studentId);
    	model.addAttribute("studentName", student.getName());
    	model.addAttribute("studentSurname", student.getSurname());
    	model.addAttribute("studentPesel", student.getPesel());
    	model.addAttribute("studentId",student.getId());
    	model.addAttribute("schoolClassStudent", DatabaseConnector.getInstance().getStudentByIdClass(studentId));
    	model.addAttribute("student", DatabaseConnector.getInstance().getStudentById(studentId));
    	model.addAttribute("schoolClasses", DatabaseConnector.getInstance().getSchoolClasses());
        return "studentFormEdit";
    }


    @RequestMapping(value="/CreateStudent", method=RequestMethod.POST)
    public String createStudent(@RequestParam(value="studentName", required=false) String name,
    		@RequestParam(value="studentSurname", required=false) String surname,
    		@RequestParam(value="studentPesel", required=false) String pesel,
    		@RequestParam(value="studentSchoolClass", required=false) String schoolClassId,
    		Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";
    	
    	Student student = new Student();
    	student.setName(name);
    	student.setSurname(surname);
    	student.setPesel(pesel);
    	
    	DatabaseConnector.getInstance().addStudent(student, schoolClassId);    	
       	model.addAttribute("students", DatabaseConnector.getInstance().getStudents());
    	model.addAttribute("message", "Nowy student został dodany");
         	
    	return "studentsList";
    }
    
    @RequestMapping(value="/UpdateStudent", method=RequestMethod.POST)
    public String updateStudent(@RequestParam(value="studentName", required=false) String name,
    		@RequestParam(value="studentSurname", required=false) String surname,
    		@RequestParam(value="studentPesel", required=false) String pesel,
    		@RequestParam(value="schoolClassStudent",required=false)  String schoolClassId,
    		@RequestParam(value="studentId", required=false) String studentId,
    		
    		Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";
    	
    	
    	Student student = DatabaseConnector.getInstance().getStudentById(studentId);
    
    	student.setName(name);
    	student.setSurname(surname);
    	student.setPesel(pesel);
    	
    	   
    	DatabaseConnector.getInstance().addStudent(student, schoolClassId);
    	model.addAttribute("schoolClasses", DatabaseConnector.getInstance().getSchoolClasses());
        model.addAttribute("schools", DatabaseConnector.getInstance().getSchools());
       	model.addAttribute("students", DatabaseConnector.getInstance().getStudents());
    	model.addAttribute("message", "Edycja danych studenta zakończona sukcesem");
         	
    	return "studentsList";
    }
  
    @RequestMapping(value="/DeleteStudent", method=RequestMethod.POST)
    public String deleteStudent(@RequestParam(value="studentId", required=false) String studentId,
    		Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";
    	
    	DatabaseConnector.getInstance().deleteStudent(studentId);    	
       	model.addAttribute("students", DatabaseConnector.getInstance().getStudents());
    	model.addAttribute("message", "Student został usunięty");
         	
    	return "studentsList";
    }


}