package com.example.khanh.bai1;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends Activity {
    private TextView textView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.TextView01);
    }

    public void readWebpage(View view) {
        DownloadWebPageTask task = new DownloadWebPageTask(textView);
        task.execute(new String[]{"http://www.fithou.edu.vn"});
    }

    class DownloadWebPageTask extends AsyncTask<String, Void, String> {

        TextView textView;
        public DownloadWebPageTask(TextView tv) {
            textView = tv;
        }

        @Override
        protected String doInBackground(String... urls) {
            String response = "", s = "";
            for (String url : urls) {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                try {
                    InputStream content = client.execute(httpGet).getEntity().getContent();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    while ((s = buffer.readLine()) != null)
                        response += s;
                } catch (Exception e) {
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
//            TextView textView = (TextView) findViewById(R.id.TextView01);
            textView.setMovementMethod(new ScrollingMovementMethod());

//            textView.setText(result);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                textView.setText(Html.fromHtml(result,Html.FROM_HTML_MODE_LEGACY));
            } else {
                textView.setText(Html.fromHtml(result));
            }
        }
    }
}