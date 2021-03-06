package db;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmConfig{

    public RealmConfiguration User_DefaultRealmVersion(Context context){

        Realm.init(context);    //realm 초기화
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("User.realm")
                .schemaVersion(1)
                //.deleteRealmIfMigrationNeeded()
                .migration(new Migration())
                .build();

        return config;

    }
}


