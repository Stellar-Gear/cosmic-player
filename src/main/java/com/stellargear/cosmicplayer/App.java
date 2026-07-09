package com.stellargear.cosmicplayer;

import com.stellargear.cosmicplayer.services.FileService;
import com.stellargear.cosmicplayer.services.PlayerService;
import com.stellargear.cosmicplayer.ui.PlayerToolbar;
import com.stellargear.cosmicplayer.ui.SongList;
import java.io.File;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class App extends Application {

    PlayerService mediaPlayer = new PlayerService();
    FileService fileService = new FileService();
    PlayerToolbar playerToolbar = new PlayerToolbar();
    SongList songList = new SongList();

    @Override
    public void start(Stage stage) {
        File folder = new File("/home/dimewolf/Música/");
        songList.setItems(fileService.getSongNames("/home/dimewolf/Música/"));

        playerToolbar.getPlayBtn().setOnAction(e -> {
            String selected = songList.getSelected();
            if (selected != null) {
                mediaPlayer.playOrResume(
                    new File(folder, selected),
                    playerToolbar.getVolumeSlider().getValue()
                );

                File song = new File(folder, selected);
                var meta = fileService.readMetadata(song);
                playerToolbar.setSongInfo(meta.title(), meta.artist());
            }
        });

        playerToolbar
            .getVolumeSlider()
            .valueProperty()
            .addListener((obs, old, val) -> {
                mediaPlayer.changeVolume(val.doubleValue());
        });

        var progressSlider = playerToolbar.getProgressSlider();

        Timeline progressUpdater = new Timeline(
            new KeyFrame(Duration.millis(300), e -> {
                if (progressSlider.isValueChanging() || !mediaPlayer.isPlayable()) {
                    return;
                }

                long length = mediaPlayer.getLength();
                long time = mediaPlayer.getTime();

                if (length > 0) {
                    double pct = (time / (double) length) * 100.0;
                    progressSlider.setValue(pct);
                }

                playerToolbar.getCurrentTimeLabel().setText(formatTime(time));
                playerToolbar.getTotalTimeLabel().setText(formatTime(length));
            })
        );

        progressUpdater.setCycleCount(Timeline.INDEFINITE);
        progressUpdater.play();

        progressSlider.valueChangingProperty().addListener((obs, wasChanging, isChanging) -> {
            if (!isChanging) {
                long length = mediaPlayer.getLength();
                if (length > 0) {
                    long newTime = (long) ((progressSlider.getValue() / 100.0) * length);
                    mediaPlayer.seek(newTime);
                }
            }
        });

        progressSlider.setOnMouseReleased(e -> {
            long length = mediaPlayer.getLength();
            if (length > 0) {
                long newTime = (long) ((progressSlider.getValue() / 100.0) * length);
                mediaPlayer.seek(newTime);
            }
        });

        BorderPane root = new BorderPane();
        root.setCenter(songList.getNode());
        root.setBottom(playerToolbar.getNode());

        Scene scene = new Scene(root, 1000, 600);
        stage.setScene(scene);
        stage.setTitle("Cosmic Music Player");

        scene
            .getStylesheets()
            .add(getClass().getResource("/global.css").toExternalForm());

        stage.show();
    }

    private static String formatTime(long ms) {
        if (ms < 0) return "00:00";
        long totalSeconds = ms / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
        public void stop() {
            mediaPlayer.release();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
