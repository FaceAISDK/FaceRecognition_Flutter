package com.zenglb.framework.modulea.demo.demo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zenglb.framework.modulea.R;
import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 应该重点再去了解熟悉链式布局
 *
 */
public class ConstraintLayoutActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<String> picUrlList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constraint_layout);

        picUrlList.add("http://e.hiphotos.baidu.com/image/h%3D300/sign=237e8385972f070840052c00d926b865/d8f9d72a6059252d70828cce3d9b033b5ab5b967.jpg");
        picUrlList.add("http://b.hiphotos.baidu.com/image/h%3D300/sign=5c0f345843540923b569657ea259d1dc/dcc451da81cb39db6743ad2dda160924aa1830ce.jpg");
        picUrlList.add("http://b.hiphotos.baidu.com/image/h%3D300/sign=0a54c9f99b45d688bc02b4a494c07dab/4b90f603738da977f0bb86e1b951f8198718e360.jpg");
        picUrlList.add("http://c.hiphotos.baidu.com/image/h%3D300/sign=36e4a7bfdf88d43fefa997f24d1cd2aa/f703738da977391279fc85abf1198618377ae226.jpg");
        picUrlList.add("http://b.hiphotos.baidu.com/image/h%3D300/sign=b80c7f6aff03738dc14a0a22831ab073/08f790529822720e0dfee23072cb0a46f31fabc3.jpg");
        picUrlList.add("http://c.hiphotos.baidu.com/image/h%3D300/sign=5db33b34753e6709a10043ff0bc79fb8/faedab64034f78f00d035de073310a55b3191c9d.jpg");

        picUrlList.add("http://c.hiphotos.baidu.com/image/h%3D300/sign=36e4a7bfdf88d43fefa997f24d1cd2aa/f703738da977391279fc85abf1198618377ae226.jpg");
        picUrlList.add("http://b.hiphotos.baidu.com/image/h%3D300/sign=b80c7f6aff03738dc14a0a22831ab073/08f790529822720e0dfee23072cb0a46f31fabc3.jpg");
        picUrlList.add("http://c.hiphotos.baidu.com/image/h%3D300/sign=5db33b34753e6709a10043ff0bc79fb8/faedab64034f78f00d035de073310a55b3191c9d.jpg");

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,9);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(new TestAdapter(this, picUrlList));
    }


    /**
     * 测试的Adapter
     *
     */
    public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {
        private int checkedIndex = -1;
        private Context mContext;
        private LayoutInflater mLayoutInflater;
        private List<String> data = new ArrayList<>();


        /**
         * @param mContext
         * @param data
         */
        public TestAdapter(Context mContext, List<String> data) {
            this.mContext = mContext;
            mLayoutInflater = LayoutInflater.from(mContext);
            this.data = data;
        }

        //==================================== RecyclerView 更新封装完毕 =================================================
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.group_member_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
            Log.e("TAG",data.get(position));

//            ImageLoader.getInstance().displayImage(data.get(position), viewHolder.imageView, options);
//            viewHolder.imageView.setImageResource(R.drawable.logo);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private View itemView;
            private CircleImageView imageView;

            public ViewHolder(View itemView) {
                super(itemView);
                this.itemView = itemView;
                imageView = (CircleImageView) itemView.findViewById(R.id.head_image);
            }
        }

    }

}
