package com.stellargear.cosmicplayer.services;

import java.io.File;

public class FileService {

    private File selectedFolder;
    private File[] storedSongs;

    public File[] returnStoredSongs() {
        return storedSongs;
    }
}
