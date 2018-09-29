package com.zlb.http.result;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;


/**
 * 模拟两个字段去重
 */
@Entity(indexes = {
        @Index(value = "customerCode,userId", unique = true)
})
public class TestResult {

    @Index
    private String fullname;//from customer_result t1


    private String customerCode;//null  test
    private int userId;//from customer_result t1

    private boolean isMultiHouse=false;

    @Generated(hash = 861151746)
    public TestResult(String fullname, String customerCode, int userId, boolean isMultiHouse) {
        this.fullname = fullname;
        this.customerCode = customerCode;
        this.userId = userId;
        this.isMultiHouse = isMultiHouse;
    }

    @Generated(hash = 808538852)
    public TestResult() {
    }


    public boolean isMultiHouse() {
        return isMultiHouse;
    }

    public void setMultiHouse(boolean multiHouse) {
        isMultiHouse = multiHouse;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    /**
     * 重新hashCode
     *
     * @return
     */
    @Override
    public int hashCode() {

        String multiValue=userId+customerCode;

        return multiValue.hashCode();
    }


    /**
     * 重写equal
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TestResult){
            return (userId+customerCode).equals(((TestResult) obj).userId+((TestResult) obj).customerCode);
        }
        return super.equals(obj);
    }

    public boolean getIsMultiHouse() {
        return this.isMultiHouse;
    }

    public void setIsMultiHouse(boolean isMultiHouse) {
        this.isMultiHouse = isMultiHouse;
    }
}
