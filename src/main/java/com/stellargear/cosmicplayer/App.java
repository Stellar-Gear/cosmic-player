package com.stellargear.cosmicplayer;

import com.stellargear.cosmicplayer.services.FileService;
import com.stellargear.cosmicplayer.services.PlayerService;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App extends Application {

    PlayerService mediaPlayer = new PlayerService();
    FileService fileService = new FileService();

    @Override
    public void start(Stage stage) {
        Label songName = new Label("Song Name");
        Label separator = new Label(" - ");
        Label artistName = new Label("Artist");

        File folder = new File("/home/dimewolf/Música/");
        File[] files = folder.listFiles(File::isFile);

        ListView<String> list = new ListView<>();
        if (files != null) {
            Arrays.stream(files)
                .map(File::getName)
                .sorted(Comparator.naturalOrder())
                .forEach(list.getItems()::add);
        }

        Button playBtn = new Button("Reproducir");

        Button stopBtn = new Button("Detener");
        stopBtn.setOnAction(e -> {
            if (mediaPlayer != null) mediaPlayer.pause();
        });

        playBtn.setOnAction(e -> {
            String selected = list.getSelectionModel().getSelectedItem();
            if (selected != null) mediaPlayer.playSong(
                new File(folder, selected)
            );
        });

        list.getSelectionModel()
            .selectedItemProperty()
            .addListener((obs, old, nuevo) -> {
                if (nuevo == null) return;
                File song = new File(folder, nuevo);
                var meta = fileService.readMetadata(song);
                songName.setText(meta.title());
                artistName.setText(meta.artist());
            });

        ToolBar playerBar = new ToolBar(
            songName,
            separator,
            artistName,
            playBtn,
            stopBtn
        );

        BorderPane root = new BorderPane();
        root.setCenter(list);
        root.setBottom(playerBar);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Cosmic Music Player");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
