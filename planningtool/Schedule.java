package planningtool;

// Made with August Hermas and Ella Harmaala

import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
 
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
 
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.util.ArrayList;
 
/*
 * In this version we are creating a table that does not allow cell edition.
 * See method createCourseTableModel() below
 */
public class Schedule extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final static int MAX_QTY = 30; // Value that defines the max amount of Courses we can have in the application
 
    private ArrayList<Course> allCourses;
    private Course currentCourse;
   
    static JTable tableCourse;
    static JButton btnAddCourse;
    static JButton btnEditCourse;
    static JButton btnDeleteCourse;
    static DefaultTableModel myCourseTableModel;
   
    //Creating the GUI for the application including table that shows data from database
    public Schedule(){
        super("My Course Collection");
 
        new courseQueries();
       
        setDefaultCloseOperation(EXIT_ON_CLOSE); // For closing the application
        getContentPane().setLayout(null);
        setBounds(0,0,506,427);
        setLocationRelativeTo(null);
 
        JLabel lblTheseAreMy = new JLabel("These are my Courses:");
        lblTheseAreMy.setBounds(139, 10, 167, 14);
        getContentPane().add(lblTheseAreMy);
       
        tableCourse = new JTable();
        tableCourse.setShowGrid(false);
        tableCourse.setModel(createCourseTableModel()); // Creating a custom TableModel with Course data from the database and disabling cell edition
        tableCourse.setBounds(16, 40, 463, 213);
        getContentPane().add(tableCourse);
       
        btnAddCourse = new JButton("Add Course"); // Button to add a new course
        btnAddCourse.setBounds(300, 274, 117, 29);
        getContentPane().add(btnAddCourse);
       
        btnDeleteCourse = new JButton("Delete Course"); // Button to delete courses
        btnDeleteCourse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = tableCourse.getSelectedRow();
                DefaultTableModel model= (DefaultTableModel)tableCourse.getModel();
 
                String selected = model.getValueAt(row, 0).toString();
                               
                            //Option dialog to confirm the delete action
                            if (row >= 0) { int dialogResult = JOptionPane.showConfirmDialog (null, "Would you like to delete this course?","Warning",JOptionPane.OK_CANCEL_OPTION);
                            if(dialogResult == JOptionPane.YES_OPTION){
 
                                model.removeRow(row);
                           
                                try {
                                      // Deleting courses here and connecting to database
                                   
                                    Connection conn = (Connection) DriverManager.getConnection("jdbc:mysql://eu-cdbr-azure-west-b.cloudapp.net:3306/inkinen_jesse", "b9b0dcc9056907", "0d8483fc");
                                    PreparedStatement ps = (PreparedStatement) conn.prepareStatement("delete from courses where courseName='"+selected+"' ");
                                    ps.executeUpdate();
                                }
                                catch (Exception w) {
                                    JOptionPane.showMessageDialog(null, "Connection Error!");
                                }  
                            }                                    
                    }  
            }
           
        } );
        btnDeleteCourse.setBounds(16, 274, 143, 29);
        getContentPane().add(btnDeleteCourse);
       
       
        btnEditCourse = new JButton("Edit Course"); // Button to edit course
        btnEditCourse.setBounds(171, 274, 117, 29);
        getContentPane().add(btnEditCourse);
 
        MyEventHandler commandHandler = new MyEventHandler(); // Adding ActionListener to add course button
        btnAddCourse.addActionListener(commandHandler);
   
        MyEventHandler2 commandHandler1 = new MyEventHandler2(); // Adding ActionListener to edit course button
        btnEditCourse.addActionListener(commandHandler1);
    }
    /*
     * Creates a customized TableModel that:
     * Contains Course data retrieved from the database (via the allCourses ArrayList)
     */
    private DefaultTableModel createCourseTableModel()
    {
        allCourses = planningtool.courseQueries.getAllCourses();
 
        Object[][] data = new Object[allCourses.size()][5];
        String[] columns = new String[] {"Course Name", "Course ID", "Semester", "Status", "Year"};
       
        
        // This for loop will populate the fixed array "data" with the rows found from the allCourses ArrayList
         
        for (int row=0; row<allCourses.size(); row++){
           
            currentCourse = allCourses.get(row); // get a Course from the ArrayList allCourses
           
            data[row][0] = currentCourse.getCourseName();
            data[row][1] = currentCourse.getCourseId();
            data[row][2] = currentCourse.getSemester();
            data[row][3] = currentCourse.getCourseStatus();
            data[row][4] = currentCourse.getYear();
        }
 
        myCourseTableModel = new DefaultTableModel(data, columns) // "data" contains the Course data from the database
        {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column)  // Disabling cell edition
			{
				return false;
			}
};
        return myCourseTableModel;
    }
 
    private class MyEventHandler implements ActionListener // EventHandler for add course button
    {
        public void actionPerformed (ActionEvent myEvent)
        {
            if (myEvent.getSource() == btnAddCourse){
                if (allCourses.size() < MAX_QTY){ // If the current amount of Courses in the database is smaller than MAX_QTY ...
                    getNewCourseFromUser();
                    tableCourse.setModel(createCourseTableModel()); // Assigning a new TableModel to table containing up-to-date Course data
                }
                else{
                    JOptionPane.showMessageDialog(null, "You can not add more Courses in your collection", "Info", JOptionPane.INFORMATION_MESSAGE);
                }          
            }
        }
    }
   //Creating JPanel that adds new courses to JTable
    private void getNewCourseFromUser(){ 
        JTextField courseNameField = new JTextField(10); // TextField for courseName
        JTextField courseIdField = new JTextField(10); // TextField for CourseId
        JComboBox<String> semesterField = new JComboBox<String>(); // Dropdown menu for semester
        JComboBox<String> courseStatusField = new JComboBox<String>(); // Dropdown menu for status
        JTextField yearField = new JTextField(10); // TextField for year
 
        JPanel myPanel = new JPanel();
       
        myPanel.add(new JLabel("Course Name:"));
        myPanel.add(courseNameField);
       
        myPanel.add(new JLabel("Course ID:"));
        myPanel.add(courseIdField);
 
        myPanel.add(new JLabel("Semester:"));
        myPanel.add(semesterField);
        semesterField.addItem("Spring");
        semesterField.addItem("Autumn");
       
        myPanel.add(new JLabel("Status:"));
        myPanel.add(courseStatusField);
        courseStatusField.addItem("Scheduled");
        courseStatusField.addItem("Completed");
        courseStatusField.addItem("Failed");
        courseStatusField.addItem("Ongoing");
       
        myPanel.add(new JLabel("Year:"));
        myPanel.add(yearField);
       
        int result = JOptionPane.showConfirmDialog(null, myPanel, "Enter details of your new Course", JOptionPane.OK_CANCEL_OPTION);
       
        if (result == JOptionPane.OK_OPTION) {         
            // year should be passed as an integer to the addCourse method. Integer.parseInt() is used to convert a String to integer
        	// Calling courseQueries to Insert data to database
            planningtool.courseQueries.addCourse(courseNameField.getText(), courseIdField.getText(), semesterField.getSelectedItem(), courseStatusField.getSelectedItem(), Integer.parseInt(yearField.getText()));
        }
    }
   
    private class MyEventHandler2 implements ActionListener { // EventHandler for edit course button
        public void actionPerformed(ActionEvent thisEvent) {
            {
                if (thisEvent.getSource() == btnEditCourse){
                    if (allCourses.size() <= MAX_QTY){ // Works at any amount of courses
                        getCourseFromUser();
                        tableCourse.setModel(createCourseTableModel()); // Assigning a new TableModel to table containing up-to-date Course data
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Something Went Wrong, Try Again!", "Info", JOptionPane.INFORMATION_MESSAGE);
                    }          
                }
            }
        }
      //Creating JPanel to update data in JTable and database
        private void getCourseFromUser(){ 
           
        DefaultTableModel model = (DefaultTableModel)tableCourse.getModel();
   
      // Get selected row index
        int selectedRowIndex = tableCourse.getSelectedRow();
        String thisRow = model.getValueAt(selectedRowIndex, 0).toString();
           
            JTextField courseName = new JTextField(10); // TextField for CourseName
            JTextField courseId = new JTextField(10);	// TextField for CourseId
            JComboBox<String> semester = new JComboBox<String>(); // Dropdown menu for semester
            JComboBox<String> courseStatus = new JComboBox<String>(); // Dropdown menu for status
            JTextField year = new JTextField(10); // TextField for year
           
         JPanel thisPanel = new JPanel();
           
            thisPanel.add(new JLabel("Course Name:"));
            thisPanel.add(courseName);
           
            thisPanel.add(new JLabel("Course ID:"));
            thisPanel.add(courseId);
 
            thisPanel.add(new JLabel("Semester:"));
            thisPanel.add(semester);
            semester.addItem("Spring");
            semester.addItem("Autumn");
           
            thisPanel.add(new JLabel("Status:"));
            thisPanel.add(courseStatus);
            courseStatus.addItem("Scheduled");
            courseStatus.addItem("Completed");
            courseStatus.addItem("Failed");
            courseStatus.addItem("Ongoing");
           
            thisPanel.add(new JLabel("Year:"));
            thisPanel.add(year);
                   
            int result = JOptionPane.showConfirmDialog(null, thisPanel, "Enter the changes", JOptionPane.OK_CANCEL_OPTION);
           
            if (result == JOptionPane.OK_OPTION) {         
                // year should be passed as an integer to the addCourse method. Integer.parseInt() is used to convert a String to integer
            	// Calling courseQueries to update data to JTable and database
            	planningtool.courseQueries.editCourse(courseName.getText(), courseId.getText(), semester.getSelectedItem(), courseStatus.getSelectedItem(), Integer.parseInt(year.getText()), thisRow);
            }
           
                             
}      
       
};
   // Sets application to be visible
    public static void main(String[] args) {
        Schedule frame = new Schedule();
        frame.setVisible(true);
    }
}