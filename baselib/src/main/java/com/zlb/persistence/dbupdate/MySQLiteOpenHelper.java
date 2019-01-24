package com.zlb.persistence.dbupdate;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zlb.persistence.dbmaster.DaoMaster;
import com.zlb.persistence.dbmaster.SysAlertMess2Dao;
import com.zlb.persistence.dbmaster.SysAlertMessDao;

/**
 * FBI WARMING，如果新加的字段是int ,boolean,目前你需要修改为Integer,Boolean
 *
 * 当某张表需要添加一个int类型的列时，升级数据库则会报NOT NULL约束不通过，
 * 原因是 GreenDao建表时Int 类型 加了NOT NULL。而再数据转移的时候并没有Insert 该列
 *
 * INTEGER NOT NULL
 *
 * 数据库还是有很多问题
 *
 * Created by zenglb on 2017/1/3.
 */
public class MySQLiteOpenHelper extends DaoMaster.OpenHelper {

    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        MigrationHelper.migrate(db,

                SysAlertMess2Dao.class,
                SysAlertMessDao.class
        );  //升级

        if (oldVersion == newVersion) {
            Log.d("onUpgrade", "数据库是最新版本:" + oldVersion + "，    不需要升级");
            return;
        }

        Log.d("onUpgrade", "数据库从版本:" + oldVersion + " 升级到版本:" + newVersion);

    }
}