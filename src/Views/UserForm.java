package Views;

import Models.NguoiDung;
import javax.swing.*;

public class UserForm extends JFrame {
    public UserForm(NguoiDung nd) {
        setTitle("User - Xin chào " + nd.getHoTen());
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JLabel label = new JLabel("Chào mừng User: " + nd.getHoTen(), JLabel.CENTER);
        add(label);
    }
}
