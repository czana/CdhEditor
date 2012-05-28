package cdh.editor.objects;

import cdh.editor.Main;
import cdh.editor.SettingsPanel;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.DefaultListModel;

public class Track extends Component{

    private ArrayList<CanvasObject> waypoints = new ArrayList<CanvasObject>();
    private boolean selected = false;
    static int number = 1;
    public String name;

    public Track(boolean selected) {
        this.selected = selected;
        this.name = "track " + (number++);
        ((DefaultListModel) SettingsPanel.tracks.getModel()).add(0, name);
        if (selected)
            SettingsPanel.tracks.setSelectedIndex(0);
    }

    public ArrayList<CanvasObject> getWaypoints() {
        return waypoints;
    }

    public boolean isSelected() {
        return selected;
    }

    public void select() {
        selected = true;
    }

    public void deselect() {
        selected = false;
    }
}
