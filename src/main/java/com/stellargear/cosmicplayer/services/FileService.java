package com.stellargear.cosmicplayer.services;

import java.io.File;
import java.util.Comparator;
import java.util.Arrays;
import java.util.List;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

public class FileService {

    public List<File> getSongFiles(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles(File::isFile);
        if (files == null) return List.of();
        return Arrays.stream(files)
            .sorted(Comparator.comparing(File::getName))
            .toList();
    }

    public List<String> getSongNames(String folderPath) {
        return getSongFiles(folderPath).stream().map(File::getName).toList();
    }

    public SongMetadata readMetadata(File file) {
        try {
            AudioFile f = AudioFileIO.read(file);
            Tag tag = f.getTag();
            String title = tag.getFirst(FieldKey.TITLE);
            String artist = tag.getFirst(FieldKey.ARTIST);
            return new SongMetadata(title, artist);
        } catch (Exception e) {
            return new SongMetadata(file.getName(), file.getName());
        }
    }

    public record SongMetadata(String title, String artist) {}
}
