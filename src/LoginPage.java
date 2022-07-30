import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginPage extends UniversityData {
    private JFrame frame;
    private JLabel label1,label2;
    private JPanel panel1,panel2,panel3;
    protected JTextField text1, text2;
    private JButton login;
    //LOGIN PAGE EX USE Username:"10001" and Password:"admin" TO LOGIN TO ADMIN
    LoginPage() {
        ImageIcon icon = new ImageIcon("KU-logo.jpg");
        ImageIcon image = new ImageIcon("KU-LoginImage.jpg");
        ImageIcon header = new ImageIcon("KU-Header.jpg");
        Image originalImage = image.getImage();

        panel1 = new JPanel();
        panel1.setBounds(20,30,380,380);
        panel1.setLayout(new BorderLayout());
        Image newImage = originalImage.getScaledInstance(panel1.getWidth(),panel1.getHeight(), Image.SCALE_SMOOTH);
        image = new ImageIcon(newImage);
        label1 = new JLabel();
        label1.setIcon(image);
        label1.setVerticalAlignment(JLabel.TOP);

        panel2 = new JPanel();
        panel2.setBounds(440,30,300,70);
        panel2.setLayout(new BorderLayout());
        label2 = new JLabel();
        label2.setIcon(header);
        label2.setVerticalAlignment(JLabel.TOP);

        panel3 = new JPanel();
        panel3.setBounds(500,150,200,145);
        panel3.setBackground(Color.WHITE);
        panel3.setLayout(new GridLayout(6,1));
        text1 = new JTextField();
        text2 = new JTextField();
        login = new JButton("Login");
        login.setFocusable(false);
        login.addActionListener(this);
        panel3.add(new JLabel("Username:"));
        panel3.add(text1);
        panel3.add(new JLabel("Password:"));
        panel3.add(text2);
        panel3.add(new JLabel(""));
        panel3.add(login);

        frame = new JFrame();
        frame.setTitle("Student Registration Program SKMM");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,480);
        frame.setLayout(null);
        frame.setIconImage(icon.getImage());
        panel1.add(label1);
        frame.add(panel1);
        panel2.add(label2);
        frame.add(panel2);
        frame.add(panel3);
        frame.getContentPane().setBackground(Color.WHITE);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == login) {
            if (UniversityData.readAuthentication(text1.getText(), text2.getText()))
            {
                if (text1.getText().charAt(0) == '1' && text1.getText().length() == 5)
                {
                    frame.dispose();
                    new AdminPage(text1.getText());
                }
                else if (text1.getText().charAt(0) == '2' && text1.getText().length() == 5)
                {
                    frame.dispose();
                    new InstructorPage(text1.getText());
                }
                else if (text1.getText().charAt(0) == '3' && text1.getText().length() == 5)
                {
                    frame.dispose();
                    new StudentPage(text1.getText());
                }
            } else {
                JOptionPane.showMessageDialog(null,"Please try again","Wrong Username or Password", JOptionPane.ERROR_MESSAGE);
            }

        }
    }
}
