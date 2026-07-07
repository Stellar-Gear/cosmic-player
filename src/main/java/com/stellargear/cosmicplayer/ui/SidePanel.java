package com.stellargear.cosmicplayer.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SidePanel {

    private final VBox layout = new VBox();
    private final Label home = new Label("Home");

    public SidePanel() {
        layout.getChildren().addAll(home);
    }

    public VBox getNode() {
        return layout;
    }
}
