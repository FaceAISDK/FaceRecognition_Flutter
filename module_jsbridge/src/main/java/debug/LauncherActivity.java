package debug;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zlb.base.BaseWebViewActivity;
import com.zenglb.framework.jsbridge.WebActivity;

/**
 *
 *
 */
public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //在这里传值给需要调试的Activity
        Intent intent = new Intent(this, WebActivity.class);
//        intent.putExtra(BaseWebViewActivity.URL, "https://www.baidu.com");
        intent.putExtra(BaseWebViewActivity.URL, "file:///android_asset/index.html");

        startActivity(intent);
        finish();

    }

}
