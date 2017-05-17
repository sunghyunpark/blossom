package db;

import android.content.Context;

import db.model.UserData;
import io.realm.Realm;
import model.User;

/**
 * Created by sunghyun on 2017. 5. 17..
 */

public class RealmUtil {

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
