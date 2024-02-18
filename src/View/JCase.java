/*
 * Copyright (c) 2022.
 */

package View;

import javax.swing.*;

public class JCase extends JButton {
    private final int x;
    private final int y;
    private boolean isTouched;
    private boolean isShip;

    public JCase(int x, int y) {
        this.x = x;
        this.y = y;
        this.isTouched = false;
        this.isShip = false;
        //this.setText(x + " " + y);
        this.setText("X");
        setSize(10, 10);
        setModel(new DefaultButtonModel());
        setAlignmentX(LEFT_ALIGNMENT);
        setAlignmentY(TOP_ALIGNMENT);
        //setAlignmentY(CENTER_ALIGNMENT);
        updateUI();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isTouched() {
        return isTouched;
    }

    public void setTouched(boolean touched) {
        isTouched = touched;
    }

    public boolean isShip() {
        return isShip;
    }

    public void setShip(boolean ship) {
        isShip = ship;
    }
}

