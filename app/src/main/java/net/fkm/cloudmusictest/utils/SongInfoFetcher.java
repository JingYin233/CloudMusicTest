package net.fkm.cloudmusictest.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SongInfoFetcher {
    public List<Map<String, String>> fetchSongInfo(String keyword) throws Exception {
        String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8.toString());
        String urlString = "https://dataiqs.com/api/netease/music?msg=" + encodedKeyword;

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        String output;
        StringBuilder response = new StringBuilder();
        while ((output = br.readLine()) != null) {
            response.append(output);
        }

        JSONObject jsonObject = new JSONObject(response.toString());
        JSONArray dataArray = jsonObject.getJSONArray("data");

        List<Map<String, String>> songInfos = new ArrayList<>();
        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject songObject = dataArray.getJSONObject(i);
            Integer id = songObject.getInt("id");
            String name = songObject.getString("name");
            String singername = songObject.getString("singername");

            String songUrl = "http://music.163.com/song/media/outer/url?id=" + id + ".mp3";

            // 获取封面图片URL
            String coverImgUrl = getCoverImgUrl(id);

            Map<String, String> songInfo = new HashMap<>();
            songInfo.put("id", id.toString());
            songInfo.put("name", name);
            songInfo.put("singername", singername);
            songInfo.put("songUrl", songUrl);
            songInfo.put("coverImgUrl", coverImgUrl);

            songInfos.add(songInfo);
        }

        conn.disconnect();

        return songInfos;
    }

    private String getCoverImgUrl(Integer songId) throws IOException {
        String url = "https://music.163.com/song?id=" + songId;
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.83 Safari/537.36")
                .get();

        Elements imgElements = doc.select("img");
        for (org.jsoup.nodes.Element img : imgElements) {
            String dataSrc = img.attr("data-src");
            if (dataSrc != null && !dataSrc.isEmpty()) {
                return dataSrc;
            }
        }

        return null;
    }
}
