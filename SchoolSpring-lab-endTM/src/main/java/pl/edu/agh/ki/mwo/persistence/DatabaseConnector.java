package pl.edu.agh.ki.mwo.persistence;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import pl.edu.agh.ki.mwo.model.School;
import pl.edu.agh.ki.mwo.model.SchoolClass;
import pl.edu.agh.ki.mwo.model.Student;

public class DatabaseConnector {
	
	protected static DatabaseConnector instance = null;
	
	public static DatabaseConnector getInstance() {
		if (instance == null) {
			instance = new DatabaseConnector();
		}
		return instance;
	}
	
	Session session;

	protected DatabaseConnector() {
		session = HibernateUtil.getSessionFactory().openSession();
	}
	
	public void teardown() {
		session.close();
		HibernateUtil.shutdown();
		instance = null;
	}
	
	public Iterable<School> getSchools() {
		String hql = "FROM School";
		Query query = session.createQuery(hql);
		List schools = query.list();
		
		return schools;
	}
	
	public School getSchool(long schoolId) {
		return (School) session.get(School.class, schoolId);
	}
	
	public void addSchool(School school) {
		Transaction transaction = session.beginTransaction();
		session.save(school);
		transaction.commit();
	}
	
	public void deleteSchool(String schoolId) {
		String hql = "FROM School S WHERE S.id=" + schoolId;
		Query query = session.createQuery(hql);
		List<School> results = query.list();
		Transaction transaction = session.beginTransaction();
		for (School s : results) {
			session.delete(s);
		}
		transaction.commit();
	}

	public Iterable<SchoolClass> getSchoolClasses() {
		String hql = "FROM SchoolClass";
		Query query = session.createQuery(hql);
		List schoolClasses = query.list();
		
		return schoolClasses;
	}
	
	public SchoolClass getSchoolClassById(String schoolClassId) {
		String hql = "FROM SchoolClass S WHERE S.id="+schoolClassId;
		Query query = session.createQuery(hql);
		SchoolClass schoolClassById = (SchoolClass) query.uniqueResult();
		return schoolClassById;
	}
	
	public School getSchoolById(String schoolClassId) {
		
		String hql = "SELECT s FROM School s INNER JOIN s.classes classes WHERE classes.id="+schoolClassId;
		Query query = session.createQuery(hql);
		School schoolById = (School) query.uniqueResult();
		
		return schoolById;
		
	}
	
	public void addSchoolClass(SchoolClass schoolClass, String schoolId) {
		String hql = "FROM School S WHERE S.id=" + schoolId;
		Query query = session.createQuery(hql);
		List<School> results = query.list();
		Transaction transaction = session.beginTransaction();
		if (results.size() == 0) {
			session.save(schoolClass);
		} else {
			School school = results.get(0);
			school.addClass(schoolClass);
			session.save(school);
		}
		transaction.commit();
	}
	

	
	public void deleteSchoolClass(String schoolClassId) {
		String hql = "FROM SchoolClass S WHERE S.id=" + schoolClassId;
		Query query = session.createQuery(hql);
		List<SchoolClass> results = query.list();
		Transaction transaction = session.beginTransaction();
		for (SchoolClass s : results) {
			session.delete(s);
		}
		transaction.commit();
	}
	
	public Iterable<School> getStudents() {
		String hql = "FROM Student";
		Query query = session.createQuery(hql);
		List students = query.list();
		
		return students;
	}
	
	public Student getStudentById(String studentId) {
		String hql = "FROM Student S WHERE S.id="+studentId;
		Query query = session.createQuery(hql);
		Student studentById = (Student) query.uniqueResult();
		return studentById;
	}
	
	public SchoolClass getStudentByIdClass(String studentId) {
		String hql = "Select S "
				+ "FROM SchoolClass S "
				+ "JOIN S.students studentsSet "
				+ "WHERE studentsSet.id="+studentId;
		Query query = session.createQuery(hql);
		SchoolClass studentByIdClass = (SchoolClass) query.uniqueResult();
		return studentByIdClass;
	}

	
	public void deleteStudent(String studentId) {
		String hql = "FROM Student S WHERE S.id=" + studentId;
		Query query = session.createQuery(hql);
		List<Student> results = query.list();
		Transaction transaction = session.beginTransaction();
		for (Student s : results) {
			session.delete(s);
		}
		transaction.commit();
	}
	
	public void addStudent(Student student, String schoolClassId) {
		String hql = "FROM SchoolClass S WHERE S.id=" + schoolClassId;
		Query query = session.createQuery(hql);
		List<SchoolClass> results = query.list();
		Transaction transaction = session.beginTransaction();
		if (results.size() == 0) {
			session.save(student);
		} else {
			SchoolClass schoolClass = results.get(0);
			schoolClass.addStudent(student);
			session.save(schoolClass);
		}
		transaction.commit();
	}
	
	public void saveSchoolClass(SchoolClass schoolClass, String schoolId) {
		String hql = "FROM School S WHERE S.id=" + schoolId;
		Query query = session.createQuery(hql);
		List<School> results = query.list();
		Transaction transaction = session.beginTransaction();
			School school = results.get(0);
			school.addClass(schoolClass);
			session.save(school);
		transaction.commit();
	}
}
