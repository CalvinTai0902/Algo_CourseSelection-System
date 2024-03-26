

import java.util.*;


public class HW2 {
    
  private HW2() {}
  
  public static class Course{
    int id = 0; // course's id. ITR->1, MIS->2, DataBase->3, ResearchMethod->4
    String name; // course's name
    Student[] candidate; // The course selection result. 
    int number;
    boolean available;
    // Course should initial by course id and name.
    private Course(int id, String name, int limit_number) {
        this.id = id;
        this.name = name;
        this.candidate = new Student[limit_number];
        this.number = 0;
    }
    
  }
  
  public static class Student{
    int year; // Student's year. freshman->1, sophomore->2, junior->3, senior ->4
    int id; // Unique student id
        //A set of student's preferences of courses id. e.g. [4,3,2,1]. The first priority of course is 4, which means ResearchMethod
    int[] preference;
    boolean choose;
    // Student should initial by year, id ,and candidate_courses.
    private Student(int year, int id, int[] preference) {
        this.year = year;
        this.preference = preference;
        this.id = id;
        this.choose = false;
    }
    
    //overriding the toString() method
    @Override
    public String toString()
    {
        return "大"+this.year + " 學號" + this.id;
    }
    
  }
     
  // Test case 1
  private static void test1() {
    Course[] courses = new Course[4];
    courses[0] = new Course(1, "ITR", 3);
    courses[1] = new Course(2, "MIS", 3);
    courses[2] = new Course(3, "DataBase", 3);
    courses[3] = new Course(4, "ResearchMethod", 3);
    
    Student[] students = new Student[12];
    students[0] =  new Student(1, 11, new int[]{1, 2, 3, 4});
    students[1] =  new Student(1, 12, new int[]{1, 2, 3, 4});
    students[2] =  new Student(1, 13, new int[]{1, 2, 3, 4});
    students[3] =  new Student(2, 21, new int[]{1, 2, 3, 4});
    students[4] =  new Student(2, 22, new int[]{1, 2, 3, 4});
    students[5] =  new Student(2, 23, new int[]{1, 2, 3, 4});
    students[6] =  new Student(3, 31, new int[]{1, 2, 3, 4});
    students[7] =  new Student(3, 32, new int[]{1, 2, 3, 4});
    students[8] =  new Student(3, 33, new int[]{1, 2, 3, 4});
    students[9] =  new Student(4, 41, new int[]{1, 2, 3, 4});
    students[10] =  new Student(4, 42, new int[]{1, 2, 3, 4});
    students[11] =  new Student(4, 43, new int[]{1, 2, 3, 4});
    
    System.out.println("Test case1:");
    long startTime = System.nanoTime();
    Course[] result = simulate(students, courses);
    long estimatedTime = System.nanoTime() - startTime;
    print_course(result);
    print_first_priority(result);
    System.out.println("Performance(time): "+estimatedTime);
  }
  
  // Test case 2
  private static void test2() {
    Course[] courses = new Course[3];
    courses[0] = new Course(1, "ITR", 6);
    courses[1] = new Course(2, "MIS", 2);
    courses[2] = new Course(3, "DataBase", 4);
    
    Student[] students = new Student[12];
    students[0] =  new Student(1, 11, new int[]{1, 2, 3});
    students[1] =  new Student(1, 12, new int[]{1, 2, 3});
    students[2] =  new Student(1, 13, new int[]{1, 2, 3});
    students[3] =  new Student(2, 21, new int[]{1, 2, 3});
    students[4] =  new Student(2, 22, new int[]{1, 2});
    students[5] =  new Student(2, 23, new int[]{2, 1, 3});
    students[6] =  new Student(3, 31, new int[]{1, 2, 3});
    students[7] =  new Student(3, 32, new int[]{1, 2, 3});
    students[8] =  new Student(3, 33, new int[]{1, 2, 3});
    students[9] =  new Student(4, 41, new int[]{});
    students[10] =  new Student(4, 42, new int[]{1, 2, 3});
    students[11] =  new Student(4, 43, new int[]{1, 2, 3});
    
    System.out.println("Test case2:");
    long startTime = System.nanoTime();
    Course[] result = simulate(students, courses);
    long estimatedTime = System.nanoTime() - startTime;
    print_course(result);
    print_first_priority(result);
    System.out.println("Performance(time): "+estimatedTime);
  }
  
  // Abstract test case 3. Prepare for hidden test case.
  // It is normal that the function is no code.
  private static void test3() {
  }
  
  
  
  static class StudentSort implements Comparator<Student>           //sort by year
  {	
	  @Override
	  public int compare(Student a, Student b) {
	  	  return b.year - a.year;                                   //override to sort by year 
	  }
		
   }

