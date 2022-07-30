import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class AdminPage extends UniversityData {
    private final Object[] courseColumns = {"CRN","Course", "Section", "Days", "Time", "Room", "Instructor", "Credit"};
    private final Object[] courseColumns2 = {"CRN","Course", "Section", "Days", "Time", "Room", "Credit"};
    private final Object[] instructorCourseColumns = {"CRN","Course", "Section", "Days", "Time", "Room", "Credit"};
    private final Object[] studentList = {"Name","ID"};
    private int[] studentCourses = {0,0,0,0,0,0,0};
    private Object[][] courseData, instructorCourseData, studentData;
    private JFrame menuFrame;
    private JPanel headerPanel, mainMenuPanel, deletePanel, coursePanel, instructorPanel, selectCoursePanel, selectCoursePanel2, courseTablePanel,
            addCoursePanel,selectInstructorPanel,addInstructorPanel, instructorCourseTablePanel,instructorAssignSectionPanel, studentPanel
            , studentTablePanel, addStudentPanel, studentCoursesPanel, departmentCoursePanel, selectCoursePanel3
            , studentAssignSectionPanel;
    private JButton logout, courses, instructors, students, backBtn, backBtn2,backBtn3,backBtn4, updateBtn, mainMenuBtn,
            submitBtn, submitBtn2, submitBtn3,deleteCourse,deleteSection, editBtn, deleteInstructor, deleteInstructorSection
            , assignCourse, submitBtn4, backBtn5, backBtn6, assignSection, viewScheduleBtn, deleteStudentBtn, addStudentBtn
            , deleteStudentCourse, addStudentCourseBtn, assignStudentSectionBtn, backBtn7, mainMenuBtn2, mainMenuBtn3;
    private String[] departments = {"Electrical Engineer", "Computer Science", "Mechanical Engineer"};
    private ArrayList<String> departmentCourses,departmentCourses2, departmentInstructor;
    private JComboBox departmentList;
    private JComboBox courseList,courseList2, courseList3, instructorList;
    private ItemSelectable is;
    private static String selectedCourse,selectedCourse2, selectedInstructor, selectedStudentID;
    private JTextField crnTxt, courseTxt, sectionTxt, nameTxt, maxStuTxt, daysTxt, timeTxt,roomTxt,instructorTxt,
            creditTxt, departmentTxt, startDateTxt,endDateTxt;
    private JTextField idTxt, userNameTxt, passwordTxt, jobTxt, userDepartmentTxt;
    private boolean edit = true;
    private int count = 0;
    private JTable coursesTable,coursesTable2, coursesTable3,  instructorCoursesTable, studentListTable, studentCourseTable;
    private DefaultTableModel courseTableModel,courseTableModel2,courseTableModel3, InstructorCourseTableModel, studentListTableModel, studentCourseTableModel;
    actionHandlerCourse courseHandler = new actionHandlerCourse();
    actionHandlerInstructor instructorHandler = new actionHandlerInstructor();
    actionHandlerStudent studentHandler = new actionHandlerStudent();

    //ADMIN MAIN MENU
    AdminPage(String id) {
        //HEADER OF ADMIN MAIN MENU
        ImageIcon icon = new ImageIcon("KU-logo.jpg");
        ImageIcon adminIcon = new ImageIcon("userIcon.png");
        logoutHandler outH = new logoutHandler();
        logout = new JButton("Log Out");
        logout.addActionListener(outH);
        logout.setBackground(new Color(255, 83, 73));
        logout.setFocusable(false);
        headerPanel = new JPanel();
        headerPanel.setBounds(0, 0, 787, 78);
        headerPanel.setBackground(Color.WHITE);
        Border border = new LineBorder(Color.BLACK, 3, false);
        headerPanel.setBorder(border);
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        headerPanel.add(new JLabel(adminIcon));
        //ADD LABEL ADMIN NAME, ADD LABEL ID
        JLabel name = new JLabel(getUserName(id) + " - " + id);
        name.setFont(new Font("Arial", Font.BOLD, 22));
        headerPanel.add(new JLabel("                "));
        headerPanel.add(name);
        headerPanel.add(new JLabel("                                              " +
                "                "));
        headerPanel.add(logout);

        //BODY PANEL OF THE ADMIN MAIN MENU
        ImageIcon courseIcon = new ImageIcon("courseIcon.png");
        ImageIcon instructorIcon = new ImageIcon("instructorIcon.png");
        ImageIcon studentIcon = new ImageIcon("studentIcon.png");

        selectHandler act1 = new selectHandler();
        courses = new JButton("Courses");
        courses.setFont(new Font("Arial", Font.BOLD, 30));
        courses.setPreferredSize(new Dimension(400, 50));
        courses.setFocusable(false);
        courses.setBackground(new Color(209,165,100));
        courses.addActionListener(act1);

        selectHandler act2 = new selectHandler();
        instructors = new JButton("Instructors");
        instructors.setFont(new Font("Arial", Font.BOLD, 30));
        instructors.setPreferredSize(new Dimension(400, 50));
        instructors.setFocusable(false);
        instructors.setBackground(new Color(209,165,100));
        instructors.addActionListener(act2);

        selectHandler act3 = new selectHandler();
        students = new JButton("Students");
        students.setFont(new Font("Arial", Font.BOLD, 30));
        students.setPreferredSize(new Dimension(400, 50));
        students.setFocusable(false);
        students.setBackground(new Color(209,165,100));
        students.addActionListener(act3);

        mainMenuPanel = new JPanel();
        mainMenuPanel.setBounds(100, 100, 700, 300);
        mainMenuPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 40, 36));
        mainMenuPanel.setBackground(Color.WHITE);
        mainMenuPanel.add(courses);
        mainMenuPanel.add(new JLabel(courseIcon));
        mainMenuPanel.add(instructors);
        mainMenuPanel.add(new JLabel(instructorIcon));
        mainMenuPanel.add(students);
        mainMenuPanel.add(new JLabel(studentIcon));

        menuFrame = new JFrame();
        menuFrame.setTitle("Student Registration Program SKMM");
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setSize(800, 480);
        menuFrame.setResizable(false);
        menuFrame.setLayout(null);
        menuFrame.setIconImage(icon.getImage());
        menuFrame.getContentPane().setBackground(Color.WHITE);

        menuFrame.add(headerPanel);
        menuFrame.add(mainMenuPanel);
        menuFrame.setVisible(true);

    }

    //COURSE PAGES
    public void departmentSelectionCourse() {
        backBtn = new JButton("Back");
        submitBtn = new JButton("Submit");
        actionHandlerCourse submit = new actionHandlerCourse();
        backBtn.addActionListener(submit);
        backBtn.setFocusable(false);
        submitBtn.setFocusable(false);
        submitBtn.addActionListener(submit);
        coursePanel = new JPanel();
        coursePanel.setBounds(250, 135, 300, 300);
        coursePanel.setBackground(Color.white);
        departmentList = new JComboBox();
        departmentList.setPreferredSize(new Dimension(300, 30));
        JLabel templabel = new JLabel("Please select a department");
        templabel.setFont(new Font("Arial", Font.BOLD, 15));
        int count = 0;
        for (int i = 0; i < departments.length; i++)
            departmentList.addItem(departments[count++]);
        coursePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 35));
        coursePanel.add(templabel);
        coursePanel.add(departmentList);
        coursePanel.add(backBtn);
        coursePanel.add(submitBtn);
        menuFrame.add(coursePanel);


    }

    public void selectCourse(String department) {
        Connection con = UniversityData.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            System.out.println(department);
            String sql = "SELECT Course FROM courseMain WHERE Department IS " + department;

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            departmentCourses = new ArrayList<String >();
            while (rs.next()) {
                departmentCourses.add(rs.getString(1));
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            try {
                con.close();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        backBtn2 = new JButton("Back");
        submitBtn2 = new JButton("Submit");
        actionHandlerCourse submit = new actionHandlerCourse();
        backBtn2.addActionListener(submit);
        backBtn2.setFocusable(false);
        submitBtn2.setFocusable(false);
        submitBtn2.addActionListener(submit);
        selectCoursePanel = new JPanel();
        selectCoursePanel.setBounds(250, 135, 300, 300);
        selectCoursePanel.setBackground(Color.white);
        courseList = new JComboBox();
        courseList.setPreferredSize(new Dimension(300, 30));
        JLabel tempLabel = new JLabel("Select a Course");
        tempLabel.setFont(new Font("Arial", Font.BOLD, 15));
        for (int i = 0; i < departmentCourses.size(); i++)
            courseList.addItem(departmentCourses.get(i));
        courseList.addItem("Add Course");
        selectCoursePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 35));
        selectCoursePanel.add(tempLabel);
        selectCoursePanel.add(courseList);
        selectCoursePanel.add(backBtn2);
        selectCoursePanel.add(submitBtn2);

        deleteCourse = new JButton("Delete Course");
        deleteCourseHandler DeleteHandler = new deleteCourseHandler();
        deleteCourse.addActionListener(DeleteHandler);
        deleteCourse.setFocusable(false);
        deleteCourse.setBackground(Color.RED);

        selectCoursePanel.add(deleteCourse);
        menuFrame.add(selectCoursePanel);

        courseList.addItemListener(itemListenerCourse);

    }

    public void addCourse(String department) {
        addCoursePanel = new JPanel();
        addCoursePanel.setBackground(Color.WHITE);
        addCoursePanel.setLayout(new GridLayout(7,4));
        addCoursePanel.setBounds(70, 125, 600, 250);

        crnTxt = new JTextField();
        courseTxt = new JTextField();
        sectionTxt = new JTextField();
        nameTxt = new JTextField();
        maxStuTxt = new JTextField();
        daysTxt = new JTextField();
        timeTxt = new JTextField();
        roomTxt = new JTextField();
        instructorTxt = new JTextField();
        creditTxt = new JTextField();
        departmentTxt = new JTextField(department);
        startDateTxt = new JTextField();
        endDateTxt = new JTextField();
        submitBtn3 = new JButton("Submit");
        backBtn3 = new JButton("Back");

        addCoursePanel.add(new JLabel("CRN:"));
        addCoursePanel.add(crnTxt);
        addCoursePanel.add(new JLabel("Course:"));
        addCoursePanel.add(courseTxt);
        addCoursePanel.add(new JLabel("Section:"));
        addCoursePanel.add(sectionTxt);
        addCoursePanel.add(new JLabel("Name:"));
        addCoursePanel.add(nameTxt);
        addCoursePanel.add(new JLabel("Max Students:"));
        addCoursePanel.add(maxStuTxt);
        addCoursePanel.add(new JLabel("Days:"));
        addCoursePanel.add(daysTxt);
        addCoursePanel.add(new JLabel("Time:"));
        addCoursePanel.add(timeTxt);
        addCoursePanel.add(new JLabel("Room:"));
        addCoursePanel.add(roomTxt);
        addCoursePanel.add(new JLabel("Instructor:"));
        addCoursePanel.add(instructorTxt);
        addCoursePanel.add(new JLabel("Credit:"));
        addCoursePanel.add(creditTxt);
        addCoursePanel.add(new JLabel("Start Date:"));
        addCoursePanel.add(startDateTxt);
        addCoursePanel.add(new JLabel("End Date:"));
        addCoursePanel.add(endDateTxt);
        addCoursePanel.add(new JLabel(""));
        addCoursePanel.add(backBtn3);
        addCoursePanel.add(submitBtn3);

        submitBtn3.addActionListener(courseHandler);
        backBtn3.addActionListener(courseHandler);
        menuFrame.add(addCoursePanel);



    }

    public void courseTable(String course) {
        Connection con = UniversityData.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        courseTableModel = new DefaultTableModel(courseData, courseColumns) {
            public Class getColumnClass(int column) {
                Class returnValue;

                if ((column >= 0) && (column < getColumnCount())) {
                    returnValue = getValueAt(0, column).getClass();
                } else {
                    returnValue = Object.class;
                }
                return returnValue;
            }
        };
        try {
            String sql = "SELECT CRN, Course, Section, Days, Time, Room, Instructor, Credit FROM courses WHERE" +
                    " Course IS " + course;

            Object[] tempRow;

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                tempRow = new Object[]{rs.getInt(1),rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getString(6), rs.getString(7), rs.getInt(8)};
                System.out.println("Row Added");
                courseTableModel.addRow(tempRow);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            try {
                rs.close();
                ps.close();
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        coursesTable = new JTable(courseTableModel) {
            public boolean isCellEditable(int data, int columns) {
                if (edit)
                    return false;
                return true;
            }
        };
        coursesTable.setAutoCreateColumnsFromModel(true);
        JScrollPane scrollPane = new JScrollPane(coursesTable);
        coursesTable.setPreferredScrollableViewportSize(new Dimension(650, 80));
        coursesTable.setFillsViewportHeight(true);
        coursesTable.setDragEnabled(false);

        TableColumnModel columnModel = coursesTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(17);
        columnModel.getColumn(1).setPreferredWidth(25);
        columnModel.getColumn(2).setPreferredWidth(17);
        columnModel.getColumn(3).setPreferredWidth(17);
        columnModel.getColumn(7).setPreferredWidth(17);

        courseTablePanel = new JPanel();

        JLabel courseName = new JLabel(course);
        courseName.setFont(new Font("Arial", Font.BOLD, 22));
        courseTablePanel.add(courseName);
        courseTablePanel.add(scrollPane, BorderLayout.CENTER);



        backBtn4 = new JButton("Back");
        editBtn = new JButton("Edit");
        updateBtn = new JButton("Update");
        mainMenuBtn = new JButton("Main Menu");
        deleteSection = new JButton("Delete");
        deleteSection.setBackground(Color.RED);
        backBtn4.addActionListener(courseHandler);
        editBtn.addActionListener(courseHandler);
        updateBtn.addActionListener(courseHandler);
        mainMenuBtn.addActionListener(courseHandler);
        deleteSectionHandler deleteSectionHandler = new deleteSectionHandler();
        deleteSection.addActionListener(deleteSectionHandler);

        courseTablePanel.add(backBtn4);
        courseTablePanel.add(editBtn);
        courseTablePanel.add(updateBtn);
        courseTablePanel.add(deleteSection);
        courseTablePanel.add(mainMenuBtn);

        courseTablePanel.setBounds(69, 135, 660, 270);
        courseTablePanel.setBackground(Color.WHITE);
        menuFrame.add(courseTablePanel);

    }

    //INSTRUCTOR PAGES
    public void departmentSelectionInstructor() {
        backBtn = new JButton("Back");
        submitBtn = new JButton("Submit");
        backBtn.addActionListener(instructorHandler);
        backBtn.setFocusable(false);
        submitBtn.setFocusable(false);
        submitBtn.addActionListener(instructorHandler);
        instructorPanel = new JPanel();
        instructorPanel.setBounds(250, 135, 300, 300);
        instructorPanel.setBackground(Color.white);
        departmentList = new JComboBox();
        departmentList.setPreferredSize(new Dimension(300, 30));
        JLabel templabel = new JLabel("Please select a department");
        templabel.setFont(new Font("Arial", Font.BOLD, 15));
        int count = 0;
        for (int i = 0; i < departments.length; i++)
            departmentList.addItem(departments[count++]);
        instructorPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 35));
        instructorPanel.add(templabel);
        instructorPanel.add(departmentList);
        instructorPanel.add(backBtn);
        instructorPanel.add(submitBtn);
        menuFrame.add(instructorPanel);


    }

    public void selectInstructor(String Department) {
        Connection con = UniversityData.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            System.out.println(Department);
            String sql = "SELECT Name FROM users WHERE Job IS 'Instructor' AND Department IS "+ Department;

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            departmentInstructor = new ArrayList<String >();
            while (rs.next()) {
                departmentInstructor.add(rs.getString(1));
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            try {
                con.close();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        backBtn2 = new JButton("Back");
        submitBtn2 = new JButton("Submit");
        backBtn2.addActionListener(instructorHandler);
        backBtn2.setFocusable(false);
        submitBtn2.setFocusable(false);
        submitBtn2.addActionListener(instructorHandler);

        selectInstructorPanel = new JPanel();
        selectInstructorPanel.setBounds(250, 135, 300, 300);
        selectInstructorPanel.setBackground(Color.white);
        instructorList = new JComboBox();
        instructorList.setPreferredSize(new Dimension(300, 30));
        JLabel tempLabel = new JLabel("Select an Instructor");
        tempLabel.setFont(new Font("Arial", Font.BOLD, 15));
        for (int i = 0; i < departmentInstructor.size(); i++)
            instructorList.addItem(departmentInstructor.get(i));
        instructorList.addItem("Add Instructor");
        selectInstructorPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 35));
        selectInstructorPanel.add(tempLabel);
        selectInstructorPanel.add(instructorList);
        selectInstructorPanel.add(backBtn2);
        selectInstructorPanel.add(submitBtn2);

        deleteInstructor = new JButton("Delete Instructor");
        deleteInstructorHandler DeleteHandler = new deleteInstructorHandler();
        deleteInstructor.addActionListener(DeleteHandler);
        deleteInstructor.setFocusable(false);
        deleteInstructor.setBackground(Color.RED);

        selectInstructorPanel.add(deleteInstructor);
        menuFrame.add(selectInstructorPanel);

        instructorList.addItemListener(itemListenerInstructor);

    }

    public void addInstructor(String Department) {
        addInstructorPanel = new JPanel();
        addInstructorPanel.setBackground(Color.WHITE);
        addInstructorPanel.setLayout(new GridLayout(3,4));
        addInstructorPanel.setBounds(70, 220, 500, 80);

        idTxt = new JTextField();
        userNameTxt = new JTextField();
        passwordTxt = new JTextField();
        jobTxt = new JTextField("Instructor");
        userDepartmentTxt = new JTextField(Department);
        submitBtn3 = new JButton("Submit");
        backBtn3 = new JButton("Back");

        addInstructorPanel.add(new JLabel("ID:"));
        addInstructorPanel.add(idTxt);
        addInstructorPanel.add(new JLabel("Name:"));
        addInstructorPanel.add(userNameTxt);
        addInstructorPanel.add(new JLabel("Password:"));
        addInstructorPanel.add(passwordTxt);
        addInstructorPanel.add(new JLabel(""));
        addInstructorPanel.add(new JLabel(""));

        addInstructorPanel.add(new JLabel(""));
        addInstructorPanel.add(backBtn3);
        addInstructorPanel.add(submitBtn3);

        submitBtn3.addActionListener(instructorHandler);
        backBtn3.addActionListener(instructorHandler);
        menuFrame.add(addInstructorPanel);
    }

    public void instructorCourseTable(String Name) {
        Connection con = UniversityData.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String ID = "";
        InstructorCourseTableModel = new DefaultTableModel(instructorCourseData, instructorCourseColumns) {
            public Class getColumnClass(int column) {
                Class returnValue;

                if ((column >= 0) && (column < getColumnCount())) {
                    returnValue = getValueAt(0, column).getClass();
                } else {
                    returnValue = Object.class;
                }
                return returnValue;
            }
        };
        try {
            String sql = "SELECT ID FROM users WHERE Name IS " + Name;
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next())
            ID = rs.getString("ID");

            System.out.println("ID SELECTED" + ID);

            sql = "SELECT CRN, Course, Section, Days, Time, Room, Credit FROM courses WHERE Instructor IS " + Name;

            System.out.println("COURSES SELECTED");
            Object[] tempRow;

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                tempRow = new Object[]{rs.getInt(1),rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getString(6), rs.getInt(7)};
                System.out.println("Row Added");
                InstructorCourseTableModel.addRow(tempRow);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                ps.close();
                rs.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        instructorCoursesTable = new JTable(InstructorCourseTableModel) {
            public boolean isCellEditable(int data, int columns) {
                if (edit)
                    return false;
                return true;
            }
        };
        instructorCoursesTable.setAutoCreateColumnsFromModel(true);
        JScrollPane scrollPane = new JScrollPane(instructorCoursesTable);
        instructorCoursesTable.setPreferredScrollableViewportSize(new Dimension(650, 80));
        instructorCoursesTable.setFillsViewportHeight(true);
        instructorCoursesTable.setDragEnabled(false);

        TableColumnModel columnModel = instructorCoursesTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(17);
        columnModel.getColumn(1).setPreferredWidth(25);
        columnModel.getColumn(2).setPreferredWidth(17);
        columnModel.getColumn(3).setPreferredWidth(17);
        columnModel.getColumn(6).setPreferredWidth(17);

        instructorCourseTablePanel = new JPanel();

        JLabel instructorName = new JLabel(Name + " - " + ID);
        instructorName.setFont(new Font("Arial", Font.BOLD, 22));
        instructorCourseTablePanel.add(instructorName);
        instructorCourseTablePanel.add(scrollPane, BorderLayout.CENTER);



        backBtn4 = new JButton("Back");
        assignCourse = new JButton("Assign New");
        mainMenuBtn = new JButton("Main Menu");
        deleteInstructorSection = new JButton("Delete");
        deleteInstructorSection.setBackground(Color.RED);
        backBtn4.addActionListener(instructorHandler);
        assignCourse.addActionListener(instructorHandler);
        mainMenuBtn.addActionListener(instructorHandler);

        deleteSectionHandler deleteSectionHandler2 = new deleteSectionHandler();
        deleteInstructorSection.addActionListener(deleteSectionHandler2);

        instructorCourseTablePanel.add(backBtn4);
        instructorCourseTablePanel.add(assignCourse);
        instructorCourseTablePanel.add(deleteInstructorSection);
        instructorCourseTablePanel.add(mainMenuBtn);

        instructorCourseTablePanel.setBounds(69, 135, 675, 270);
        instructorCourseTablePanel.setBackground(Color.WHITE);
        menuFrame.add(instructorCourseTablePanel);

    }

    public void instructorAssignCourse(String Department) {
        Connection con = UniversityData.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            System.out.println(Department);
            String sql = "SELECT Course FROM courseMain WHERE Department IS " + Department;

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            departmentCourses2 = new ArrayList<String >();
            while (rs.next()) {
                departmentCourses2.add(rs.getString(1));
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            try {
                con.close();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        backBtn5 = new JButton("Back");
        submitBtn4 = new JButton("Next");

        backBtn5.addActionListener(instructorHandler);
        submitBtn4.addActionListener(instructorHandler);

        backBtn5.setFocusable(false);
        submitBtn4.setFocusable(false);

        selectCoursePanel2 = new JPanel();
        selectCoursePanel2.setBounds(250, 135, 300, 300);
        selectCoursePanel2.setBackground(Color.white);
        courseList2 = new JComboBox();
        courseList2.setPreferredSize(new Dimension(300, 30));
        JLabel tempLabel = new JLabel("Select a Course");
        tempLabel.setFont(new Font("Arial", Font.BOLD, 15));
        for (int i = 0; i < departmentCourses2.size(); i++)
            courseList2.addItem(departmentCourses2.get(i));
        selectCoursePanel2.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 35));
        selectCoursePanel2.add(tempLabel);
        selectCoursePanel2.add(courseList2);
        selectCoursePanel2.add(backBtn5);
        selectCoursePanel2.add(submitBtn4);
        selectCoursePanel2.setVisible(true);
        menuFrame.add(selectCoursePanel2);
        courseList2.addItemListener(itemListenerCourse2);
    }

    public void instructorAssignSection(String Course) {
        Connection con = UniversityData.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        courseTableModel2 = new DefaultTableModel(courseData, courseColumns2);
        if (selectedCourse2 == null)
            Course = "\'"+(String) courseList2.getItemAt(0)+"\'";
        try {
            System.out.println("Course is "+Course);
            String sql = "SELECT CRN, Course, Section, Days, Time, Room, Credit FROM courses WHERE Course IS " + Course +
                    " AND Instructor IS 'TBA'";

            Object[] tempRow;

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                tempRow = new Object[]{rs.getInt(1),rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getString(6), rs.getInt(7)};
                System.out.println("Row Added");
                courseTableModel2.addRow(tempRow);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            try {
                rs.close();
                ps.close();
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        coursesTable2 = new JTable(courseTableModel2) {
            public boolean isCellEditable(int data, int columns) {
                if (edit)
                    return false;
                return true;
            }
        };
        coursesTable2.setAutoCreateColumnsFromModel(true);
        JScrollPane scrollPane = new JScrollPane(coursesTable2);
        coursesTable2.setPreferredScrollableViewportSize(new Dimension(650, 80));
        coursesTable2.setFillsViewportHeight(true);
        coursesTable2.setDragEnabled(false);

        TableColumnModel columnModel = coursesTable2.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(17);
        columnModel.getColumn(1).setPreferredWidth(25);
        columnModel.getColumn(2).setPreferredWidth(17);
        columnModel.getColumn(3).setPreferredWidth(17);
        columnModel.getColumn(6).setPreferredWidth(17);

        instructorAssignSectionPanel = new JPanel();

        JLabel courseName = new JLabel(Course);
        courseName.setFont(new Font("Arial", Font.BOLD, 22));
        instructorAssignSectionPanel.add(courseName);
        instructorAssignSectionPanel.add(scrollPane, BorderLayout.CENTER);



        backBtn6 = new JButton("Back");
        assignSection = new JButton("Assign");
        mainMenuBtn = new JButton("Main Menu");
        backBtn6.addActionListener(instructorHandler);
        assignSection.addActionListener(instructorHandler);
        mainMenuBtn.addActionListener(instructorHandler);



        instructorAssignSectionPanel.add(backBtn6);
        instructorAssignSectionPanel.add(assignSection);
        instructorAssignSectionPanel.add(mainMenuBtn);

        instructorAssignSectionPanel.setBounds(49, 180, 670, 270);
        instructorAssignSectionPanel.setBackground(Color.WHITE);
        menuFrame.add(instructorAssignSectionPanel);


    }

    //STUDENT PAGES
    public void departmentSelectionStudent() {
        backBtn = new JButton("Back");
        submitBtn = new JButton("Submit");
        backBtn.addActionListener(studentHandler);
        backBtn.setFocusable(false);
        submitBtn.setFocusable(false);
        submitBtn.addActionListener(studentHandler);
        studentPanel = new JPanel();
        studentPanel.setBounds(250, 135, 300, 300);
        studentPanel.setBackground(Color.white);
        departmentList = new JComboBox();
        departmentList.setPreferredSize(new Dimension(300, 30));
        JLabel templabel = new JLabel("Please select a department");
        templabel.setFont(new Font("Arial", Font.BOLD, 15));
        int count = 0;
        for (int i = 0; i < departments.length; i++)
            departmentList.addItem(departments[count++]);
        studentPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 35));
        studentPanel.add(templabel);
        studentPanel.add(departmentList);
        studentPanel.add(backBtn);
        studentPanel.add(submitBtn);
        menuFrame.add(studentPanel);


    }

    public void selectStudent(String department) {
        Connection con = UniversityData.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        studentListTableModel = new DefaultTableModel(studentData, studentList) {
            public Class getColumnClass(int column) {
                Class returnValue;

                if ((column >= 0) && (column < getColumnCount())) {
                    returnValue = getValueAt(0, column).getClass();
                } else {
                    returnValue = Object.class;
                }
                return returnValue;
            }
        };
        try {
            String sql = "SELECT Name, ID FROM users WHERE Department IS " + department + " AND Job IS 'Student'";

            Object[] tempRow;

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                tempRow = new Object[]{rs.getString(1),rs.getString(2)};
                System.out.println("Row Added");
                studentListTableModel.addRow(tempRow);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            try {
                rs.close();
                ps.close();
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        studentListTable = new JTable(studentListTableModel) {
            public boolean isCellEditable(int data, int columns) {
                if (edit)
                    return false;
                return true;
            }
        };
        studentListTable.setAutoCreateColumnsFromModel(true);
        JScrollPane scrollPane = new JScrollPane(studentListTable);
        studentListTable.setPreferredScrollableViewportSize(new Dimension(600, 80));
        studentListTable.setFillsViewportHeight(true);
        studentListTable.setDragEnabled(false);

        studentTablePanel = new JPanel();
        studentTablePanel.add(scrollPane, BorderLayout.CENTER);

        backBtn2 = new JButton("Back");
        viewScheduleBtn = new JButton("View Schedule");
        deleteStudentBtn = new JButton("Delete Student");
        addStudentBtn = new JButton("Add Student");

        deleteStudentBtn.setBackground(Color.RED);

        backBtn2.addActionListener(studentHandler);
        viewScheduleBtn.addActionListener(studentHandler);
        deleteStudentBtn.addActionListener(studentHandler);
        addStudentBtn.addActionListener(studentHandler);

        deleteSectionHandler deleteSectionHandler = new deleteSectionHandler();
        deleteStudentBtn.addActionListener(deleteSectionHandler);

        studentTablePanel.add(backBtn2);
        studentTablePanel.add(viewScheduleBtn);
        studentTablePanel.add(deleteStudentBtn);
        studentTablePanel.add(addStudentBtn);

        studentTablePanel.setBounds(69, 170, 660, 270);
        studentTablePanel.setBackground(Color.WHITE);
        menuFrame.add(studentTablePanel);
    }

    public void addStudent(String department) {
        addStudentPanel = new JPanel();
        addStudentPanel.setBackground(Color.WHITE);
        addStudentPanel.setLayout(new GridLayout(3,4));
        addStudentPanel.setBounds(70, 220, 500, 80);

        idTxt = new JTextField();
        userNameTxt = new JTextField();
        passwordTxt = new JTextField();
        jobTxt = new JTextField("Student");
        userDepartmentTxt = new JTextField(department);
        submitBtn3 = new JButton("Submit");
        backBtn3 = new JButton("Back");

        addStudentPanel.add(new JLabel("ID:"));
        addStudentPanel.add(idTxt);
        addStudentPanel.add(new JLabel("Name:"));
        addStudentPanel.add(userNameTxt);
        addStudentPanel.add(new JLabel("Password:"));
        addStudentPanel.add(passwordTxt);
        addStudentPanel.add(new JLabel(""));
        addStudentPanel.add(new JLabel(""));

        addStudentPanel.add(new JLabel(""));
        addStudentPanel.add(backBtn3);
        addStudentPanel.add(submitBtn3);

        submitBtn3.addActionListener(studentHandler);
        backBtn3.addActionListener(studentHandler);
        menuFrame.add(addStudentPanel);
    }

    public void viewStudentSchedule(String ID) {
        Connection con = UniversityData.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        studentCourseTableModel = new DefaultTableModel(courseData, courseColumns) {
            public Class getColumnClass(int column) {
                Class returnValue;

                if ((column >= 0) && (column < getColumnCount())) {
                    returnValue = getValueAt(0, column).getClass();
                } else {
                    returnValue = Object.class;
                }
                return returnValue;
            }
        };
        try {
            String sql = "SELECT * FROM studentCourses WHERE ID IS " + ID;
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                studentCourses[0] = rs.getInt("Course1");
                studentCourses[1] = rs.getInt("Course2");
                studentCourses[2] = rs.getInt("Course3");
                studentCourses[3] = rs.getInt("Course4");
                studentCourses[4] = rs.getInt("Course5");
                studentCourses[5] = rs.getInt("Course6");
                studentCourses[6] = rs.getInt("Course7");
            }
            Object[] tempRow;

            for (int i = 0; i<7;i++) {
                sql = "SELECT CRN, Course, Section, Days, Time, Room, Instructor, Credit FROM courses WHERE" +
                        " CRN IS " + studentCourses[i];
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    tempRow = new Object[]{rs.getInt(1),rs.getString(2), rs.getString(3), rs.getString(4),
                            rs.getString(5), rs.getString(6), rs.getString(7), rs.getInt(8)};
                    System.out.println("Row Added");
                    studentCourseTableModel.addRow(tempRow);
                }
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            try {
                rs.close();
                ps.close();
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        studentCourseTable = new JTable(studentCourseTableModel) {
            public boolean isCellEditable(int data, int columns) {
                if (edit)
                    return false;
                return true;
            }
        };
        studentCourseTable.setAutoCreateColumnsFromModel(true);
        JScrollPane scrollPane = new JScrollPane(studentCourseTable);
        studentCourseTable.setPreferredScrollableViewportSize(new Dimension(650, 80));
        studentCourseTable.setFillsViewportHeight(true);
        studentCourseTable.setDragEnabled(false);

        TableColumnModel columnModel = studentCourseTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(17);
        columnModel.getColumn(1).setPreferredWidth(25);
        columnModel.getColumn(2).setPreferredWidth(17);
        columnModel.getColumn(3).setPreferredWidth(17);
        columnModel.getColumn(7).setPreferredWidth(17);

        studentCoursesPanel = new JPanel();

        JLabel studentName = new JLabel();
        studentName.setText((String) studentListTable.getModel().getValueAt(studentListTable.getSelectedRow(),0)
                +" - " + (String) studentListTable.getModel().getValueAt(studentListTable.getSelectedRow(),1) );
        studentName.setFont(new Font("Arial", Font.BOLD, 22));
        studentCoursesPanel.add(studentName);
        studentCoursesPanel.add(scrollPane, BorderLayout.CENTER);

        deleteSectionHandler deleteSectionHandler = new deleteSectionHandler();

        backBtn4 = new JButton("Back");
        mainMenuBtn2 = new JButton("Main Menu");
        deleteStudentCourse = new JButton("Delete");
        addStudentCourseBtn = new JButton("Add");
        deleteStudentCourse.setBackground(Color.RED);
        backBtn4.addActionListener(studentHandler);
        mainMenuBtn2.addActionListener(studentHandler);
        deleteStudentCourse.addActionListener(deleteSectionHandler);
        addStudentCourseBtn.addActionListener(studentHandler);

        studentCoursesPanel.add(backBtn4);
        studentCoursesPanel.add(deleteStudentCourse);
        studentCoursesPanel.add(addStudentCourseBtn);
        studentCoursesPanel.add(mainMenuBtn2);

        selectedStudentID = (String) studentListTable.getModel().getValueAt(studentListTable.getSelectedRow(),1);
        studentCoursesPanel.setBounds(69, 135, 670, 270);
        studentCoursesPanel.setBackground(Color.WHITE);
        menuFrame.add(studentCoursesPanel);
    }

    public void departmentCourses() {
        backBtn5 = new JButton("Back");
        submitBtn2 = new JButton("Submit");
        backBtn5.addActionListener(studentHandler);
        backBtn5.setFocusable(false);
        submitBtn2.setFocusable(false);
        submitBtn2.addActionListener(studentHandler);
        departmentCoursePanel = new JPanel();
        departmentCoursePanel.setBounds(250, 135, 300, 300);
        departmentCoursePanel.setBackground(Color.white);
        departmentList = new JComboBox();
        departmentList.setPreferredSize(new Dimension(300, 30));
        JLabel templabel = new JLabel("Please select a department");
        templabel.setFont(new Font("Arial", Font.BOLD, 15));
        int count = 0;
        for (int i = 0; i < departments.length; i++)
            departmentList.addItem(departments[count++]);
        departmentCoursePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 35));
        departmentCoursePanel.add(templabel);
        departmentCoursePanel.add(departmentList);
        departmentCoursePanel.add(backBtn5);
        departmentCoursePanel.add(submitBtn2);
        menuFrame.add(departmentCoursePanel);


    }

    public void studentAssignCourse(String Department) {
        Connection con = UniversityData.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            System.out.println(Department);
            String sql = "SELECT Course FROM courseMain WHERE Department IS " + Department;

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            departmentCourses2 = new ArrayList<String >();
            while (rs.next()) {
                departmentCourses2.add(rs.getString(1));
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            try {
                con.close();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        backBtn6 = new JButton("Back");
        submitBtn4 = new JButton("Next");

        backBtn6.addActionListener(studentHandler);
        submitBtn4.addActionListener(studentHandler);

        backBtn6.setFocusable(false);
        submitBtn4.setFocusable(false);

        selectCoursePanel3 = new JPanel();
        selectCoursePanel3.setBounds(250, 135, 300, 300);
        selectCoursePanel3.setBackground(Color.white);
        courseList3 = new JComboBox();
        courseList3.setPreferredSize(new Dimension(300, 30));
        JLabel tempLabel = new JLabel("Select a Course");
        tempLabel.setFont(new Font("Arial", Font.BOLD, 15));
        for (int i = 0; i < departmentCourses2.size(); i++)
            courseList3.addItem(departmentCourses2.get(i));
        selectCoursePanel3.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 35));
        selectCoursePanel3.add(tempLabel);
        selectCoursePanel3.add(courseList3);
        selectCoursePanel3.add(backBtn6);
        selectCoursePanel3.add(submitBtn4);
        selectCoursePanel3.setVisible(true);
        menuFrame.add(selectCoursePanel3);
        courseList3.addItemListener(itemListenerCourse2);
    }

    public void studentAssignSection(String Course) {
        Connection con = UniversityData.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        courseTableModel3 = new DefaultTableModel(courseData, courseColumns2);
        if (selectedCourse2 == null)
            Course = "\'"+(String) courseList3.getItemAt(0)+"\'";
        try {
            System.out.println("Course is "+Course);
            String sql = "SELECT CRN, Course, Section, Days, Time, Room, Credit FROM courses WHERE Course IS " + Course;

            Object[] tempRow;

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                tempRow = new Object[]{rs.getInt(1),rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getString(6), rs.getInt(7)};
                System.out.println("Row Added");
                courseTableModel3.addRow(tempRow);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            try {
                rs.close();
                ps.close();
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        coursesTable3 = new JTable(courseTableModel3) {
            public boolean isCellEditable(int data, int columns) {
                if (edit)
                    return false;
                return true;
            }
        };
        coursesTable3.setAutoCreateColumnsFromModel(true);
        JScrollPane scrollPane = new JScrollPane(coursesTable3);
        coursesTable3.setPreferredScrollableViewportSize(new Dimension(650, 80));
        coursesTable3.setFillsViewportHeight(true);
        coursesTable3.setDragEnabled(false);

        TableColumnModel columnModel = coursesTable3.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(17);
        columnModel.getColumn(1).setPreferredWidth(25);
        columnModel.getColumn(2).setPreferredWidth(17);
        columnModel.getColumn(3).setPreferredWidth(17);
        columnModel.getColumn(6).setPreferredWidth(17);

        studentAssignSectionPanel = new JPanel();

        JLabel courseName = new JLabel(Course);
        courseName.setFont(new Font("Arial", Font.BOLD, 22));
        studentAssignSectionPanel.add(courseName);
        studentAssignSectionPanel.add(scrollPane, BorderLayout.CENTER);

        backBtn7 = new JButton("Back");
        assignStudentSectionBtn = new JButton("Assign");
        mainMenuBtn3 = new JButton("Main Menu");
        backBtn7.addActionListener(studentHandler);
        assignStudentSectionBtn.addActionListener(studentHandler);
        mainMenuBtn3.addActionListener(studentHandler);



        studentAssignSectionPanel.add(backBtn7);
        studentAssignSectionPanel.add(assignStudentSectionBtn);
        studentAssignSectionPanel.add(mainMenuBtn3);

        studentAssignSectionPanel.setBounds(49, 180, 670, 270);
        studentAssignSectionPanel.setBackground(Color.WHITE);
        menuFrame.add(studentAssignSectionPanel);


    }

    private class deleteCourseHandler implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource() == deleteCourse)
            {
                int reply = JOptionPane.showConfirmDialog(null, "Are You sure you want to " +
                        "delete the course", "Remove Course", JOptionPane.YES_NO_OPTION);
                Connection con = UniversityData.connect();
                PreparedStatement ps = null;
                    try {
                        if (reply == JOptionPane.YES_OPTION){
                            System.out.println("Course Deleted");
                            System.out.println("\'"+ courseList.getSelectedItem()+"\'");
                            selectedCourse = "\'"+ courseList.getSelectedItem()+"\'";
                            String sql = "DELETE FROM courses WHERE Course IS " + selectedCourse;
                            ps = con.prepareStatement(sql);
                            ps.executeUpdate();
                            sql = "DELETE FROM courseMain WHERE Course IS " + selectedCourse;
                            ps = con.prepareStatement(sql);
                            ps.executeUpdate();
                            selectCoursePanel.setVisible(false);
                            coursePanel.setVisible(true);
                            coursePanel.setVisible(false);
                            if (departmentList.getSelectedItem().equals("Electrical Engineer")) {
                                selectCourse("\'Electrical Engineering\'");
                            } else if (departmentList.getSelectedItem().equals("Computer Science")) {
                                selectCourse("\'Computer Science\'");
                            } else if (departmentList.getSelectedItem().equals("Mechanical Engineer")) {
                                selectCourse("\'Mechanical Engineering\'");
                            }

                        } else if (reply == JOptionPane.NO_OPTION) {
                        //nothing will happen
                    }
                    } catch (SQLException e1) {
                        System.out.println(e1.toString());
                    } finally {
                        try {
                            con.close();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
            }
        }

    }

    private class deleteInstructorHandler implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource() == deleteInstructor)
            {
                int reply = JOptionPane.showConfirmDialog(null, "Are You sure you want to " +
                        "delete the instructor", "Remove instructor", JOptionPane.YES_NO_OPTION);
                Connection con = UniversityData.connect();
                PreparedStatement ps = null;
                try {
                    if (reply == JOptionPane.YES_OPTION){
                        System.out.println("instructor Deleted");
                        if (instructorList.getSelectedItem().equals(departmentInstructor.get(0)));
                        selectedInstructor = "\'"+ departmentInstructor.get(0)+"\'";
                        System.out.println(selectedInstructor);
                        String sql = "DELETE FROM users WHERE Name IS " + selectedInstructor;
                        ps = con.prepareStatement(sql);
                        ps.executeUpdate();
                        sql = "DELETE FROM instructorCourses WHERE Name IS " + selectedInstructor;
                        ps = con.prepareStatement(sql);
                        ps.executeUpdate();
                        selectInstructorPanel.setVisible(false);
                        instructorPanel.setVisible(true);
                        instructorPanel.setVisible(false);
                        selectInstructor("\'Electrical Engineering\'");

                    } else if (reply == JOptionPane.NO_OPTION) {
                        //nothing will happen
                    }
                } catch (SQLException e1) {
                    System.out.println(e1.toString());
                } finally {
                    try {
                        ps.close();
                        con.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

    }

    private class deleteSectionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == deleteSection) {
                System.out.println("DELETE SECTION");
                System.out.println("SELECTED ROW " + coursesTable.getSelectedRows());
                    System.out.println(coursesTable.getModel().getValueAt(coursesTable.getSelectedRow(), 0));
                    Connection con = UniversityData.connect();
                    PreparedStatement ps = null;
                    try
                    {
                        String sql = "DELETE FROM courses WHERE CRN = " + coursesTable.getModel().getValueAt(coursesTable.getSelectedRow(), 0);
                        ps = con.prepareStatement(sql);
                        ps.executeUpdate();
                        courseTableModel.removeRow(coursesTable.getSelectedRow());
                        JOptionPane.showMessageDialog(null, "Deleted successfully");
                    }
                    catch (SQLException wew)
                    {
                        System.out.println(wew.toString());
                    }
                    finally
                    {
                        try
                        {
                            ps.close();
                            con.close();
                        }
                        catch (SQLException throwables)
                        {
                            throwables.printStackTrace();
                        }
                    }


            }
            else if (e.getSource() == deleteInstructorSection)
            {
                System.out.println(instructorCoursesTable.getModel().getValueAt(instructorCoursesTable.getSelectedRow(), 0));
                Connection con = UniversityData.connect();
                PreparedStatement ps = null;
                try
                {
                    String sql = "UPDATE courses SET Instructor = ? WHERE CRN = ?";

                    ps = con.prepareStatement(sql);
                    ps.setString(1, "TBA");
                    ps.setInt(2, (Integer) instructorCoursesTable.getModel().getValueAt(instructorCoursesTable.getSelectedRow(), 0));
                    ps.executeUpdate();
                    InstructorCourseTableModel.removeRow(instructorCoursesTable.getSelectedRow());
                    JOptionPane.showMessageDialog(null, "Deleted successfully");
                }
                catch (SQLException wew)
                {
                    System.out.println(wew.toString());
                }
                finally
                {
                    try
                    {
                        con.close();
                    }
                    catch (SQLException throwables)
                    {
                        throwables.printStackTrace();
                    }
                }
            }
            else if (e.getSource() == deleteStudentCourse)
            {
                Connection con = UniversityData.connect();
                PreparedStatement ps = null;
                try {
                    for (int i = 1; i<=7;i++) {
                        String sql = "UPDATE studentCourses SET Course"+i +" = ? WHERE ID IS " + selectedStudentID +
                                " AND Course"+i+" IS " + studentCourseTable.getValueAt(studentCourseTable.getSelectedRow(),0);

                        ps = con.prepareStatement(sql);
                        ps.setInt(1,0);
                        ps.executeUpdate();
                    }

                    studentCourseTableModel.removeRow(studentCourseTable.getSelectedRow());
                    JOptionPane.showMessageDialog(null, "Deleted successfully");

                } catch (SQLException e4) {
                    System.out.println(e4.toString());
                } finally {
                    try {
                        ps.close();
                        con.close();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }

            }
        }
    }

    private class logoutHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == logout) {
                menuFrame.dispose();
                new LoginPage();
            }
        }
    }

    private class selectHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == courses) {
                //LOAD COURSES
                mainMenuPanel.setVisible(false);
                departmentSelectionCourse();
            } else if (e.getSource() == instructors) {
                //LOAD INSTRUCTORS
                mainMenuPanel.setVisible(false);
                departmentSelectionInstructor();
            } else if (e.getSource() == students) {
                mainMenuPanel.setVisible(false);
                departmentSelectionStudent();
            }

        }
    }

    private class actionHandlerCourse implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == submitBtn) {
                if (departmentList.getSelectedItem().equals("Electrical Engineer")) {
                    coursePanel.setVisible(false);
                    selectCourse("\'Electrical Engineering\'");
                } else if (departmentList.getSelectedItem().equals("Computer Science")) {
                    coursePanel.setVisible(false);
                    selectCourse("\'Computer Science\'");
                } else if (departmentList.getSelectedItem().equals("Mechanical Engineer")) {
                    coursePanel.setVisible(false);
                    selectCourse("\'Mechanical Engineering\'");
                }
            }
            else if (e.getSource() == submitBtn2)
            {
                if (courseList.getSelectedItem().equals(("Add Course")))
                {
                    if (departmentList.getSelectedItem().equals("Electrical Engineer"))
                    {
                        System.out.println((String) departmentList.getSelectedItem());
                        selectCoursePanel.setVisible(false);
                        addCourse("Electrical Engineering");

                    }
                    else if (departmentList.getSelectedItem().equals("Computer Science"))
                    {
                        System.out.println((String) departmentList.getSelectedItem());
                        selectCoursePanel.setVisible(false);
                        addCourse("Computer Science");

                    }
                    else if (departmentList.getSelectedItem().equals("Mechanical Engineer"))
                    {
                        System.out.println((String) departmentList.getSelectedItem());
                        selectCoursePanel.setVisible(false);
                        addCourse("Mechanical Engineering");

                    }
                }
                else
                {
                if (selectedCourse == null)
                    selectedCourse = "\'"+ departmentCourses.get(0)+"\'";

                selectedCourse = "\'"+(String) courseList.getSelectedItem()+"\'";
                System.out.println(selectedCourse);
                selectCoursePanel.setVisible(false);
                courseTable(selectedCourse);
                }

            }
            else if (e.getSource() == submitBtn3)
            {
                boolean insertCondition = true;
                if (crnTxt.getText().equals(""))
                    insertCondition = false;
                if (courseTxt.getText().equals(""))
                    insertCondition = false;
                if (sectionTxt.getText().equals(""))
                    insertCondition = false;
                if (nameTxt.getText().equals(""))
                    insertCondition = false;
                if (maxStuTxt.getText().equals(""))
                    insertCondition = false;
                if (daysTxt.getText().equals(""))
                    insertCondition = false;
                if (timeTxt.getText().equals(""))
                    insertCondition = false;
                if (roomTxt.getText().equals(""))
                    insertCondition = false;
                if (instructors.getText().equals(""))
                    insertCondition = false;
                if (creditTxt.getText().equals(""))
                    insertCondition = false;
                if (departmentTxt.getText().equals(""))
                    insertCondition = false;
                if (startDateTxt.getText().equals(""))
                    insertCondition = false;
                if (endDateTxt.getText().equals(""))
                    insertCondition = false;

                if (insertCondition)
                {
                    insertCourse(Integer.parseInt(crnTxt.getText()), courseTxt.getText(), sectionTxt.getText(), nameTxt.getText(),
                            Integer.parseInt(maxStuTxt.getText()), daysTxt.getText(), timeTxt.getText(), roomTxt.getText(), instructorTxt.getText(),
                            Integer.parseInt(creditTxt.getText()), departmentTxt.getText(), startDateTxt.getText(), endDateTxt.getText());
                    addCoursePanel.setVisible(false);
                    courseTable("\'"+courseTxt.getText()+"\'");
                }
            }
            else if (e.getSource() == updateBtn)
            {

                Connection con = UniversityData.connect();
                PreparedStatement ps = null;
                try {
                    String sql = "UPDATE courses SET Section = ?, Days = ?, Time = ?, Room = ?," +
                            " Instructor = ?, Credit = ? WHERE CRN = ?";
                    ps = con.prepareStatement(sql);

                    for (int row=0; row<coursesTable.getRowCount(); row++)
                    {
                        int column =2;
                        ps.setString(1, (String) coursesTable.getValueAt(row,column));
                        column++;
                        ps.setString(2, (String) coursesTable.getValueAt(row,column));
                        column++;
                        ps.setString(3, (String) coursesTable.getValueAt(row,column));
                        column++;
                        ps.setString(4, (String) coursesTable.getValueAt(row,column));
                        column++;
                        ps.setString(5, (String) coursesTable.getValueAt(row,column));
                        column++;
                        ps.setInt(6, (Integer) coursesTable.getValueAt(row,column));
                        ps.setInt(7, (Integer) coursesTable.getValueAt(row,0));
                        ps.executeUpdate();
                    }

                } catch (SQLException dang) {
                    System.out.println(dang.toString());
                } finally {
                    try {
                        ps.close();
                        con.close();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
            else if (e.getSource() == backBtn)
            {
                coursePanel.setVisible(false);
                mainMenuPanel.setVisible(true);
            }
            else if (e.getSource() == backBtn2)
            {
                selectCoursePanel.setVisible(false);
                coursePanel.setVisible(true);
            }
            else if (e.getSource() == backBtn3)
            {
                addCoursePanel.setVisible(false);
                selectCoursePanel.setVisible(true);
            }
            else if (e.getSource() == backBtn4)
            {
                courseTablePanel.setVisible(false);
                selectCoursePanel.setVisible(false);
                menuFrame.remove(selectCoursePanel);
                if (departmentList.getSelectedItem().equals("Electrical Engineer"))
                {
                    selectCourse("\'Electrical Engineering\'");
                }
                else if (departmentList.getSelectedItem().equals("Computer Science"))
                {
                    selectCourse("\'Computer Science\'");
                }
                else if (departmentList.getSelectedItem().equals("Mechanical Engineer"))
                {
                    selectCourse("\'Mechanical Engineering\'");
                }
                edit = true;
            }
            else if (e.getSource() == mainMenuBtn)
            {
                mainMenuPanel.setVisible(true);
                courseTablePanel.setVisible(false);
            }
            else if (e.getSource() == editBtn)
            {
                if (count == 1)
                {
                    edit = true;
                    count = 0;
                }
                else if (count == 0)
                {
                    edit = false;
                    count = 1;
                }

            }
        }
    }

    private class actionHandlerInstructor implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == submitBtn) {
                if (departmentList.getSelectedItem().equals("Electrical Engineer")) {
                    instructorPanel.setVisible(false);
                    selectInstructor("\'Electrical Engineering\'");
                } else if (departmentList.getSelectedItem().equals("Computer Science")) {
                    instructorPanel.setVisible(false);
                    selectInstructor("\'Computer Science\'");
                } else if (departmentList.getSelectedItem().equals("Mechanical Engineer")) {
                    instructorPanel.setVisible(false);
                    selectInstructor("\'Mechanical Engineering\'");
                }
            }
            else if (e.getSource() == submitBtn2)
            {
                System.out.println(instructorList.getSelectedItem());
                if (instructorList.getSelectedItem().equals(("Add Instructor")))
                {
                    if (departmentList.getSelectedItem().equals("Electrical Engineer"))
                    {
                        System.out.println((String) departmentList.getSelectedItem());
                        selectInstructorPanel.setVisible(false);
                        addInstructor("Electrical Engineering");

                    }
                    else if (departmentList.getSelectedItem().equals("Computer Science"))
                    {
                        System.out.println((String) departmentList.getSelectedItem());
                        selectInstructorPanel.setVisible(false);
                        addInstructor("Computer Science");

                    }
                    else if (departmentList.getSelectedItem().equals("Mechanical Engineer"))
                    {
                        System.out.println((String) departmentList.getSelectedItem());
                        selectInstructorPanel.setVisible(false);
                        addInstructor("Mechanical Engineering");
                    }
                }
                else
                {
                    if (selectedInstructor == null)
                        selectedInstructor = "\'"+ departmentInstructor.get(0)+"\'";
                    else
                        selectedInstructor = "\'"+(String) instructorList.getSelectedItem()+"\'";

                    System.out.println(selectedInstructor);
                    selectInstructorPanel.setVisible(false);
                    instructorCourseTable(selectedInstructor);
                }

            }
            else if (e.getSource() == submitBtn3)
            {
                boolean insertCondition = true;

                if (idTxt.getText().equals(""))
                    insertCondition = false;
                if (userNameTxt.getText().equals(""))
                    insertCondition = false;
                if (passwordTxt.getText().equals(""))
                    insertCondition = false;

                if (insertCondition)
                {
                    insertUser(Integer.parseInt(idTxt.getText()),userNameTxt.getText(),passwordTxt.getText(),
                            jobTxt.getText(),userDepartmentTxt.getText());

                    if (departmentList.getSelectedItem().equals("Electrical Engineer")) {
                        selectInstructor("\'Electrical Engineering\'");
                    } else if (departmentList.getSelectedItem().equals("Computer Science")) {
                        selectInstructor("\'Computer Science\'");
                    } else if (departmentList.getSelectedItem().equals("Mechanical Engineer")) {
                        selectInstructor("\'Mechanical Engineering\'");
                    }

                    addInstructorPanel.setVisible(false);
                } else {
                    System.out.println("fill the text boxes");
                    JOptionPane.showMessageDialog(null, "fill the text boxes");
                }
            }
            else if (e.getSource() == backBtn)
            {
                instructorPanel.setVisible(false);
                mainMenuPanel.setVisible(true);
            }
            else if (e.getSource() == backBtn2)
            {
                selectInstructorPanel.setVisible(false);
                instructorPanel.setVisible(true);
            }
            else if (e.getSource() == backBtn3)
            {
                addInstructorPanel.setVisible(false);
                selectInstructorPanel.setVisible(true);
            }
            else if (e.getSource() == backBtn4)
            {
                if (instructorCoursesTable.getModel().getRowCount() >= 1)
                {
                    instructorCourseTablePanel.setVisible(false);
                    selectInstructorPanel.setVisible(false);
                    menuFrame.remove(selectInstructorPanel);

                    if (departmentList.getSelectedItem().equals("Electrical Engineer")) {
                        selectedCourse2 = null;
                        selectInstructor("\'Electrical Engineering\'");
                    } else if (departmentList.getSelectedItem().equals("Computer Science")) {
                        selectedCourse2 = null;
                        selectInstructor("\'Computer Science\'");
                    } else if (departmentList.getSelectedItem().equals("Mechanical Engineer")) {
                        selectedCourse2 = null;
                        selectInstructor("\'Mechanical Engineering\'");
                    }
                }
                else {
                    instructorCourseTablePanel.setVisible(false);
                    selectInstructorPanel.setVisible(true);
                }
            }
            else if (e.getSource() == backBtn5)
            {
                selectCoursePanel2.setVisible(false);
                instructorCourseTable(selectedInstructor);
            }
            else if (e.getSource() == mainMenuBtn)
            {
                mainMenuPanel.setVisible(true);
                instructorCourseTablePanel.setVisible(false);
            }
            else if (e.getSource() == assignCourse)
            {
                if (departmentList.getSelectedItem().equals("Electrical Engineer")) {
                    instructorAssignCourse("\'Electrical Engineering\'");
                } else if (departmentList.getSelectedItem().equals("Computer Science")) {
                    instructorAssignCourse("\'Computer Science\'");
                } else if (departmentList.getSelectedItem().equals("Mechanical Engineer")) {
                    instructorAssignCourse("\'Mechanical Engineering\'");
                }
                instructorCourseTablePanel.setVisible(false);
            }
            else if (e.getSource() == submitBtn4)
            {
                selectCoursePanel2.setVisible(false);
                instructorAssignSection(selectedCourse2);
            }
            else if (e.getSource() == backBtn6)
            {
                instructorAssignSectionPanel.setVisible(false);
                selectCoursePanel2.setVisible(true);
            } else if (e.getSource() == assignSection) {
                Connection con = UniversityData.connect();
                PreparedStatement ps = null;
                ResultSet rs = null;
                int Credit = 0;
                int addedCredit = 0;
                try {
                    System.out.println("Instructor Selected: " + selectedInstructor);
                    String sql = "SELECT Credit FROM courses WHERE Instructor IS " + selectedInstructor;
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        Credit += rs.getInt("Credit");
                    }
                    System.out.println("Instructor Course Credit: " + Credit);

                    System.out.println(coursesTable2.getModel().getValueAt(coursesTable2.getSelectedRow(), 0));

                    sql = "SELECT Credit FROM courses WHERE CRN = " + coursesTable2.getModel().getValueAt(coursesTable2.getSelectedRow(), 0);
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        addedCredit += rs.getInt("Credit");
                    }
                    if (Credit+addedCredit <= 12) {
                        sql = "UPDATE courses SET Instructor = " + selectedInstructor+ " WHERE CRN = " +coursesTable2.getModel().getValueAt(coursesTable2.getSelectedRow(), 0);
                        ps = con.prepareStatement(sql);
                        System.out.println("pass");
                        ps.executeUpdate();
                        courseTableModel2.removeRow(coursesTable2.getSelectedRow());
                        JOptionPane.showMessageDialog(null, "Added successfully");
                    } else {
                        JOptionPane.showMessageDialog(null, "Cannot Assign Course instructor added credit is " + ((int) Credit+ (int) addedCredit));
                    }

                } catch (SQLException e2) {
                    System.out.println(e2.toString());
                } finally {
                    try {
                        ps.close();
                        rs.close();
                        con.close();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }

        }
    }

    private class actionHandlerStudent implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == submitBtn) {
                if (departmentList.getSelectedItem().equals("Electrical Engineer")) {
                    studentPanel.setVisible(false);
                    selectStudent("\'Electrical Engineering\'");
                } else if (departmentList.getSelectedItem().equals("Computer Science")) {
                    studentPanel.setVisible(false);
                    selectStudent("\'Computer Science\'");
                } else if (departmentList.getSelectedItem().equals("Mechanical Engineer")) {
                    studentPanel.setVisible(false);
                    selectStudent("\'Mechanical Engineering\'");
                }
            }
            else if (e.getSource() == submitBtn2)
            {
                if (departmentList.getSelectedItem() == "Electrical Engineer")
                {
                    studentAssignCourse("\'Electrical Engineering\'");
                    departmentCoursePanel.setVisible(false);
                }
                else if (departmentList.getSelectedItem() == "Computer Science")
                {
                    studentAssignCourse("\'Computer Science\'");
                    departmentCoursePanel.setVisible(false);
                }
                else if (departmentList.getSelectedItem() == "Mechanical Engineer")
                {
                    studentAssignCourse("\'Mechanical Engineering\'");
                    departmentCoursePanel.setVisible(false);
                }

            }
            else if (e.getSource() == submitBtn3)
            {
                boolean insertCondition = true;

                if (idTxt.getText().equals(""))
                    insertCondition = false;
                if (userNameTxt.getText().equals(""))
                    insertCondition = false;
                if (passwordTxt.getText().equals(""))
                    insertCondition = false;

                if (insertCondition)
                {
                    insertUser(Integer.parseInt(idTxt.getText()),userNameTxt.getText(),passwordTxt.getText(),
                            jobTxt.getText(),userDepartmentTxt.getText());


                    if (departmentList.getSelectedItem().equals("Electrical Engineer")) {
                        studentPanel.setVisible(false);
                        selectStudent("\'Electrical Engineering\'");
                    } else if (departmentList.getSelectedItem().equals("Computer Science")) {
                        studentPanel.setVisible(false);
                        selectStudent("\'Computer Science\'");
                    } else if (departmentList.getSelectedItem().equals("Mechanical Engineer")) {
                        studentPanel.setVisible(false);
                        selectStudent("\'Mechanical Engineering\'");
                    }
                    addStudentPanel.setVisible(false);
                } else {
                    System.out.println("fill the text boxes");
                    JOptionPane.showMessageDialog(null, "fill the text boxes");
                }
            }
            else if (e.getSource() == submitBtn4)
            {
                if (departmentList.getSelectedItem() == "Electrical Engineer")
                {
                    studentAssignSection(selectedCourse2);
                    selectCoursePanel3.setVisible(false);
                }
                else if (departmentList.getSelectedItem() == "Computer Science")
                {
                    studentAssignSection(selectedCourse2);
                    selectCoursePanel3.setVisible(false);
                }
                else if (departmentList.getSelectedItem() == "Mechanical Engineer")
                {
                    studentAssignSection(selectedCourse2);
                    selectCoursePanel3.setVisible(false);
                }
            }
            else if (e.getSource() == backBtn)
            {
                studentPanel.setVisible(false);
                mainMenuPanel.setVisible(true);
            }
            else if (e.getSource() == backBtn2)
            {
                studentTablePanel.setVisible(false);
                studentPanel.setVisible(true);
            }
            else if (e.getSource() == backBtn3)
            {
                addStudentPanel.setVisible(false);
                studentTablePanel.setVisible(true);
            }
            else if (e.getSource() == backBtn4)
            {
                studentCoursesPanel.setVisible(false);
                studentTablePanel.setVisible(true);
            }
            else if (e.getSource() == backBtn5)
            {
                departmentCoursePanel.setVisible(false);
                viewStudentSchedule(selectedStudentID);
            }
            else if (e.getSource() == backBtn6)
            {
                selectCoursePanel3.setVisible(false);
                departmentCoursePanel.setVisible(true);
            }
            else if (e.getSource() == backBtn7)
            {
                selectedCourse2 = null;
                studentAssignSectionPanel.setVisible(false);
                selectCoursePanel3.setVisible(true);
            }
            else if (e.getSource() == mainMenuBtn)
            {
                mainMenuPanel.setVisible(true);
                instructorCourseTablePanel.setVisible(false);
            }
            else if (e.getSource() == mainMenuBtn2)
            {
                studentCoursesPanel.setVisible(false);
                mainMenuPanel.setVisible(true);
            }
            else if (e.getSource() == mainMenuBtn3)
            {
                studentAssignSectionPanel.setVisible(false);
                mainMenuPanel.setVisible(true);
            }
            else if (e.getSource() == addStudentCourseBtn)
            {
                studentCoursesPanel.setVisible(false);
                departmentCourses();
            }
            else if (e.getSource() == addStudentBtn)
            {
                studentTablePanel.setVisible(false);
                if (departmentList.getSelectedItem().equals("Electrical Engineer"))
                {
                    System.out.println((String) departmentList.getSelectedItem());
                    addStudent("Electrical Engineering");

                }
                else if (departmentList.getSelectedItem().equals("Computer Science"))
                {
                    System.out.println((String) departmentList.getSelectedItem());
                    addStudent("Computer Science");

                }
                else if (departmentList.getSelectedItem().equals("Mechanical Engineer"))
                {
                    System.out.println((String) departmentList.getSelectedItem());
                    addStudent("Mechanical Engineering");
                }
            }
            else if (e.getSource() == deleteStudentBtn)
            {
                Connection con = UniversityData.connect();
                PreparedStatement ps = null;
                try {
                    String sql = "DELETE FROM users WHERE ID IS " + studentListTable.getModel().getValueAt(studentListTable.getSelectedRow(),1);
                    ps = con.prepareStatement(sql);
                    ps.executeUpdate();
                    sql = "DELETE FROM studentCourses WHERE ID IS " + studentListTable.getModel().getValueAt(studentListTable.getSelectedRow(),1);
                    ps = con.prepareStatement(sql);
                    ps.executeUpdate();
                    studentListTableModel.removeRow(studentListTable.getSelectedRow());
                    JOptionPane.showMessageDialog(null, "Deleted successfully");
                }
                catch (SQLException wew)
                {
                    System.out.println(wew.toString());
                }
                finally
                {
                    try
                    {
                        ps.close();
                        con.close();
                    }
                    catch (SQLException throwables)
                    {
                        throwables.printStackTrace();
                    }
                }
            }
            else if (e.getSource() == viewScheduleBtn)
            {
                System.out.println(studentListTable.getModel().getValueAt(studentListTable.getSelectedRow(),1));
                viewStudentSchedule((String) studentListTable.getModel().getValueAt(studentListTable.getSelectedRow(),1));
                studentTablePanel.setVisible(false);

            }
            else if (e.getSource() == assignStudentSectionBtn)
            {
                Connection con = UniversityData.connect();
                PreparedStatement ps = null;
                ResultSet rs = null;
                int Credit = 0;
                int addedCredit = 0;
                int[] studentCoursesCRN = {0,0,0,0,0,0,0};
                String[] studentCoursesName = {"","","","","","",""};
                int coursePosition = 0;
                String courseName ="";
                try {
                    System.out.println("Student Selected: " + selectedStudentID);
                    String sql = "SELECT * FROM studentCourses WHERE ID IS " +selectedStudentID;
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        studentCoursesCRN[0] = rs.getInt("Course1");
                        studentCoursesCRN[1] = rs.getInt("Course2");
                        studentCoursesCRN[2] = rs.getInt("Course3");
                        studentCoursesCRN[3] = rs.getInt("Course4");
                        studentCoursesCRN[4] = rs.getInt("Course5");
                        studentCoursesCRN[5] = rs.getInt("Course6");
                        studentCoursesCRN[6] = rs.getInt("Course7");
                    }

                    for (int i = 0; i<7;i++) {
                        sql = "SELECT Credit FROM courses WHERE CRN IS " + studentCoursesCRN[i];
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();

                        while (rs.next()) {
                            Credit += rs.getInt("Credit");
                        }
                    }
                    System.out.println("Current credit is " +Credit);

                    System.out.println(coursesTable3.getModel().getValueAt(coursesTable3.getSelectedRow(), 0));
                    sql = "SELECT Credit FROM courses WHERE CRN = " + coursesTable3.getModel().getValueAt(coursesTable3.getSelectedRow(), 0);
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        addedCredit += rs.getInt("Credit");
                    }
                    sql = "SELECT Course FROM courses WHERE CRN = " + coursesTable3.getModel().getValueAt(coursesTable3.getSelectedRow(), 0);
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        courseName = rs.getString("Course");
                    }
                    for (int i =0;i<7;i++) {
                        sql = "SELECT Course FROM courses WHERE CRN IS " + studentCoursesCRN[i];
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            studentCoursesName[i] = rs.getString("Course");
                        }
                    }
                    System.out.println("Courses CRN: " + studentCoursesCRN[2]);
                    System.out.println("Courses Name: " + Arrays.toString(studentCoursesName));
                        if (Credit+addedCredit <= 15) {
                            for (int i=0; i<7;i++) {
                                if (!(courseName.equals(studentCoursesName[i])))
                                {
                                    if (i==6)
                                    {
                                        for (int x=0;x<7;x++) {
                                            if (studentCoursesCRN[x] == 0)
                                            {
                                                coursePosition = x ;
                                                break;
                                            }
                                        }
                                        sql = "UPDATE studentCourses SET Course"+((int) coursePosition+(int) 1)+" = " +
                                                coursesTable3.getValueAt(coursesTable3.getSelectedRow(),0) + " WHERE ID IS " + selectedStudentID;
                                        ps = con.prepareStatement(sql);
                                        System.out.println("pass");
                                        ps.executeUpdate();
                                        courseTableModel3.removeRow(coursesTable3.getSelectedRow());
                                        JOptionPane.showMessageDialog(null, "Added successfully");
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "Course Already assigned");
                                    break;
                                }
                            }
                    } else {
                        JOptionPane.showMessageDialog(null, "Cannot Assign Course Student added credit is " + ((int) Credit+ (int) addedCredit));
                        }

                } catch (SQLException e2) {
                    System.out.println(e2.toString());
                } finally {
                    try {
                        ps.close();
                        rs.close();
                        con.close();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
        public void actionPerformed(ActionEvent e) {

        }

     private String selectedString(ItemSelectable is) {
        Object selected[] = is.getSelectedObjects();
        return ((selected.length == 0) ? "null" : (String) selected[0]);
    }
    ItemListener itemListenerCourse = new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
            int state = e.getStateChange();
            System.out.println((state == ItemEvent.SELECTED) ? "Selected" : "Deselected");
            System.out.println("Item: " + e.getItem());
            ItemSelectable is = e.getItemSelectable();
            System.out.println(", Selected: " + selectedString(is));
            if (!(selectedString(is).equals("Add Course"))) {
                deleteCourse.setVisible(true);
                selectedCourse = "\'"+selectedString(is)+"\'";
            }
            else
                deleteCourse.setVisible(false);

        }
    };
    ItemListener itemListenerCourse2 = new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
            int state = e.getStateChange();
            System.out.println((state == ItemEvent.SELECTED) ? "Selected" : "Deselected");
            System.out.println("Item: " + e.getItem());
            ItemSelectable is = e.getItemSelectable();
            System.out.println(", Selected course: " + selectedString(is));
            if (!(selectedString(is).equals("Add Course"))) {
                selectedCourse2 = "\'"+selectedString(is)+"\'";
            }
        }
    };
    ItemListener itemListenerInstructor = new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
            int state = e.getStateChange();
            System.out.println((state == ItemEvent.SELECTED) ? "Selected" : "Deselected");
            System.out.println("Item: " + e.getItem());
            ItemSelectable is = e.getItemSelectable();
            System.out.println(", Selected: " + selectedString(is));
            if (!(selectedString(is).equals("Add Instructor"))) {
                deleteInstructor.setVisible(true);
                selectedInstructor = "\'"+selectedString(is)+"\'";
            }
            else
                deleteInstructor.setVisible(false);

        }
    };

}
