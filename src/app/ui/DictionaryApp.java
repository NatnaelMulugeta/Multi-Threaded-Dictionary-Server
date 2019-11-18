package app.ui;

import app.code.ClientManager;
import app.code.Word;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DictionaryApp {
    private JPanel MainPannel;
    private JTextField search_field;
    private JButton searchButton;
    private JTextField word_field;
    private JTextField origin_field;
    private JTextField first_used_field;
    private JTextArea meaning_field;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton addButton;
    private JList suggestionlist;

    //CODE :: Objects
    private ClientManager clientManager;

    public DictionaryApp(ClientManager clientManager) {
        // creating client manager
        this.clientManager=clientManager;
        JOptionPane.showMessageDialog(null,this.clientManager.initialize_connection());
        updateButton.setEnabled(false);


        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(!clientManager.isConnected()) {
                    JOptionPane.showMessageDialog(null, "Unable to contact the server  please make sure its running");
                    return;
                }
                Word word=clientManager.getWord(search_field.getText().toString());
                clearButton.doClick();
                if(word==null){
                    JOptionPane.showMessageDialog(null,"Unable to find Your word. :"+search_field.getText().toString());
                }else{
                    updateButton.setEnabled(true);
                    addButton.setEnabled(false);
                    word_field.setText(word.getWord());
                    origin_field.setText(word.getOrigin());
                    first_used_field.setText(word.getfirst_used());
                    for(String meanings :word.getMeanings()){
                        meaning_field.append(meanings);
                        meaning_field.append("\n");
                    }
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                word_field.setText("");
                origin_field.setText("");
                first_used_field.setText("");
                meaning_field.setText("");
            }
        });

        word_field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                updateButton.setEnabled(false);
                addButton.setEnabled(true);
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(!clientManager.isConnected()) {
                    JOptionPane.showMessageDialog(null, "Unable to contact the server  please make sure its running");
                    return;
                }
                boolean ans=clientManager.removeWord(word_field.getText().toString());
                clearButton.doClick();
                if (ans==true){
                    JOptionPane.showMessageDialog(null,"Word deleted successfully.");
                }else{
                    JOptionPane.showMessageDialog(null,"Unable to find the word.");
                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(!clientManager.isConnected()) {
                    JOptionPane.showMessageDialog(null, "Unable to contact the server  please make sure its running");
                    return;
                }
                LinkedList meanings=getMeanings();
                if (!checkFields())
                    return;
                Word word= new Word(word_field.getText().toString(),first_used_field.getText().toString(),origin_field.getText().toString(),
                meanings);
                boolean ans=clientManager.replaceWord(word);
                if (ans==true){
                    JOptionPane.showMessageDialog(null,"Word replace successfully.");
                }else{
                    JOptionPane.showMessageDialog(null,"Unable to replace the word.");
                }
                clearButton.doClick();
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(!clientManager.isConnected()) {
                    JOptionPane.showMessageDialog(null, "Unable to contact the server  please make sure its running");
                    return;
                }
                if (!checkFields())
                    return;
                else if(clientManager.getWord(word_field.getText())!=null){
                    JOptionPane.showMessageDialog(null,"Word :"+word_field.getText()+" already exists.");
                    clearButton.doClick();return;
                }
                LinkedList meanings=getMeanings();
                Word word= new Word(word_field.getText().toString(),first_used_field.getText().toString(),origin_field.getText().toString(),
                        meanings);
                boolean ans=clientManager.addWord(word);
                if (ans==true){
                    JOptionPane.showMessageDialog(null,"Word added successfully.");
                }else{
                    JOptionPane.showMessageDialog(null,"Unable to add the word.");
                }
                clearButton.doClick();
            }
        });

        search_field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                String key=search_field.getText().toString();
                setSuggestionList(key);
            }
        });

        suggestionlist.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                search_field.setText(suggestionlist.getSelectedValue().toString());
            }
        });

    }

    private LinkedList getMeanings(){
        LinkedList meanings= new LinkedList<String>();
        for(String meaning : meaning_field.getText().split("\n")){
            meanings.add(meaning);
        }
        return meanings;
    }

    private void setSuggestionList(String key){
        List suggestions=clientManager.getSuggestion(key);
        Object[] list=suggestions.toArray();
        suggestionlist.setListData(list);
        if(key.equals("")) suggestionlist.setListData(new String[0]);
    }
    private boolean checkFields(){
        if (word_field.getText().equals("")|| first_used_field.getText().equals("")||
                origin_field.getText().equals("")||meaning_field.getText().equals("")){
            JOptionPane.showMessageDialog(null," Please fill all of the input. ");
            return false;
        }
        return true;
    }


    public static void main(String[] arg){
        ClientManager clientManager=null;
        if (arg.length>0){
            try{
                clientManager=new ClientManager(Integer.parseInt(arg[1]),arg[0]);
            }catch (Exception e){
                JOptionPane.showMessageDialog(null," Please fill a valid address. ");
                return;
            }
        }else{
            clientManager=new ClientManager();
        }
        JFrame frame=new JFrame("Dictionary");
        DictionaryApp dictionaryApp=new DictionaryApp(clientManager);

        frame.setContentPane(dictionaryApp.MainPannel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/5, dim.height/5);

        frame.pack();
        if (!dictionaryApp.clientManager.isConnected())
            return;

        frame.setVisible(true);
        frame.setResizable(false);
    }
}
