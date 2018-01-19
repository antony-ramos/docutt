package fr.utt.if26.projetx;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;

public class HttpUtils {
    private static final String BASE_URL = "http://docutt-backend.eu-west-1.elasticbeanstalk.com/";
    private static final AsyncHttpClient client = new AsyncHttpClient();

    public static void get(final String url, final RequestParams params, final AsyncHttpResponseHandler responseHandler) {
        client.removeAllHeaders();

        Task<GetTokenResult> task = FirebaseAuth.getInstance().getCurrentUser().getToken(true);
        task.addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                client.addHeader("Authorization", task.getResult().getToken());
                client.get(getAbsoluteUrl(url), params, responseHandler);
            }
        });
    }

    public static void post(final Context context, final String url, final Header[] headers, final HttpEntity entity,
                                    final ResponseHandlerInterface responseHandler) {
        client.removeAllHeaders();

        Task<GetTokenResult> task = FirebaseAuth.getInstance().getCurrentUser().getToken(true);
        task.addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                Log.d("coucou", task.getResult().getToken());
                client.addHeader("Authorization", task.getResult().getToken());
                client.post(context, getAbsoluteUrl(url), headers, entity, "application/json", responseHandler);
            }
        });
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}