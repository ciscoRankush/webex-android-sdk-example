/*
 * Copyright 2016-2017 Cisco Systems Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */

package com.ciscowebex.androidsdk.kitchensink.actions.commands;

import com.ciscowebex.androidsdk.CompletionHandler;
import com.ciscowebex.androidsdk.Result;
import com.ciscowebex.androidsdk.Webex;
import com.ciscowebex.androidsdk.auth.JWTAuthenticator;
import com.ciscowebex.androidsdk.auth.OAuthTestUserAuthenticator;
import com.ciscowebex.androidsdk.kitchensink.BuildConfig;
import com.ciscowebex.androidsdk.kitchensink.actions.IAction;
import com.ciscowebex.androidsdk.kitchensink.actions.WebexAgent;
import com.ciscowebex.androidsdk.kitchensink.KitchenSinkApp;
import com.ciscowebex.androidsdk.kitchensink.actions.events.LoginEvent;
import com.github.benoitdion.ln.Ln;

/**
 * Created on 19/09/2017.
 */

public class AppIdLoginAction implements IAction {
    private String jwt;

    public AppIdLoginAction(String jwt) {
        this.jwt = jwt;
    }

    @Override
    public void execute() {
//        JWTAuthenticator jwtAuthenticator = new JWTAuthenticator();
//        Webex webex = new Webex(KitchenSinkApp.getApplication(), jwtAuthenticator);
//        WebexAgent.getInstance().setWebex(webex);
//        jwtAuthenticator.authorize(jwt);
//        new RegisterAction(jwtAuthenticator).execute();

        OAuthTestUserAuthenticator oAuth2;
        String password = Integer.valueOf(jwt) <= 10 ? "Test123@cisco" : "Test(123)";
        jwt = "sparksdktestuser"+jwt+"@tropo.com";
        oAuth2 = new OAuthTestUserAuthenticator(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SEC, BuildConfig.SCOPE,BuildConfig.REDIRECT_URL,jwt,jwt,password);
        Webex webex = new Webex(KitchenSinkApp.getApplication(), oAuth2);
        WebexAgent.getInstance().setWebex(webex);
        oAuth2.authorize(new CompletionHandler<Void>() {
            @Override
            public void onComplete(Result<Void> result) {
                Ln.d("====Spark ID login complated:"+ result.isSuccessful()+"====");
                if (result.isSuccessful()) {
                    new RegisterAction(oAuth2).execute();
                } else {
                    new LoginEvent(result).post();
                }
            }
        });
    }
}