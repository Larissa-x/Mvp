package com.larissa.mvp.http;

import android.content.Context;
import com.larissa.mvp.bean.ParamNames;
import com.larissa.mvp.utils.LogI;
import com.larissa.mvp.utils.NetworkUtils;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * HttpUtils
 * Created by laorencel on 2017/8/19.
 */

public class HttpUtils {

    private static HttpUtils mInstance;
    private Context mContext;
    private Gson mGson;
    private boolean isDebug;

    private Object defaultHttps;
    private Object mDianDianDriverHttps;
    private Object mDianDianClientHttps;
    private final static String api_default = "";
    private Map<String, String> header;

    public static HttpUtils getInstance() {
        if (mInstance == null) {
            synchronized (HttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new HttpUtils();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context, boolean isDebug) {
        this.mContext = context;
        this.isDebug = isDebug;
//        HttpHead.init(context);
    }

    public <T> T getDefaultServer(Class<T> apiServer) {
        if (defaultHttps == null) {
            defaultHttps = getBuilder(api_default).build().create(apiServer);
        }
        return (T) defaultHttps;
    }

    public <T> T driverServer(String api, Class<T> apiServer) {
        if (mDianDianDriverHttps == null) {
            mDianDianDriverHttps = getBuilder(api).build().create(apiServer);
        }
        return (T) mDianDianDriverHttps;
    }

    public <T> T clientServer(String api, Class<T> apiServer) {
        if (mDianDianClientHttps == null) {
            mDianDianClientHttps = getBuilder(api).build().create(apiServer);
        }
        return (T) mDianDianClientHttps;
    }

    public Retrofit.Builder getBuilder(String apiUrl) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(getOkHttpClient())
                .baseUrl(apiUrl)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        return builder;
    }

    public void setHeader(Map<String,String>map){
        header = map;
    }

    public Gson getGson() {
        if (mGson == null) {
            GsonBuilder builder = new GsonBuilder();
            builder.setLenient()
                    .setFieldNamingStrategy(new AnnotateNaming())
                    .serializeNulls();
            mGson = builder.create();
        }
        return mGson;
    }

    class AnnotateNaming implements FieldNamingStrategy {

        @Override
        public String translateName(Field f) {
            ParamNames names = f.getAnnotation(ParamNames.class);
            return names != null ? names.value() : FieldNamingPolicy.IDENTITY.translateName(f);
        }
    }

    public OkHttpClient getOkHttpClient() {
        OkHttpClient client1;
        client1 = getUnsafeOkHttpClient();
        return client1;
    }

    public static int CONNECT_TIMEOUT = 10;
    public static int READ_TIMEOUT = 20;
    public static int WRITE_TIMEOUT = 20;

    public OkHttpClient getUnsafeOkHttpClient() {
        try {
            TrustManager[] trustAllCertSManagers = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }
            }};
            // Install the all-trusting trust manager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCertSManagers, new SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
            okBuilder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .addInterceptor(new HttpHeadInterceptor())
                    .addInterceptor(getLogInterceptor())
                    .sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    });
            return okBuilder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    class HttpHeadInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder builder = request.newBuilder();
            builder.addHeader("Accept","*/*");
            if (NetworkUtils.isConnected(mContext)) {
                int maxAge = 60;
                builder.addHeader("Cache-Control", "public, max-age=" + maxAge);
            } else {
                int maxStale = 60 * 60 * 24 * 28;
                builder.addHeader("Cache-Control", "public, only-if-cached, max-stale=" + maxStale);
            }
            if (header != null) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (key != null && value != null)
                        builder.addHeader(key, value);
                }
            }
            return chain.proceed(builder.build());
        }
    }

    private HttpLoggingInterceptor getLogInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                LogI.h(message);
            }
        });
        if (isDebug) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        return interceptor;
    }
}
