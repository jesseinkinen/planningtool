package planningtool;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
/*
 * This Class manages database operations associated to the Course Class
 * Don't worry yet about the try/catch/finally blocks in some methods. This will be explained later in the course
 */
public class courseQueries {
    // DB connection details
    private static final String URL = "jdbc:mysql://eu-cdbr-azure-west-b.cloudapp.net:3306/inkinen_jesse";
	private static final String USERNAME = "b9b0dcc9056907";
	private static final String PASSWORD = "0d8483fc";
 
    private Connection connection = null;
    private static PreparedStatement selectAllCourses = null;
    private static PreparedStatement insertCourse = null;
    private static PreparedStatement editCourse = null;
   
   
    public courseQueries()
    {
        try
        {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD); // Starts a connection to the database
            selectAllCourses = connection.prepareStatement("SELECT * FROM courses"); // Prepare the select query that gets all cars from the database
           
            insertCourse = connection.prepareStatement("INSERT INTO courses VALUES (?,?,?,?,?)");  // Prepare the insert query
           
            editCourse = connection.prepareStatement("UPDATE courses SET courseName = ?, courseId = ?, semester = ?, courseStatus = ?, year = ? WHERE courseName = ?"); // Prepare the update query
           
        }// end try
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
            System.exit(1);
        }
    }
   
   
    /*
     * This method will execute the select query that gets all Courses from the database.
     * It returns an ArrayList containing Courses objects initialized with Course data from each row in the courses table (database)
     */
    public static ArrayList<Course> getAllCourses()
    {
        ArrayList<Course> results = null;
        ResultSet resultSet = null;
       
        try
        {
            resultSet = selectAllCourses.executeQuery(); // Execute the select query. resultSet contains the rows returned by the query
            results = new ArrayList<Course>();
       
            while(resultSet.next()) // for each row returned by the select query
            {
                // Initialize a new Course object with the row's data. Add the Course object to the results ArrayList
                results.add(new Course(
                    resultSet.getString("courseName"), // get the value associated to the courseName column
                    resultSet.getString("courseId"), // get the value associated to the courseId column
                    resultSet.getString("semester"), // get the value associated to the semester column
                    resultSet.getString("courseStatus"), // get the value associated to the status column
                    resultSet.getInt("year"))); // get the value associated to the year column
            }
        } // end try
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        finally
        {
            try
            {
                resultSet.close();
            }
            catch (SQLException sqlException)
            {
                sqlException.printStackTrace();
            }
        } // end finally
       
        return results;
    } // end method getAllCourses
   
    
     // Method that inserts a new Courses in the database
 
    public static void addCourse(String courseName, String courseId, Object semester, Object courseStatus, int year)
    {
        try
        {
            // Setting the values for the question marks '?' in the prepared statement
            insertCourse.setString(1, courseName);
            insertCourse.setString(2, courseId);
            insertCourse.setString(3, (String) semester);
            insertCourse.setString(4, (String) courseStatus);
            insertCourse.setInt(5, year);
           
            //executes the updates
            insertCourse.executeUpdate();
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
        }  
    }
   
    public static void editCourse(String courseName, String courseId, Object semester, Object courseStatus, int year, String thisRow)
    {
        try
        {
            // Setting the values for the question marks '?' in the prepared statement
            editCourse.setString(1, courseName);
            editCourse.setString(2, courseId);
            editCourse.setString(3, (String) semester);
            editCourse.setString(4, (String) courseStatus);
            editCourse.setInt(5, year);
            editCourse.setString(6, thisRow);
           
           //executes the updates
             editCourse.executeUpdate();
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
        }  
    }
}
