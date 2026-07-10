package com.stellargear.cosmicplayer.ui;

import com.stellargear.cosmicplayer.services.PlayerService.Song;

import javafx.scene.control.ListCell;

public class SongCell extends ListCell<Song> {
    @Override
    protected void updateItem(Song song, boolean empty) {
        super.updateItem(song, empty);
        if (empty || song == null) {
            setText(null);
            setGraphic(null);
        } else {
            SongListItem item = new SongListItem(song.title(), song.artist(), song.duration());
            setGraphic(item.getNode());
        }
    }
}
