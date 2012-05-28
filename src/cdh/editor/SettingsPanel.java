package cdh.editor;

import cdh.editor.objects.Track;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class SettingsPanel extends JPanel implements ActionListener, ListSelectionListener {

    private MyButton generateXML;
    private MyButton addTrack;
    public static JList tracks;
    private DefaultListModel model;

    public SettingsPanel() {
        super(new GridLayout(12, 1, 0, 5));
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        setPreferredSize(new Dimension(150, 720));
        setBackground(new Color(240, 240, 240));
        initLayout();
    }

    private void initLayout() {
        generateXML = new MyButton("Generate XML");
        generateXML.addActionListener(this);
        add(generateXML);
        add(new Gap());

        addTrack = new MyButton("Add new Track");
        addTrack.addActionListener(this);
        add(addTrack);
        
        model = new DefaultListModel();
        tracks = new JList(model);
        tracks.addListSelectionListener(this);
        tracks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(tracks);
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if((MyButton) source == addTrack) {
            Canvas.tracks.add(new Track(false));
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        Main.frame.repaint();
        String trackName = tracks.getSelectedValue().toString();

        for(Track track: Canvas.tracks){
            if (track.name.equals(trackName))
                track.select();
            else
                track.deselect();
        }
    }

    private class Gap extends JLabel {

        public Gap() {
            super();
        }
    }

    private class MyButton extends JButton {

        public MyButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFont(new Font("SansSerif", Font.PLAIN, 15));
        }

        @Override
        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;

            if (getModel().isRollover())
                g2.setColor(Color.CYAN);
            else
                g2.setColor(Color.CYAN.darker());
            
            g2.fill(new Rectangle2D.Float(0, 0, getWidth(), getHeight()));
            super.paintComponent(g);
        }
    }
}
