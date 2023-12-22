package net.fkm.cloudmusictest.model;

public class MusicModel extends BaseModel {

    private String musicId; // 音乐标识符
    private String name; // 音乐名称
    private String poster; // 音乐封面图片
    private String path; // 音乐文件图片
    private String author; // 音乐作者
    private String remark; // 音乐描述

    public String getMusicId() {
        return musicId;
    }

    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