  public static void insertionSort(ArrayList<Student> hasPreference) {   //define insertionSort function to sort by student id
      int n = hasPreference.size(); 
      for (int j = 1; j < n; j++) {  
          Student key = hasPreference.get(j);  
          int i = j-1;  
          while ( (i > -1) && ( hasPreference.get(i).id > key.id ) 
        		  && (hasPreference.get(i).year ==key.year)) {          //if the year is equal, and id in the back is bigger than the front, 
        	  hasPreference.set(i+1, hasPreference.get(i));  			//then swap 
              i--;  
          }  
          hasPreference.set(i+1, key);                                  //find the right insert point then insert it          
      }  
  } 
  
  public static void freePrefer(Student student, Course[] courses) {    //define freePrefer function 
	  for(int k=0; k < courses.length; k++) {                           //run through the courses to find the course which still not full
		  if(courses[k].number < courses[k].candidate.length) {         //check if course id full
			  courses[k].candidate[courses[k].number++] = student;      // if not full, insert student into the candidate list
			  student.choose = true;                                    //renew the choose to true
			  break;             
		  }else continue;                                               //if is full then search next course
	  }
  }
  
  // Simulate courses allocating process
  private static Course[] simulate(Student[] students, Course[] courses) {
	  ArrayList<Student> hasPreference = new ArrayList<>(); //ArrayList for students with preference to sort
	  ArrayList<Student> noPreference = new ArrayList<>();
	        
	  for(Student student : students){             //go though all student data 
	      if(student.preference.length!=0) {       //if student have preference
	    	  hasPreference.add(student);          //add into hasPreference
	          continue;                            //then next run
	      }else{
	    	  noPreference.add(student);           //if no Preference add into noPreference
	      }
	   }
	  
	  Collections.sort(hasPreference, new StudentSort() );        //call sort function to sort by year for hasPreference
	  insertionSort(hasPreference);                               //than call insertonSort function to sort by id
	  if(noPreference.size() > 0) {
		  Collections.sort(noPreference, new StudentSort() );     //call sort function to sort noPreference by year
		  insertionSort(noPreference);                            //than sort by id using insertionSort
	  }
	  hasPreference.addAll(noPreference);                         //combine two array list into hasPreference
	  
	  for(int i = 0; i < hasPreference.size(); i++) {             //go through all sorted list: hasPreference
		  int[] prefer = hasPreference.get(i).preference;         //setup parameter as prefer
		  if(prefer.length > 0) {                                 //if student has Preference 
			  for(int j = 0 ; j < prefer.length; j++) {           //loop until go through all Preference list or student already get the course
				  int rank = prefer[j] -1;                        //setup the Preference array index
				  if(courses[rank].number < courses[rank].candidate.length) {                      //check if course is still have space
					  courses[rank].candidate[courses[rank].number++] =  hasPreference.get(i);     //put the student data into the course candidate list
					  hasPreference.get(i).choose = true;         //renew choose, means this student already chosen the course
					  break;
				  }
				  else continue;                                  //if course is full then pick next preference
			  }
			  if(!hasPreference.get(i).choose) {                  //if under the situation that student has preference but because of the low priority, 
				                								  //student doesn't chosen the course successfully
				  freePrefer(hasPreference.get(i), courses);      //call freePrefer function to choose course randomly
			  }
		  }
		  else freePrefer(hasPreference.get(i), courses);         //under the situation that students doesn't have preference, 
		                                                          //call freePrefer function to choose course randomly
	  }
      
      return courses;
  }
  


// helper function
  // print result of allocating the student to course
  private static void print_course(Course[] courses) {
    for(Course course:courses) {
        System.out.print(course.name+" : ");
        System.out.println(Arrays.toString(course.candidate));
    }
  }
  
  // helper function
  // Calculate the number of students who meet his first priority.
  private static void print_first_priority(Course[] courses) {
    int count = 0;
    for(Course course:courses) {
        for(Student one:course.candidate) {
            if (one != null &&  one.preference.length>0 && one.preference[0] == course.id) {
                count++;
            }
        }
    }
    System.out.println("Students satisfaction: "+ count);
  }


  public static void main(String[] args) {    
      test1();
//    test1 expected output: 
//    ITR : [大4 學號41, 大4 學號42, 大4 學號43]
//      MIS : [大3 學號31, 大3 學號32, 大3 學號33]
//      DataBase : [大2 學號21, 大2 學號22, 大2 學號23]
//      ResearchMethod : [大1 學號11, 大1 學號12, 大1 學號13]
//      Students satisfaction: 3
//      Performance(time): XXXX
      test2();
//    test2 expected output:
//    ITR : [大4 學號42, 大4 學號43, 大3 學號31, 大3 學號32, 大3 學號33, 大2 學號21]
//      MIS : [大2 學號22, 大2 學號23]
//      DataBase : [大1 學號11, 大1 學號12, 大1 學號13, 大4 學號41]
//      Students satisfaction: 7
//      Performance(time): XXXX
      test3(); // hidden test case 3
  }
}