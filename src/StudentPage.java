import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class StudentPage extends UniversityData {
    JButton logout;
    JButton deleteStudentCourse;
    JButton addStudentCourseBtn;
    JButton backBtn2;
    JButton submitBtn2;
    JButton assignStudentSectionBtn;
    JButton backBtn3;
    JButton mainMenuBtn;
    JPanel headerPanel;
    JPanel studentCoursesPanel;
    JPanel coursePanel;
    JPanel sectionPanel;
    JFrame menuFrame;
    DefaultTableModel studentCourseTableModel, studentCourseTableModel2;
    JTable studentCourseTable, coursesTable;
    private Object[][] courseData;
    private final Object[] courseColumns = {"CRN", "Course", "Section", "Days", "Time", "Room", "Instructor", "Credit"};
    private final Object[] courseColumns2 = {"CRN", "Course", "Section", "Days", "Time", "Room", "Credit"};
    private int[] studentCourses = {0, 0, 0, 0, 0, 0, 0};
    private static String selectedStudentID;
    actionHandlerStudent studentHandler = new actionHandlerStudent();
    private JComboBox courseList;
    private ArrayList<String> courses;
    private String selectedCourse;
    StudentPage(String id) {
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

        menuFrame = new JFrame();
        menuFrame.setTitle("Student Registration Program SKMM");
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setSize(800, 480);
        menuFrame.setResizable(false);
        menuFrame.setLayout(null);
        menuFrame.setIconImage(icon.getImage());
        menuFrame.getContentPane().setBackground(Color.WHITE);

        menuFrame.add(headerPanel);
        menuFrame.setVisible(true);
        selectedStudentID = id;
        studentSchedule(id);

    }
    public void studentSchedule(String id) {
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
            String sql = "SELECT * FROM studentCourses WHERE ID IS " + id;
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

            for (int i = 0; i < 7; i++) {
                sql = "SELECT CRN, Course, Section, Days, Time, Room, Instructor, Credit FROM courses WHERE" +
                        " CRN IS " + studentCourses[i];
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    tempRow = new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                            rs.getString(5), rs.getString(6), rs.getString(7), rs.getInt(8)};
                    System.out.println("Row Added");
                    studentCourseTableModel.addRow(tempRow);
                }
            }

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        studentCourseTable = new JTable(studentCourseTableModel) {
            public boolean isCellEditable(int data, int columns) {
                return false;
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
        studentName.setText(getUserName(id) + " - " + id);
        studentName.setFont(new Font("Arial", Font.BOLD, 22));
        studentCoursesPanel.add(studentName);
        studentCoursesPanel.add(scrollPane, BorderLayout.CENTER);

        deleteSectionHandler deleteSectionHandler = new deleteSectionHandler();

        deleteStudentCourse = new JButton("Delete");
        addStudentCourseBtn = new JButton("Add");
        deleteStudentCourse.setBackground(Color.RED);

        deleteStudentCourse.addActionListener(deleteSectionHandler);
        addStudentCourseBtn.addActionListener(studentHandler);

        studentCoursesPanel.add(deleteStudentCourse);
        studentCoursesPanel.add(addStudentCourseBtn);

        studentCoursesPanel.setBounds(69, 135, 670, 270);
        studentCoursesPanel.setBackground(Color.WHITE);
        menuFrame.add(studentCoursesPanel);
    }


    public void studentAssignCourse() {
        Connection con = UniversityData.connect();
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        try {
            System.out.println(getDepartment(selectedStudentID));
            String sql = "SELECT Course FROM courseMain WHERE Department IS " + "'" +getDepartment(selectedStudentID)+ "'";

//            String sql2 = "SELECT Name FROM courses WHERE CRN IS "  + courses.get();
//            ps2 = con.prepareStatement(sql2);
//            rs2 = ps2.executeQuery();

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            courses = new ArrayList<>();
            System.out.println(courses);
            while (rs.next()) {
                courses.add(rs.getString(1));
            }

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            try {
                con.close();
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        backBtn2 = new JButton("Back");
        submitBtn2 = new JButton("Next");

        backBtn2.addActionListener(studentHandler);
        submitBtn2.addActionListener(studentHandler);

        backBtn2.setFocusable(false);
        submitBtn2.setFocusable(false);

        coursePanel = new JPanel();
        coursePanel.setBounds(250, 135, 300, 300);
        coursePanel.setBackground(Color.white);
        courseList = new JComboBox();
        courseList.setPreferredSize(new Dimension(300, 30));
        JLabel tempLabel = new JLabel("Select a Course");
        tempLabel.setFont(new Font("Arial", Font.BOLD, 15));
        for (String course : courses) courseList.addItem(course);
        coursePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 35));
        coursePanel.add(tempLabel);
        coursePanel.add(courseList);
        coursePanel.add(backBtn2);
        coursePanel.add(submitBtn2);
        coursePanel.setVisible(true);
        menuFrame.add(coursePanel);
        courseList.addItemListener(itemListenerCourse);
    }

    public void studentAssignSection(String Course) {
        Connection con = UniversityData.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        studentCourseTableModel2 = new DefaultTableModel(courseData, courseColumns2);
        if (selectedCourse == null)
            Course = "'" + courseList.getItemAt(0) + "'";
        try {
            System.out.println("Course is " + Course);
            String sql = "SELECT CRN, Course, Section, Days, Time, Room, Credit FROM courses WHERE Course IS " + Course;

            Object[] tempRow;

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                tempRow = new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getString(6), rs.getInt(7)};
                System.out.println("Row Added");
                studentCourseTableModel2.addRow(tempRow);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        coursesTable = new JTable(studentCourseTableModel2) {
            public boolean isCellEditable(int data, int columns) {
                return false;
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
        columnModel.getColumn(6).setPreferredWidth(17);

        sectionPanel = new JPanel();

        JLabel courseName = new JLabel(selectedCourse);
        courseName.setFont(new Font("Arial", Font.BOLD, 22));
        sectionPanel.add(courseName);
        sectionPanel.add(scrollPane, BorderLayout.CENTER);


        backBtn3 = new JButton("Back");
        assignStudentSectionBtn = new JButton("Assign");
        mainMenuBtn = new JButton("Main Menu");
        backBtn3.addActionListener(studentHandler);
        assignStudentSectionBtn.addActionListener(studentHandler);
        mainMenuBtn.addActionListener(studentHandler);


        sectionPanel.add(backBtn3);
        sectionPanel.add(assignStudentSectionBtn);
        sectionPanel.add(mainMenuBtn);

        sectionPanel.setBounds(49, 180, 670, 270);
        sectionPanel.setBackground(Color.WHITE);
        menuFrame.add(sectionPanel);


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

    private class deleteSectionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == deleteStudentCourse) {
                Connection con = UniversityData.connect();
                PreparedStatement ps = null;
                try {
                    for (int i = 1; i <= 7; i++) {
                        String sql = "UPDATE studentCourses SET Course" + i + " = ? WHERE ID IS " + selectedStudentID +
                                " AND Course" + i + " IS " + studentCourseTable.getValueAt(studentCourseTable.getSelectedRow(), 0);

                        ps = con.prepareStatement(sql);
                        ps.setInt(1, 0);
                        ps.executeUpdate();
                    }

                    studentCourseTableModel.removeRow(studentCourseTable.getSelectedRow());
                    JOptionPane.showMessageDialog(null, "Deleted successfully");

                } catch (SQLException e4) {
                    System.out.println(e4.toString());
                } finally {
                    try {
                        if (ps != null) {
                            ps.close();
                        }
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
            if (e.getSource() == addStudentCourseBtn) {
                studentCoursesPanel.setVisible(false);
                studentAssignCourse();

            }
            else if (e.getSource() == backBtn2) {
                coursePanel.setVisible(false);
                studentSchedule(selectedStudentID);
            }
            else if (e.getSource() == submitBtn2) {
                studentAssignSection(selectedCourse);
                coursePanel.setVisible(false);
            }
            else if (e.getSource() == assignStudentSectionBtn) {
                Connection con = UniversityData.connect();
                PreparedStatement ps = null;
                ResultSet rs = null;
                int Credit = 0;
                int addedCredit = 0;
                int[] studentCoursesCRN = {0, 0, 0, 0, 0, 0, 0};
                String[] studentCoursesName = {"", "", "", "", "", "", ""};
                int coursePosition = 0;
                String courseName = "";
                try {
                    System.out.println("Student Selected: " + selectedStudentID);
                    String sql = "SELECT * FROM studentCourses WHERE ID IS " + selectedStudentID;
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

                    for (int i = 0; i < 7; i++) {
                        sql = "SELECT Credit FROM courses WHERE CRN IS " + studentCoursesCRN[i];
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();

                        while (rs.next()) {
                            Credit += rs.getInt("Credit");
                        }
                    }
                    System.out.println("Current credit is " + Credit);

                    System.out.println(coursesTable.getModel().getValueAt(coursesTable.getSelectedRow(), 0));
                    sql = "SELECT Credit FROM courses WHERE CRN = " + coursesTable.getModel().getValueAt(coursesTable.getSelectedRow(), 0);
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        addedCredit += rs.getInt("Credit");
                    }
                    sql = "SELECT Course FROM courses WHERE CRN = " + coursesTable.getModel().getValueAt(coursesTable.getSelectedRow(), 0);
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        courseName = rs.getString("Course");
                    }
                    for (int i = 0; i < 7; i++) {
                        sql = "SELECT Course FROM courses WHERE CRN IS " + studentCoursesCRN[i];
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            studentCoursesName[i] = rs.getString("Course");
                        }
                    }
                    System.out.println("Courses CRN: " + studentCoursesCRN[2]);
                    System.out.println("Courses Name: " + Arrays.toString(studentCoursesName));
                    if (Credit + addedCredit <= 15) {
                        for (int i = 0; i < 7; i++) {
                            if (!(courseName.equals(studentCoursesName[i]))) {
                                if (i == 6) {
                                    for (int x = 0; x < 7; x++) {
                                        if (studentCoursesCRN[x] == 0) {
                                            coursePosition = x;
                                            break;
                                        }
                                    }
                                    sql = "UPDATE studentCourses SET Course" + (coursePosition + 1) + " = " +
                                            coursesTable.getValueAt(coursesTable.getSelectedRow(), 0) + " WHERE ID IS " + selectedStudentID;
                                    ps = con.prepareStatement(sql);
                                    System.out.println("pass");
                                    ps.executeUpdate();
                                    studentCourseTableModel2.removeRow(coursesTable.getSelectedRow());
                                    JOptionPane.showMessageDialog(null, "Added successfully");
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Course Already assigned");
                                break;
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Cannot Assign Course Student current credit is " + (Credit + addedCredit));
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
            else if (e.getSource() == backBtn3) {
                    sectionPanel.setVisible(false);
                    coursePanel.setVisible(true);
                }
            }
        }

        private String selectedString(ItemSelectable is) {
            Object[] selected = is.getSelectedObjects();
            return ((selected.length == 0) ? "null" : (String) selected[0]);
        }

        ItemListener itemListenerCourse = new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();
                System.out.println((state == ItemEvent.SELECTED) ? "Selected" : "Deselected");
                System.out.println("Item: " + e.getItem());
                ItemSelectable is = e.getItemSelectable();
                System.out.println(", Selected course: " + selectedString(is));
                if (!(selectedString(is).equals("Add Course"))) {
                    selectedCourse = "'" + selectedString(is) + "'";
                }
            }
        };


    }