package GUI.StopButtonArea;

import javax.swing.*;

public class StopButton extends JButton {
    public StopButton(String buttonText){
        this.setFocusable(false);
        this.setText(buttonText);
    }
}
