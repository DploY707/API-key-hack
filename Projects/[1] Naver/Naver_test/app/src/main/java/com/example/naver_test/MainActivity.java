package com.example.naver_test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.searchBtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView searchText = (TextView) findViewById(R.id.searchText);
                String keyword = searchText.getText().toString();

                searchNaver(keyword);
            }
        });
    }

    String TAG = "N_text";

    public void searchNaver(final String searchObject) { // 검색어 = searchObject로 ;
        final String clientId = "pcs6neuWISbVB8JhJqA9";//애플리케이션 클라이언트 아이디값", 등록되어있는 값;
        final String clientSecret = "gHufl5Z8H1";//애플리케이션 클라이언트 시크릿값";
        final int display = 5; // 보여지는 검색결과의 수

        // 네트워크 연결은 Thread 생성 필요
        new Thread() {

            @Override
            public void run() {
                try {
                    String text = URLEncoder.encode(searchObject, "UTF-8");
                    String apiURL = "https://openapi.naver.com/v1/search/blog?query=" + text; // json 결과
                    // Json 형태로 결과값을 받아옴.
                    URL url = new URL(apiURL);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("X-Naver-Client-Id", clientId);
                    con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                    con.connect();

                    int responseCode = con.getResponseCode();

                    BufferedReader br;

                    if(responseCode==200) { // 정상 호출
                        br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    } else {  // 에러 발생
                        br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    }

                    StringBuilder searchResult = new StringBuilder();
                    String inputLine;
                    while ((inputLine = br.readLine()) != null) {
                        searchResult.append(inputLine + "\n");
                    }

                    br.close();
                    con.disconnect();

                    String data = searchResult.toString();

                    TextView searchResult2 = (TextView) findViewById(R.id.searchResult2);
                    searchResult2.setText(data);

                } catch (Exception e) {
                    Log.d(TAG, "error : " + e);
                }
            }
        }.start();

    }
}
