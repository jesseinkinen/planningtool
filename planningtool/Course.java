package planningtool;

public class Course {

		// Set Course variables
		private String courseName;
		private String courseId;
		private String semester;
		private String courseStatus;
		private int year;
		
		
		Course (String aCourseName, String aCourseId, String aSemester, String acourseStatus, int aYear)
		{
			this.courseName = aCourseName;
			this.courseId = aCourseId;
			this.semester = aSemester;
			this.courseStatus = acourseStatus;
			this.year = aYear;
		}

		// Getters for Schedule.java
		public String getCourseName()
		{
			return this.courseName;
		}
		
		public String getCourseId()
		{
			return this.courseId;
		}
		
		public String getSemester()
		{
			return this.semester;
		}
		
		public String getCourseStatus()
		{
			return this.courseStatus;
		}
		
		public int getYear()
		{
			return this.year;
		}
	}

