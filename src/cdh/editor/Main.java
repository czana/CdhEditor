package cdh.editor;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public void createGUI() {
        JFrame frame = new JFrame("Clay Defenser Editor by LAZYSOFT");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(initCanvas());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private Canvas initCanvas() {
        Canvas canvas = new Canvas();
        return canvas;
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
