package university;

import java.util.logging.Logger;

/**
 * This class represents a university education system.
 * 
 * It manages students and courses.
 *
 */
public class University {
    // Private fields to store information
    private String name;
    private String rectorFirstName;
    private String rectorLastName;

    // Limits from the assignment hints
    private static final int MAX_STUDENTS = 1000;
    private static final int MAX_COURSES = 50;
    private static final int MAX_COURSES_PER_STUDENT = 25;

    // Arrays for students and courses
    private Student[] students = new Student[MAX_STUDENTS];
    private int studentCount = 0;
    private int nextStudentId = 10000;

    private Course[] courses = new Course[MAX_COURSES];
    private int courseCount = 0;
    private int nextCourseCode = 10;

    // --- Helper inner classes ---

    private static class Student {
        int id;
        String firstName;
        String lastName;

        int numCourses = 0;
        int[] courseCodes = new int[MAX_COURSES_PER_STUDENT];
        int[] courseGrades = new int[MAX_COURSES_PER_STUDENT]; // -1 means no exam grade yet

        Student(int id, String first, String last) {
            this.id = id;
            this.firstName = first;
            this.lastName = last;
            for (int i = 0; i < courseGrades.length; i++) {
                courseGrades[i] = -1;
            }
        }
    }

    private static class Course {
        int code;
        String title;
        String teacher;

        Course(int code, String title, String teacher) {
            this.code = code;
            this.title = title;
            this.teacher = teacher;
        }
    }

    // --- Helper search methods ---

    private Student findStudent(int id) {
        for (int i = 0; i < studentCount; i++) {
            if (students[i].id == id) {
                return students[i];
            }
        }
        return null;
    }

    private Course findCourse(int code) {
        for (int i = 0; i < courseCount; i++) {
            if (courses[i].code == code) {
                return courses[i];
            }
        }
        return null;
    }

// R1
    /**
     * Constructor
     * @param name name of the university
     */
    public University(String name){
        this.name = name;
        // Example of logging
        // logger.info("Creating extended university object");
    }
    
    /**
     * Getter for the name of the university
     * 
     * @return name of university
     */
    public String getName(){
        return name;
    }
    
    /**
     * Defines the rector for the university
     * 
     * @param first first name of the rector
     * @param last  last name of the rector
     */
    public void setRector(String first, String last){
        this.rectorFirstName = first;
        this.rectorLastName = last;
    }
    
    /**
     * Retrieves the rector of the university with the format "First Last"
     * 
     * @return name of the rector
     */
    public String getRector(){
        if (rectorFirstName == null || rectorLastName == null) {
            return null;
        }
        return rectorFirstName + " " + rectorLastName;
    }
    
// R2
    /**
     * Enrol a student in the university
     * The university assigns ID numbers 
     * progressively from number 10000.
     * 
     * @param first first name of the student
     * @param last last name of the student
     * 
     * @return unique ID of the newly enrolled student
     */
    public int enroll(String first, String last){
        if (studentCount >= MAX_STUDENTS) {
            // Not specified what to do; we just don't add more and return -1
            return -1;
        }
        int id = nextStudentId++;
        Student s = new Student(id, first, last);
        students[studentCount++] = s;

        logger.info("New student enrolled: " + id + ", " + first + " " + last);

        return id;
    }
    
    /**
     * Retrieves the information for a given student.
     * The university assigns IDs progressively starting from 10000
     * 
     * @param id the ID of the student
     * 
     * @return information about the student
     */
    public String student(int id){
        Student s = findStudent(id);
        if (s == null) return null;
        return s.id + " " + s.firstName + " " + s.lastName;
    }
    
// R3
    /**
     * Activates a new course with the given teacher
     * Course codes are assigned progressively starting from 10.
     * 
     * @param title title of the course
     * @param teacher name of the teacher
     * 
     * @return the unique code assigned to the course
     */
    public int activate(String title, String teacher){
        if (courseCount >= MAX_COURSES) {
            // Not specified what to do; we just don't add more and return -1
            return -1;
        }
        int code = nextCourseCode++;
        Course c = new Course(code, title, teacher);
        courses[courseCount++] = c;

        logger.info("New course activated: " + code + ", " + title + " " + teacher);

        return code;
    }
    
