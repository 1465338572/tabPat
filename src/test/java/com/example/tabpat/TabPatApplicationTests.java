package com.example.tabpat;
import org.springframework.boot.test.context.SpringBootTest;

import javax.swing.*;
import java.awt.*;

@SpringBootTest
class TabPatApplicationTests extends JPanel {
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawLine(100,100,200,200);
        g.drawRect(100,100,100,100);
    }

    public static void main(String []args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test");

            TabPatApplicationTests panel = new TabPatApplicationTests();
            panel.setPreferredSize(new Dimension(400, 400));

            // 配置主窗口
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
