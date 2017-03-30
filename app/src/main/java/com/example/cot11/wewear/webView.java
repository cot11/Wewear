package com.example.cot11.wewear;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class webView extends Fragment {

    private boolean run = true;
    WebView webView;
    String URL = "";
    String Brand = "";
    View v;

    public webView() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(getArguments() != null)
        {
            URL = getArguments().getString("Link");
            Brand  = getArguments().getString("Brand");
        }

        v =  inflater.inflate(R.layout.webview, container, false);
        webView = (WebView)v.findViewById(R.id.webview);
        webView.getSettings().getJavaScriptEnabled();
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl(URL);

        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK)
                {
                    ((AvartaMain) getActivity()).backtoFrag(Brand);
                    return  true;
                }
                else
                {
                    return false;
                }

            }
        });

        return v;
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {super.onPageStarted(view, url, favicon);
            if(run)
            {
                //((AvartaMain) getActivity()).ProgressRun();
                run = false;
            }

        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //((AvartaMain) getActivity()).ProgressStop();
            run = true;
        }
    }
}