    /**
     * Retrieve the information for a given course.
     * 
     * The course information is formatted as a string containing 
     * code, title, and teacher separated by commas, 
     * e.g., {@code "10,Object Oriented Programming,James Gosling"}.
     * 
     * @param code unique code of the course
     * 
     * @return information about the course
     */
    public String course(int code){
        Course c = findCourse(code);
        if (c == null) return null;
        return c.code + "," + c.title + "," + c.teacher;
    }
    
// R4
    /**
     * Register a student to attend a course
     * @param studentID id of the student
     * @param courseCode id of the course
     */
    public void register(int studentID, int courseCode){
        Student s = findStudent(studentID);
        Course c = findCourse(courseCode);
        if (s == null || c == null) return;

        // Check if already registered
        for (int i = 0; i < s.numCourses; i++) {
            if (s.courseCodes[i] == courseCode) {
                // already registered
                return;
            }
        }

        if (s.numCourses >= MAX_COURSES_PER_STUDENT) {
            // cannot register to more courses
            return;
        }

        s.courseCodes[s.numCourses] = courseCode;
        s.courseGrades[s.numCourses] = -1; // no grade yet
        s.numCourses++;

        logger.info("Student " + studentID + " signed up for course " + courseCode);
    }
    
    /**
     * Retrieve a list of attendees.
     * 
     * The students appear one per row (rows end with `'\n'`) 
     * and each row is formatted as describe in in method {@link #student}
     * 
     * @param courseCode unique id of the course
     * @return list of attendees separated by "\n"
     */
    public String listAttendees(int courseCode){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < studentCount; i++) {
            Student s = students[i];
            for (int j = 0; j < s.numCourses; j++) {
                if (s.courseCodes[j] == courseCode) {
                    sb.append(student(s.id)).append('\n');
                    break;
                }
            }
        }
        return sb.toString();
    }

    /**
     * Retrieves the study plan for a student.
     * 
     * The study plan is reported as a string having
     * one course per line (i.e. separated by '\n').
     * The courses are formatted as describe in method {@link #course}
     * 
     * @param studentID id of the student
     * 
     * @return the list of courses the student is registered for
     */
    public String studyPlan(int studentID){
        Student s = findStudent(studentID);
        if (s == null) return null;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.numCourses; i++) {
            String courseInfo = course(s.courseCodes[i]);
            if (courseInfo != null) {
                sb.append(courseInfo).append('\n');
            }
        }
        return sb.toString();
    }

