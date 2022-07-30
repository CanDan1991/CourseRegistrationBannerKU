import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UniversityData extends JFrame implements ActionListener {
    //CONNECT TO DATABASE

    public static Connection connect() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:KhalifaUniDB.db");
            System.out.println("connected");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e+"");
        }
        return con;
    }
    //ADD STUDENT || INSTRUCTOR || ADMIN?? METHOD
    public static void insertUser(int ID, String Name, String Password, String Job, String Department) {
        Connection con = UniversityData.connect();
        PreparedStatement ps = null;
        try {
            if (Integer.parseInt(String.valueOf(String.valueOf(ID).charAt(0))) == 3) {
                String sql = "INSERT INTO studentCourses(ID, Course1, Course2, Course3, Course4,Course5,Course6,Course7) " +
                        "VALUES(?,?,?,?,?,?,?,?)";
                ps = con.prepareStatement(sql);
                ps.setInt(1,ID);
                ps.setString(2,"0");
                ps.setString(3,"0");
                ps.setString(4,"0");
                ps.setString(5,"0");
                ps.setString(6,"0");
                ps.setString(7,"0");
                ps.setString(8,"0");
                ps.execute();
            }
            String sql = "INSERT INTO users(ID, Name, Password, Job, Department) VALUES(?, ?, ?, ?, ?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, ID);
            ps.setString(2, Name);
            ps.setString(3, Password);
            ps.setString(4, Job);
            ps.setString(5,Department);
            ps.execute();
            System.out.println("Data has been inserted");

        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    //AUTHENTICATION METHOD
    public static boolean readAuthentication(String userID, String userPassword) {
        Connection con = UniversityData.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT ID, Password From users";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String ID = rs.getString("ID");
                String password = rs.getString("Password");

                if (ID.equals(userID) && password.equals(userPassword)) {
                    System.out.println("logged in successfully");
                    return true;
                }

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
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    return false;
    }
    //ADD COURSE METHOD
    public static void insertCourse(int CRN, String Course, String Section, String Name, int Max_Students,
                                    String Days, String Time, String Room, String Instructor, int Credit,
                                    String Department, String StartDate, String EndDate) {
        Connection con = UniversityData.connect();
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO courses(CRN, Course, Section, Name, Max_Students, Days, Time, Room, Instructor" +
                    ", Credit, Department, Start_Date, End_Date) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, CRN);
            ps.setString(2, Course);
            ps.setString(3, Section);
            ps.setString(4, Name);
            ps.setInt(5, Max_Students);
            ps.setString(6, Days);
            ps.setString(7, Time);
            ps.setString(8, Room);
            ps.setString(9, Instructor);
            ps.setInt(10, Credit);
            ps.setString(11, Department);
            ps.setString(12, StartDate);
            ps.setString(13, EndDate);

            ps.execute();
            sql = "INSERT INTO courseMain(Course, Department) VALUES(?, ?)";
            ps = con.prepareStatement(sql);
            ps.setString(1,Course);
            ps.setString(2,Department);
            ps.execute();

            System.out.println("Data has been inserted");

        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            try {
                con.close();
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    public static String getUserName(String userID) {
        Connection con = UniversityData.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT Name FROM users WHERE ID IS "+userID;
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            return rs.getString("Name");
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
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }
    return "";
    }
    public static List<Integer> getInstructorCRN(String ID) {
        Connection con = UniversityData.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Integer> tempList = new ArrayList<>();
        try {
            String sql = "SELECT CRN FROM courses WHERE Instructor is " + "'" +getUserName(ID)+ "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                tempList.add(rs.getInt("CRN"));
            }
            return tempList;
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
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
        return null;
    }
    public static String getDepartment(String ID) {
        Connection con = UniversityData.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT Department FROM users WHERE ID is " + ID;
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            return rs.getString("Department");
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
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
        return "";
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}

