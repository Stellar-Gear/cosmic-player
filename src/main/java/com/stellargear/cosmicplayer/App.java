package com.stellargear.cosmicplayer;

import com.stellargear.cosmicplayer.services.FileService;
import com.stellargear.cosmicplayer.services.PlayerService;
import com.stellargear.cosmicplayer.ui.PlayerToolbar;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App extends Application {

    PlayerService mediaPlayer = new PlayerService();
    FileService fileService = new FileService();
    PlayerToolbar playerToolbar = new PlayerToolbar();

    @Override
    public void start(Stage stage) {
        File folder = new File("/home/dimewolf/Música/");
        File[] files = folder.listFiles(File::isFile);

        ListView<String> list = new ListView<>();
        if (files != null) {
            Arrays.stream(files)
                .map(File::getName)
                .sorted(Comparator.naturalOrder())
                .forEach(list.getItems()::add);
        }

        playerToolbar.getPlayBtn().setOnAction(e -> {
            String selected = list.getSelectionModel().getSelectedItem();
            if (selected != null) mediaPlayer.playSong(
                new File(folder, selected), playerToolbar.getVolumeSlider().getValue()
            );
        });

        playerToolbar.getStopBtn().setOnAction(e -> mediaPlayer.pause());

        playerToolbar
            .getVolumeSlider()
            .valueProperty()
            .addListener((obs, old, val) -> {
                if (mediaPlayer != null) mediaPlayer.setVolume(
                    val.doubleValue()
                );
            });

        list.getSelectionModel()
            .selectedItemProperty()
            .addListener((obs, old, nuevo) -> {
                if (nuevo == null) return;
                File song = new File(folder, nuevo);
                var meta = fileService.readMetadata(song);
                playerToolbar.setSongInfo(meta.title(), meta.artist());
            });

        BorderPane root = new BorderPane();
        root.setCenter(list);
        root.setBottom(playerToolbar.getNode());

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Cosmic Music Player");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