// R5
    /**
     * records the grade (integer 0-30) for an exam can 
     * 
     * @param studentId the ID of the student
     * @param courseID  course code 
     * @param grade     grade ( 0-30)
     */
    public void exam(int studentId, int courseID, int grade) {
        Student s = findStudent(studentId);
        if (s == null) return;

        // Find course index in student's list
        for (int i = 0; i < s.numCourses; i++) {
            if (s.courseCodes[i] == courseID) {
                s.courseGrades[i] = grade;

                logger.info("Student " + studentId + " took an exam in course " + courseID + " with grade " + grade);

                return;
            }
        }
        // If not registered to the course, do nothing (as per hint, this should not happen)
    }

    /**
     * Computes the average grade for a student and formats it as a string
     * using the following format 
     * 
     * {@code "Student STUDENT_ID : AVG_GRADE"}. 
     * 
     * If the student has no exam recorded the method
     * returns {@code "Student STUDENT_ID hasn't taken any exams"}.
     * 
     * @param studentId the ID of the student
     * @return the average grade formatted as a string.
     */
    public String studentAvg(int studentId) {
        Student s = findStudent(studentId);
        if (s == null) return null;

        int sum = 0;
        int count = 0;
        for (int i = 0; i < s.numCourses; i++) {
            int g = s.courseGrades[i];
            if (g >= 0) {
                sum += g;
                count++;
            }
        }

        if (count == 0) {
            return "Student " + studentId + " hasn't taken any exams";
        }

        double avg = (double) sum / count;
        return "Student " + studentId + " : " + avg;
    }
    
    /**
     * Computes the average grades of all students that took the exam for a given course.
     * 
     * The format is the following: 
     * {@code "The average for the course COURSE_TITLE is: COURSE_AVG"}.
     * 
     * If no student took the exam for that course it returns {@code "No student has taken the exam in COURSE_TITLE"}.
     * 
     * @param courseId  course code 
     * @return the course average formatted as a string
     */
    public String courseAvg(int courseId) {
        Course c = findCourse(courseId);
        if (c == null) return null;

        int sum = 0;
        int count = 0;

        for (int i = 0; i < studentCount; i++) {
            Student s = students[i];
            for (int j = 0; j < s.numCourses; j++) {
                if (s.courseCodes[j] == courseId && s.courseGrades[j] >= 0) {
                    sum += s.courseGrades[j];
                    count++;
                }
            }
        }

        if (count == 0) {
            return "No student has taken the exam in " + c.title;
        }

        double avg = (double) sum / count;
        return "The average for the course " + c.title + " is: " + avg;
    }
    

// R6
    /**
     * Retrieve information for the best students to award a price.
     * 
     * The students' score is evaluated as the average grade of the exams they've taken. 
     * To take into account the number of exams taken and not only the grades, 
     * a special bonus is assigned on top of the average grade: 
     * the number of taken exams divided by the number of courses the student is enrolled to, multiplied by 10.
     * The bonus is added to the exam average to compute the student score.
     * 
     * The method returns a string with the information about the three students with the highest score. 
     * The students appear one per row (rows are terminated by a new-line character {@code '\n'}) 
     * and each one of them is formatted as: {@code "STUDENT_FIRSTNAME STUDENT_LASTNAME : SCORE"}.
     * 
     * @return info on the best three students.
     */
    public String topThreeStudents() {
        Student[] bestStudents = new Student[3];
        double[] bestScores = new double[3];

        for (int i = 0; i < studentCount; i++) {
            Student s = students[i];

            int sum = 0;
            int exams = 0;
            for (int j = 0; j < s.numCourses; j++) {
                int g = s.courseGrades[j];
                if (g >= 0) {
                    sum += g;
                    exams++;
                }
            }

            if (exams == 0) {
                continue; // no exams, can't be in ranking
            }

            double avg = (double) sum / exams;
            int enrolled = s.numCourses;
            if (enrolled == 0) continue; // safety guard

            double bonus = ((double) exams / enrolled) * 10.0;
            double score = avg + bonus;

            // Insert into top 3 if appropriate
            for (int k = 0; k < 3; k++) {
                if (bestStudents[k] == null || score > bestScores[k]) {
                    // shift down
                    for (int m = 2; m > k; m--) {
                        bestStudents[m] = bestStudents[m - 1];
                        bestScores[m] = bestScores[m - 1];
                    }
                    bestStudents[k] = s;
                    bestScores[k] = score;
                    break;
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            if (bestStudents[i] != null) {
                sb.append(bestStudents[i].firstName)
                  .append(" ")
                  .append(bestStudents[i].lastName)
                  .append(" : ")
                  .append(bestScores[i])
                  .append('\n');
            }
        }
        return sb.toString();
    }

// R7
    /**
     * This field points to the logger for the class that can be used
     * throughout the methods to log the activities.
     */
    public static final Logger logger = Logger.getLogger("University");

}
