package ke.co.shofcosacco.app.api;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import ke.co.shofcosacco.app.api.requests.HashPinRequest;
import ke.co.shofcosacco.app.api.responses.CarouselResponse;
import ke.co.shofcosacco.app.api.responses.HashPinResponse;
import ke.co.shofcosacco.app.sharedprefs.SecurePrefs;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Singleton
public class ApiCarouselManager {
    private static final int READ_TIMEOUT = 60;
    private static final int CONNECT_TIMEOUT = 60;
    private static final int WRITE_TIMEOUT = 60;
    public static final String SERVER_URL = "https://bot.ubuniapps.com/aiportssacco/";

    private static final int NUMBER_OF_THREADS = 6;
    private static final ExecutorService apiExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private final RestApi api;
    private final SecurePrefs securePrefs;
    private final Application application;

    @Inject
    public ApiCarouselManager(SecurePrefs securePrefs, Application application) {
        this.securePrefs = securePrefs;
        this.application = application;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);


        builder.addInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder()
                    .method(original.method(), original.body());

            Request request = requestBuilder.build();
            return chain.proceed(request);
        });


        OkHttpClient client = builder.build();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(SERVER_URL)
                .client(client)
                .build();

        api = retrofit.create(RestApi.class);
    }

    public static void execute(Runnable runnable) {
        apiExecutor.execute(runnable);
    }


    public APIResponse<CarouselResponse> getCarousels() throws IOException {
        return new APIResponse<>(api.getCarousels().execute());
    }

    public APIResponse<HashPinResponse> hashPin(String phoneNumber, String pin, String newPin) throws IOException {
        HashPinRequest request = new HashPinRequest();
        request.phone= phoneNumber;
        request.pin = pin;
        request.newPin = newPin;
        return new APIResponse<>(api.hashPin(request).execute());
    }


}
