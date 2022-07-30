import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class InstructorPage extends UniversityData {
    private JButton logout, viewStudentsBtn, backBtn;
    private JPanel headerPanel, coursesTablePanel, studentTablePanel;
    private JFrame menuFrame;
    private List<Integer> coursesCrn;
    private JTable coursesTable, studentListTable;
    private DefaultTableModel coursesTableModel, studentListTableModel;
    private final Object[] courseColumns = {"CRN","Course", "Section", "Days", "Time", "Room", "Credit"};
    private final Object[] studentList = {"Name","ID"};
    private Object[][] courseData, studentData;
    instructorActionHandler instructorHandeler = new instructorActionHandler();

    InstructorPage(String id) {
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

        coursesTableModel = new DefaultTableModel(courseData, courseColumns) {
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
        coursesCrn = getInstructorCRN(id);
        System.out.println(coursesCrn);

        Connection con = UniversityData.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            for (Integer integer : coursesCrn) {
                String sql = "SELECT CRN, Course, Section, Days, Time, Room, Credit FROM courses WHERE" +
                        " CRN IS " + integer;
                Object[] tempRow;

                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    tempRow = new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                            rs.getString(5), rs.getString(6), rs.getInt(7)};
                    System.out.println("Row Added");
                    coursesTableModel.addRow(tempRow);
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
        coursesTable = new JTable(coursesTableModel) {
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

        coursesTablePanel = new JPanel();
        JLabel department = new JLabel(getDepartment(id));
        department.setFont(new Font("Arial", Font.BOLD, 22));
        coursesTablePanel.add(department);
        coursesTablePanel.add(scrollPane, BorderLayout.CENTER);

        coursesTablePanel.setBounds(59, 135, 680, 270);
        coursesTablePanel.setBackground(Color.WHITE);

        viewStudentsBtn = new JButton("View Students");
        viewStudentsBtn.addActionListener(instructorHandeler);
        coursesTablePanel.add(viewStudentsBtn);

        menuFrame = new JFrame();
        menuFrame.setTitle("Student Registration Program SKMM");
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setSize(800, 480);
        menuFrame.setResizable(false);
        menuFrame.setLayout(null);
        menuFrame.setIconImage(icon.getImage());
        menuFrame.getContentPane().setBackground(Color.WHITE);

        menuFrame.add(headerPanel);
        menuFrame.add(coursesTablePanel);
        menuFrame.setVisible(true);
    }

    public void viewStudents(int CRN) {
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
        Connection con = UniversityData.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql;
            for (int i =1; i<=7;i++) {
                sql = "SELECT ID FROM studentCourses WHERE Course"+i +"= " + CRN;
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();

                Object[] tempRow;

                while (rs.next()) {
                    tempRow = new Object[]{rs.getString("ID"), getUserName(rs.getString("ID"))};
                    System.out.println(Arrays.toString(tempRow));
                    studentListTableModel.addRow(tempRow);

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
        studentListTable = new JTable(studentListTableModel) {
            public boolean isCellEditable(int data, int columns) {
                return false;
            }
        };
        studentListTable.setAutoCreateColumnsFromModel(true);
        JScrollPane scrollPane = new JScrollPane(studentListTable);
        studentListTable.setPreferredScrollableViewportSize(new Dimension(600, 80));
        studentListTable.setFillsViewportHeight(true);
        studentListTable.setDragEnabled(false);

        studentTablePanel = new JPanel();
        studentTablePanel.add(scrollPane, BorderLayout.CENTER);

        backBtn = new JButton("Back");
        backBtn.addActionListener(instructorHandeler);

        studentTablePanel.add(backBtn);

        studentTablePanel.setBounds(59, 170, 660, 270);
        studentTablePanel.setBackground(Color.WHITE);
        menuFrame.add(studentTablePanel);

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
    private class instructorActionHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == viewStudentsBtn)
            {
                if (coursesTable.getSelectedRow() == -1)
                    coursesTable.setRowSelectionInterval(0,0);
                System.out.println("hello");
                coursesTablePanel.setVisible(false);
                viewStudents((Integer) coursesTable.getModel().getValueAt(coursesTable.getSelectedRow(),0));
            }
            else if (e.getSource() == backBtn)
            {
                studentTablePanel.setVisible(false);
                coursesTablePanel.setVisible(true);
            }
        }
    }

}
