package com.shilpakala.showcase;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.getcapacitor.BridgeActivity;
import com.getcapacitor.BridgeWebChromeClient;

public class MainActivity extends BridgeActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Enable WebView debugging for easier troubleshooting
        WebView.setWebContentsDebuggingEnabled(true);
        
        if (getIntent() != null) {
            setIntent(getIntent());
        }

        // Initialize WebView configuration after bridge is ready
        setupWebView();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        syncCookies();
    }

    private void setupWebView() {
        if (getBridge() != null && getBridge().getWebView() != null) {
            WebView webView = getBridge().getWebView();
            WebSettings settings = webView.getSettings();
            
            // Core script and storage permissions
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setDatabaseEnabled(true);
            
            // IMPORTANT: setSupportMultipleWindows(true) allows the app to handle window.open()
            // which is commonly used by Google Sign-In popups.
            settings.setSupportMultipleWindows(true); 
            settings.setJavaScriptCanOpenWindowsAutomatically(true);
            
            // Modern web app compatibility
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            settings.setAllowFileAccess(true);
            settings.setAllowContentAccess(true);
            settings.setAllowFileAccessFromFileURLs(true);
            settings.setAllowUniversalAccessFromFileURLs(true);
            
            // Rendering performance
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
            settings.setLoadWithOverviewMode(true);
            settings.setUseWideViewPort(true);

            // Fix User Agent to prevent Google from blocking login in WebView
            // Removing "; wv" from the User Agent makes it look like a regular mobile browser
            String userAgent = settings.getUserAgentString();
            if (userAgent != null) {
                settings.setUserAgentString(userAgent.replace("; wv", ""));
            }

            // Custom WebChromeClient to handle the "small popup" for Google Sign-In
            // Extending BridgeWebChromeClient ensures that Capacitor's internal logic
            // (like plugin communication, logs, etc.) remains functional.
            webView.setWebChromeClient(new BridgeWebChromeClient(getBridge()) {
                @Override
                public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                    final Dialog dialog = new Dialog(MainActivity.this, android.R.style.Theme_Black_NoTitleBar);
                    WebView newWebView = new WebView(MainActivity.this);
                    
                    WebSettings newSettings = newWebView.getSettings();
                    newSettings.setJavaScriptEnabled(true);
                    newSettings.setSupportMultipleWindows(true);
                    newSettings.setJavaScriptCanOpenWindowsAutomatically(true);
                    newSettings.setDomStorageEnabled(true);
                    
                    // Apply the same User Agent fix to the popup WebView
                    String popupUA = newSettings.getUserAgentString();
                    if (popupUA != null) {
                        newSettings.setUserAgentString(popupUA.replace("; wv", ""));
                    }

                    dialog.setContentView(newWebView);
                    Window window = dialog.getWindow();
                    if (window != null) {
                        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    }
                    dialog.show();

                    newWebView.setWebChromeClient(new WebChromeClient() {
                        @Override
                        public void onCloseWindow(WebView window) {
                            dialog.dismiss();
                        }
                    });

                    newWebView.setWebViewClient(new WebViewClient() {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            // Check if the URL is the custom scheme redirect (returning to the app)
                            if (url.startsWith("com.shilpakala.showcase://")) {
                                dialog.dismiss();
                                // Pass the intent to the main activity to handle the auth response
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                onNewIntent(intent);
                                return true; 
                            }
                            return false;
                        }
                    });

                    WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                    transport.setWebView(newWebView);
                    resultMsg.sendToTarget();
                    return true;
                }
            });
            
            syncCookies();
        }
    }

    private void syncCookies() {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if (getBridge() != null && getBridge().getWebView() != null) {
            cookieManager.setAcceptThirdPartyCookies(getBridge().getWebView(), true);
        }
        cookieManager.flush();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (getBridge() != null) {
            getBridge().onNewIntent(intent);
        }
        syncCookies();
    }
}
