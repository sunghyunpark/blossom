package db;

import android.content.Context;
import android.util.Log;

import db.model.UserData;
import io.realm.Realm;
import model.User;

/**
 * Created by sunghyun on 2017. 5. 17..
 */

public class RealmUtil {

    public void InsertDB(Context context, String uid, String email, String profile_img, String birth, String gender,
                         String token, String created_at, int seed_cnt, String bg_title){

        Realm mRealm;
        RealmConfig realmConfig;
        realmConfig = new RealmConfig();
        mRealm = Realm.getInstance(realmConfig.User_DefaultRealmVersion(context));

        mRealm.beginTransaction();
        UserData userData = new UserData();
        userData.setNo(1);
        userData.setUid(uid);
        userData.setEmail(email);
        userData.setProfile_img(profile_img);
        userData.setBirth(birth);
        userData.setGender(gender);
        userData.setToken(token);
        userData.setCreated_at(created_at);
        userData.setSeed_cnt(seed_cnt);
        userData.setBg_title(bg_title);
        mRealm.copyToRealmOrUpdate(userData);
        mRealm.commitTransaction();

        RefreshUserInfo(context, uid);

    }

    public void RefreshUserInfo(Context context, String uid){
        Realm mRealm;
        RealmConfig realmConfig;
        realmConfig = new RealmConfig();
        mRealm = Realm.getInstance(realmConfig.User_DefaultRealmVersion(context));

        UserData user = mRealm.where(UserData.class).equalTo("uid",uid).findFirst();

        User.getInstance().setUid(user.getUid());
        User.getInstance().setEmail(user.getEmail());
        User.getInstance().setGender(user.getGender());
        User.getInstance().setBirth(user.getBirth());
        User.getInstance().setProfile_img(user.getProfile_img());
        User.getInstance().setToken(user.getToken());
        User.getInstance().setCreated_at(user.getCreated_at());
        User.getInstance().setSeed_cnt(String.valueOf(user.getSeed_cnt()));
        User.getInstance().setBg_title(user.getBg_title());

        Log.d("UserInfo", "UserUid : "+user.getUid()+"");
        Log.d("UserInfo", "UserEmail : "+user.getEmail());
        Log.d("UserInfo", "UserBirth : "+user.getBirth());
        Log.d("UserInfo", "UserGender : "+user.getGender());
        Log.d("UserInfo", "Created_at : "+user.getCreated_at());
        Log.d("UserInfo", "Profile_img : "+user.getProfile_img());
        Log.d("UserInfo", "Push_Token : "+user.getToken());
        Log.d("UserInfo", "Seed_Cnt : "+user.getSeed_cnt());
        Log.d("UserInfo", "Bg_Title : "+user.getBg_title());

    }

    public void UpdateUserSeedCnt(Context context, String uid, String seed_cnt){
        Realm mRealm;
        RealmConfig realmConfig;
        realmConfig = new RealmConfig();

        mRealm = Realm.getInstance(realmConfig.User_DefaultRealmVersion(context));
        UserData user_db = mRealm.where(UserData.class).equalTo("no",1).findFirst();
        try{
            mRealm.beginTransaction();
            user_db.setSeed_cnt(Integer.parseInt(seed_cnt));
        }catch (Exception e){

        }finally {
            mRealm.commitTransaction();
            RefreshUserInfo(context, uid);
        }
    }
}
