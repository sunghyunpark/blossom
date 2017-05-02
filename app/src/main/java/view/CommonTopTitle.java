package view;

import android.view.ViewGroup;

/**
 * Created by sunghyun on 2017. 5. 2..
 * CommonTopTitle singleton
 * 공통 상단 타이틀
 */

public class CommonTopTitle {
    private static volatile CommonTopTitle commonTopTitle = null;

    private ViewGroup top_title;

    public static CommonTopTitle getInstance(){
        if(commonTopTitle == null)
            synchronized (CommonTopTitle.class){
                if(commonTopTitle==null){
                    commonTopTitle = new CommonTopTitle();
                }
            }

        return commonTopTitle;
    }

    public ViewGroup getTop_title() {
        return top_title;
    }

    public void setTop_title(ViewGroup top_title) {
        this.top_title = top_title;
    }


}
