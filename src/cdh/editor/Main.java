package cdh.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static JFrame frame;
            
    public void createGUI() {
        frame = new JFrame("Clay Defenser Editor by LAZYSOFT");
        SettingsPanel sp = new SettingsPanel();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Canvas(), BorderLayout.CENTER);
        frame.add(sp, BorderLayout.EAST );
        frame.setMinimumSize(new Dimension(1350, 720));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new Main().createGUI();
            }
        });
    }
}
