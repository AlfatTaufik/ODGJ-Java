package javaguimusicplayerjframe;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.filechooser.*;

public class JavaGUIMusicPlayerJFrame extends JFrame implements ActionListener {

    private JTextField filePathField;
    private JButton playButton;
    private JButton pauseButton;
    private JButton chooseButton;
    private JButton loopButton;
    private boolean isPaused;
    private boolean isLooping = false;
    private JFileChooser fileChooser;
    private Clip clip;
    
    public JavaGUIMusicPlayerJFrame() 
    {
        super("Music Player");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        
        filePathField = new JTextField(20);
        playButton = new JButton("Play");
        pauseButton = new JButton("Pause");
        chooseButton = new JButton("Choose File");
        loopButton = new JButton("Loop");
        isPaused = false;
        isLooping = false;
        
        playButton.addActionListener(this);
        pauseButton.addActionListener(this);
        chooseButton.addActionListener(this);
        loopButton.addActionListener(this);
        
        add(filePathField);
        add(chooseButton);
        add(playButton);
        add(pauseButton);
        add(loopButton);
        
        fileChooser = new JFileChooser(".");
        fileChooser.setFileFilter(new FileNameExtensionFilter("WAV Files", "wav"));
        
        setSize(500, 100);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent event) {
        
        if (event.getSource() == playButton) 
        {
            playMusic();
        } 
        else if (event.getSource() == pauseButton) 
        {
            pauseMusic();
        } 
        else if (event.getSource() == chooseButton) 
        {
            chooseFile();
        } 
        else if (event.getSource() == loopButton) 
        {
            toggleLoop();
        }
    }
    
    private void playMusic() {
        
        if (clip != null && clip.isRunning()) 
        {
            clip.stop();
        }
        
        try 
        {
            File file = new File(filePathField.getText());
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
            
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            
            if (isLooping) 
            {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            
            clip.start();

        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
    }
    
    private void pauseMusic() 
    {
        if (clip != null && clip.isRunning()) 
        {
            clip.stop();
            isPaused = true;
            pauseButton.setText("Resume");
        } 
        else if (clip != null && isPaused) 
        {
            clip.start();
            
            if(isLooping)
            {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            
            isPaused = false;
            pauseButton.setText("Pause");
        }
    }
    
    private void chooseFile() 
    {
        fileChooser.setCurrentDirectory(new File("."));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) 
        {
            File selectedFile = fileChooser.getSelectedFile();
            filePathField.setText(selectedFile.getAbsolutePath());
        }
    }
    
    private void toggleLoop() 
    {
        isLooping = !isLooping;
        if (isLooping) 
        {
            loopButton.setText("Stop Loop");
            
            if(clip.isRunning())
            {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } 
        }
        else 
        {
            loopButton.setText("Loop");

            if(clip.isRunning())
            {
                clip.loop(0);
            }
        }
    }
    
    public static void main(String[] args) {
        new JavaGUIMusicPlayerJFrame();
    }
}