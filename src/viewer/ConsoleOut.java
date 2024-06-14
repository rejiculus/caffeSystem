package viewer;

import java.io.*;

import javax.swing.*;

import models.Order;

public class ConsoleOut extends Thread{
    private JTextArea textArea;
    private JFrame frame;
    PipedOutputStream pipedOutputStream;
    PipedInputStream pipedInputStream;
    PrintStream printStream;
    public ConsoleOut(){
        textArea = new JTextArea(20, 50);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Создаем новое окно (JFrame) и добавляем в него прокручиваемую панель
        JFrame frame = new JFrame("New Console");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(scrollPane);
        frame.pack();
        frame.setVisible(true);


        pipedOutputStream = new PipedOutputStream();
        pipedInputStream = new PipedInputStream();
        try {
            pipedOutputStream.connect(pipedInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        printStream = new PrintStream(pipedOutputStream);
    }
    public void run(){
        BufferedReader reader = new BufferedReader(new InputStreamReader(pipedInputStream));
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    final String rere = line;
                    SwingUtilities.invokeLater(() -> this.textArea.append(rere + "\n"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        
    }
    public void printLine(String line){
        this.printStream.println(line);
    }
    public void printLine(Order order){
        this.printStream.println(order.toString());
    }
    public void setText(String text){
        this.textArea.setText(text);
    }
    public void clear(){
        this.textArea.setText("");
    }
}