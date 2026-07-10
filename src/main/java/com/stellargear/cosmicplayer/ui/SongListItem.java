package com.stellargear.cosmicplayer.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class SongListItem {

    private HBox box;
    private Label songName = new Label("");
    private Label artistName = new Label("");
    private Label songDuration = new Label("");

    public SongListItem (String song, String artist, String duration) {
        box = new HBox(songName, artistName, songDuration);
        songName.setText(song);
        artistName.setText(artist);
        songDuration.setText(duration);

        box.getStyleClass().add("list-item");
    }

    public HBox getNode () {
        return box;
    }
}
