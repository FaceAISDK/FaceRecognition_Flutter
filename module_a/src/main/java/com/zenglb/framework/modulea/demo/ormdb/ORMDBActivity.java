package com.zenglb.framework.modulea.demo.ormdb;

import android.os.Bundle;

import com.zenglb.framework.modulea.R;
import com.zlb.base.BaseActivity;
import com.zlb.base.BaseMVPActivity;
import com.zlb.persistence.dbmaster.DaoSession;
import com.zlb.persistence.entity.SysAlertMess;
import com.zlb.persistence.entity.SysAlertMess2;

import javax.inject.Inject;

//*   `insert()` adds Entity into Table, assumes Entity with that Key does not exist. If exists, it shall throw Exception
//
//*   `insertOrReplace()` adds Entity into Table if Key does not exist, replaces if Key exists.
//
//*   `save()`(https://github.com/greenrobot/greenDAO/issues/521#issuecomment-309757364) 
//*    adds Entity which has no Key into Table, updates if it has Key and Entity exists on Table. If Entity has Key and does not exist on Table, it will do nothing.

//     insertOrReplace ： 传入的对象在数据库中，有则更新无则插入。推荐同步数据库时使用该方法。
//     save 类似于insertOrReplace，区别在于save会判断传入对象的key，有key的对象执行更新，无key的执行插入。当对象有key但并不在数据库时会执行失败.适用于保存本地列表

/**
 * 测试代码，所有ORMDB 的操作在这里进行测试通过
 *
 */
public class ORMDBActivity extends BaseMVPActivity {
    @Inject
    DaoSession daoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        testIDNOTAuto();
        testIDAuto();
    }


    @Override
    protected void initViews() {
        setToolBarTitle("ORMDB TEST");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ormdb;
    }

    /**
     * 测试key NOT自动
     */
    private void testIDNOTAuto(){
        //这样子是没有数据的
        SysAlertMess sysAlertMess=new SysAlertMess(1L,"title1","actionType1","message1","detailURL1");
        daoSession.getSysAlertMessDao().saveInTx(sysAlertMess);
    }


    /**
     * 测试key 自动+
     */
    private void testIDAuto(){
        SysAlertMess2 sysAlertMess2=new SysAlertMess2("title1","actionType1","message1","detailURL1");
        daoSession.getSysAlertMess2Dao().saveInTx(sysAlertMess2);
    }


}
