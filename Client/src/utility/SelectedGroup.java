package utility;

import java.io.Serializable;

public class SelectedGroup implements Serializable {
    private static Group groupSelected;

    public static Group getGroupSelected() {
        return groupSelected;
    }

    public static void setGroupSelected(Group groupSelected) {
        SelectedGroup.groupSelected = groupSelected;
    }
}
