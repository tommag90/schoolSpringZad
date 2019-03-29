package pl.edu.agh.ki.mwo.web.controllers;

import javax.servlet.http.HttpSession;
import javax.xml.crypto.Data;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pl.edu.agh.ki.mwo.model.School;
import pl.edu.agh.ki.mwo.model.SchoolClass;
import pl.edu.agh.ki.mwo.model.Student;
import pl.edu.agh.ki.mwo.persistence.DatabaseConnector;

@Controller
public class SchoolClassesController {

    @RequestMapping(value="/SchoolClasses")
    public String listSchoolClass(Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";

    	model.addAttribute("schoolClasses", DatabaseConnector.getInstance().getSchoolClasses());
    	model.addAttribute("schools", DatabaseConnector.getInstance().getSchools());
    	
        return "schoolClassesList";    
    }
    
    @RequestMapping(value="/AddSchoolClass")
    public String displayAddSchoolClassForm(Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";

       	model.addAttribute("schools", DatabaseConnector.getInstance().getSchools());
       	
        return "schoolClassForm";    
    }

    @RequestMapping(value="/CreateSchoolClass", method=RequestMethod.POST)
    public String createSchoolClass(@RequestParam(value="schoolClassStartYear", required=false) String startYear,
    		@RequestParam(value="schoolClassCurrentYear", required=false) String currentYear,
    		@RequestParam(value="schoolClassProfile", required=false) String profile,
    		@RequestParam(value="schoolClassSchool", required=false) String schoolId,
    		Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";
    	
    	SchoolClass schoolClass = new SchoolClass();
    	schoolClass.setStartYear(Integer.valueOf(startYear));
    	schoolClass.setCurrentYear(Integer.valueOf(currentYear));
    	schoolClass.setProfile(profile);
    	
    	DatabaseConnector.getInstance().addSchoolClass(schoolClass, schoolId);    	
       	model.addAttribute("schoolClasses", DatabaseConnector.getInstance().getSchoolClasses());
       	model.addAttribute("schools", DatabaseConnector.getInstance().getSchools());
    	model.addAttribute("message", "Nowa klasa została dodana");
         	
    	return "schoolClassesList";
    }
    
    @RequestMapping(value="/DeleteSchoolClass", method=RequestMethod.POST)
    public String deleteSchoolClass(@RequestParam(value="schoolClassId", required=false) String schoolClassId,
    		Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";
    	
    	DatabaseConnector.getInstance().deleteSchoolClass(schoolClassId);    	
       	model.addAttribute("schoolClasses", DatabaseConnector.getInstance().getSchoolClasses());
       	model.addAttribute("schools", DatabaseConnector.getInstance().getSchools());
    	model.addAttribute("message", "Klasa została usunięta");
         	
    	return "schoolClassesList";
    }

   @RequestMapping(value="/EditSchoolClass")
   public String editSchoolClass(@RequestParam(value="schoolClassId", required=false) String schoolClassId,
    		Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";
    	
    	SchoolClass sclass = DatabaseConnector.getInstance().getSchoolClassById(schoolClassId);
    	model.addAttribute("schoolClassId",sclass.getId());
    	System.out.println(sclass.getId());
    	model.addAttribute("schoolClassStartYear", sclass.getStartYear());
    	model.addAttribute("schoolClassCurrentYear", sclass.getCurrentYear());
    	model.addAttribute("schoolClassProfile",sclass.getProfile());
    	model.addAttribute("schoolClassSchool", DatabaseConnector.getInstance().getSchoolById(schoolClassId));
    	model.addAttribute("schoolClass", DatabaseConnector.getInstance().getSchoolClassById(schoolClassId));
    	model.addAttribute("schools",DatabaseConnector.getInstance().getSchools());
        return "schoolClassFormEdit";
    }
   
   @RequestMapping(value="/UpdateSchoolClass", method=RequestMethod.POST)
   public String updateSchoolClass(@RequestParam(value="schoolClassStartYear", required=false) String startYear,
   		@RequestParam(value="schoolClassCurrentYear", required=false) String currentYear,
   		@RequestParam(value="schoolClassProfile", required=false) String profile,
   		@RequestParam(value="schoolClassSchool", required=false) String schoolId,
   		@RequestParam(value="schoolClassId", required=false) String schoolClassId,
   		Model model, HttpSession session) {    	
   	if (session.getAttribute("userLogin") == null)
   		return "redirect:/Login";
   	
   	SchoolClass schoolClass = DatabaseConnector.getInstance().getSchoolClassById(schoolClassId);
   	schoolClass.setStartYear(Integer.valueOf(startYear));
   	schoolClass.setCurrentYear(Integer.valueOf(currentYear));
   	schoolClass.setProfile(profile);
   	
   	DatabaseConnector.getInstance().saveSchoolClass(schoolClass, schoolId); 	
    model.addAttribute("schoolClasses", DatabaseConnector.getInstance().getSchoolClasses());
    model.addAttribute("schools", DatabaseConnector.getInstance().getSchools());
   	model.addAttribute("message", "Edycja danych szkoły zakończona sukcesem");
        	
   	return "schoolClassesList";
   }
    
    
}