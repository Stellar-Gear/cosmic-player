package com.stellargear.cosmicplayer;

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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import com.stellargear.cosmicplayer.ui.SidePanel;

public class App extends Application {

    MediaPlayer mediaPlayer;

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
            String seleccionado = list.getSelectionModel().getSelectedItem();
            if (seleccionado == null) return;

            if (
                mediaPlayer != null &&
                mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED
            ) {
                mediaPlayer.play();
                return;
            }

            // Si es una canción nueva, crea nuevo MediaPlayer
            if (mediaPlayer != null) mediaPlayer.stop();
            Media media = new Media(
                new File(folder, seleccionado).toURI().toString()
            );
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
        });

        list.getSelectionModel()
            .selectedItemProperty()
            .addListener((obs, old, nuevo) -> {
                if (nuevo == null) return;

                try {
                    File archivo = new File(folder, nuevo);
                    AudioFile f = AudioFileIO.read(archivo);
                    Tag tag = f.getTag();
                    songName.setText(tag.getFirst(FieldKey.TITLE));
                    artistName.setText(tag.getFirst(FieldKey.ARTIST));
                } catch (Exception e) {
                    songName.setText(nuevo);
                    artistName.setText(nuevo);
                }
            });

        ToolBar playerBar = new ToolBar(
            songName,
            separator,
            artistName,
            playBtn,
            stopBtn
        );

        var sidePanel = new SidePanel();
        
        BorderPane root = new BorderPane();
        root.setCenter(list);
        root.setBottom(playerBar);
        root.setLeft(sidePanel.getNode());

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Cosmic Music Player");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
