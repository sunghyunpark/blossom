package view;

import android.view.ViewGroup;

/**
 * Created by sunghyun on 2017. 5. 2..
 * CommonTabMenu singleton
 * 공통 하단 탭 메뉴
 */

public class CommonTabMenu {
    private static volatile CommonTabMenu commonTabMenu = null;

    private ViewGroup bottom_menu;

    public static CommonTabMenu getInstance(){
        if(commonTabMenu == null)
            synchronized (CommonTabMenu.class){
                if(commonTabMenu==null){
                    commonTabMenu = new CommonTabMenu();
                }
            }

        return commonTabMenu;
    }

    public ViewGroup getBottom_menu() {
        return bottom_menu;
    }

    public void setBottom_menu(ViewGroup bottom_menu) {
        this.bottom_menu = bottom_menu;
    }


}
