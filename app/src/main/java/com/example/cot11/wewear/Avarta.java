package com.example.cot11.wewear;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

/**
 * Created by 이언우 on 2017-03-13.
 */

public class Avarta extends Fragment {
    public Avarta() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.avarta_fragment, container, false);
        WebView webView = (WebView)v.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://www.naver.com/");
        //TextView local = (TextView) v.findViewById(R.id.Avarta_tv);

        return v;
    }
}
