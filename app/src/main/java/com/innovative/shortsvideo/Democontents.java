package com.innovative.shortsvideo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Democontents {
    public static List<Video> getReels() {
        List<Video> reels = new ArrayList<>();
        reels.add(new Video("https://dev.digicean.com/storage/255030357_230359622521878_8463246573248024146_n.mp4"));
       // reels.add(new Video("https://dev.digicean.com/storage/247062863_341024567823973_5333055093789086258_n.mp4"));
        reels.add(new Video("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"));
        reels.add(new Video("https://dev.digicean.com/storage/241681828_894992998067015_8734076864715268332_n.mp4"));
        reels.add(new Video("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"));
        reels.add(new Video("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"));
        return reels;
    }
}
