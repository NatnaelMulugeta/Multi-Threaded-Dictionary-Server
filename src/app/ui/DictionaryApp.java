package app.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DictionaryApp {
    private JPanel MainPannel;
    private JTextField textField1;
    private JButton searchButton;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextArea textArea1;
    private JButton saveButton;
    private JButton deleteButton;
    private JButton clearButton;

    public DictionaryApp() {
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(null,"working");
            }
        });
    }

    public static void main(String[] arg){
        JFrame frame=new JFrame("Dictionary");
        frame.setContentPane(new DictionaryApp().MainPannel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }
}
