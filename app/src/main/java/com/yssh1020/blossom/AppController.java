package com.yssh1020.blossom;

public class AppController {


    private static volatile  AppController appController = null;
    private int DISPLAY_WIDTH;    // 디스플레이 폭
    private int DISPLAY_HEIGHT;    // 디스플레이 높이
    private String server_base_ip;    // 서버 베이스 주소
    private String server_img_path;    // 서버 이미지 경로
    private int Article_Bg_Cnt;    //백그라운드 이미지 갯수

    public static  AppController getInstance(){
        if(appController == null)
            synchronized (AppController.class){
                if(appController==null){
                    appController = new AppController();
                }
            }

        return appController;
    }

    public int getDISPLAY_HEIGHT() {
        return DISPLAY_HEIGHT;
    }

    public void setDISPLAY_HEIGHT(int DISPLAY_HEIGHT) {
        this.DISPLAY_HEIGHT = DISPLAY_HEIGHT;
    }

    public int getDISPLAY_WIDTH() {
        return DISPLAY_WIDTH;
    }

    public void setDISPLAY_WIDTH(int DISPLAY_WIDTH) {
        this.DISPLAY_WIDTH = DISPLAY_WIDTH;
    }

    public String getServer_base_ip() {
        return server_base_ip;
    }

    public void setServer_base_ip(String server_base_ip) {
        this.server_base_ip = server_base_ip;
    }

    public String getServer_img_path() {
        return server_img_path;
    }

    public void setServer_img_path(String server_img_path) {
        this.server_img_path = server_img_path;
    }

    public int getArticle_Bg_Cnt() {
        return Article_Bg_Cnt;
    }

    public void setArticle_Bg_Cnt(int article_Bg_Cnt) {
        Article_Bg_Cnt = article_Bg_Cnt;
    }

}