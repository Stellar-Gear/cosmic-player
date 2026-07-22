package com.stellargear.cosmicplayer.ui;

import java.io.File;

import com.stellargear.cosmicplayer.utils.Methods;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;

public class SongListItem {

    private HBox songTimeBox;
    private HBox generalBox;

    private VBox controlBox;

    private Label songName = new Label("");
    private Label artistName = new Label("");
    private Label songDuration = new Label("");

    private final ImageView coverArtBox = new ImageView();

    public SongListItem (File songFile, String song, String artist, String duration, byte[] imageData) {
        songTimeBox = new HBox(5, songName, songDuration);
        controlBox = new VBox(songTimeBox, artistName);
        generalBox = new HBox(4, coverArtBox, controlBox);

        coverArtBox.setFitWidth(40);
        coverArtBox.setFitHeight(40);
        coverArtBox.setPreserveRatio(true);

        generalBox.getStyleClass().add("list-item");

        update(songFile, song, artist, duration, imageData);
    }

    public void update(File songFile, String song, String artist, String duration, byte[] imageData) {
        songName.setText(song);
        artistName.setText(artist);
        songDuration.setText(duration);
        coverArtBox.setImage(Methods.toCachedThumbnail(songFile, imageData, 40));
    }

    public HBox getNode () {
        return generalBox;
    }
}
