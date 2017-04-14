package event;

public class SelectArticleBGEvent
{

    private String imgPath;
    public SelectArticleBGEvent(String imgPath){
        this.imgPath = imgPath;

    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}

//BusProvider.getInstance().register(this); -> 받는 액티비티에 선언(oncreate에 넣어줌)
/*@Override -> 받는 액티비티에 선언
protected void onDestroy() {
	// Always unregister when an object no longer should be on the bus.
	BusProvider.getInstance().unregister(this);
	super.onDestroy();
}
	@Subscribe
	public void FinishLoad(Register_ProfilePushEvent mPushEvent) {
		Toast.makeText(getApplicationContext(), "오또성공", Toast.LENGTH_SHORT).show();
		club_intro = mPushEvent.getList();
// 이벤트가 발생한뒤 수행할 작업
	}*/

//BusProvider.getInstance().post(new Register_ProfilePushEvent(intro_text)); -> 이벤트를 보내는 엑티비티에 선언