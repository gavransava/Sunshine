package com.example.savagavran.sunshine;


import android.app.Activity;
import android.test.InstrumentationTestCase;

import com.example.savagavran.sunshine.data.WeatherResponse;
import com.example.savagavran.sunshine.sync.ApiInterface;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.HttpURLConnection;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

public class MainActivityTest extends InstrumentationTestCase {
    public final String LOG_TAG = MainActivityTest.class.getSimpleName();

    Activity mMainActivity;

    private MockWebServer mockWebServer;
    private Retrofit retrofit;
    private MockRetrofit mockRetrofit;
    private ApiInterface apiInterface;
    private NetworkBehavior behavior;
    private String masterList;

    public static  String quote_200_ok_response = "{\"body\":{\"city\":{\"coord\":{\"lat\":44.804008,\"lon\":20.46513},\"country\":\"RS\",\"id\":\"792680\",\"name\":\"Belgrade\",\"population\":0},\"cnt\":14,\"cod\":\"200\",\"message\":0.0234,\"list\":[{\"clouds\":\"0\",\"dt\":\"1479981600\",\"deg\":\"103\",\"humidity\":\"80\",\"pressure\":\"1023.18\",\"speed\":\"1.91\",\"temp\":{\"day\":\"12\",\"eve\":\"13.34\",\"max\":\"16.06\",\"min\":\"3.98\",\"morn\":\"12\",\"night\":\"3.98\"},\"weather\":[{\"description\":\"clear sky\",\"icon\":\"01d\",\"id\":\"800\",\"main\":\"Clear\"}]},{\"clouds\":\"32\",\"dt\":\"1480068000\",\"deg\":\"90\",\"humidity\":\"81\",\"pressure\":\"1018.32\",\"speed\":\"1.96\",\"temp\":{\"day\":\"7.28\",\"eve\":\"9.71\",\"max\":\"10.86\",\"min\":\"0.36\",\"morn\":\"1.04\",\"night\":\"3.8\"},\"weather\":[{\"description\":\"scattered clouds\",\"icon\":\"03d\",\"id\":\"802\",\"main\":\"Clouds\"}]},{\"clouds\":\"64\",\"dt\":\"1480154400\",\"deg\":\"117\",\"humidity\":\"82\",\"pressure\":\"1017.12\",\"speed\":\"4.31\",\"temp\":{\"day\":\"6.78\",\"eve\":\"8.9\",\"max\":\"9.69\",\"min\":\"2.44\",\"morn\":\"2.44\",\"night\":\"2.62\"},\"weather\":[{\"description\":\"broken clouds\",\"icon\":\"04d\",\"id\":\"803\",\"main\":\"Clouds\"}]},{\"clouds\":\"17\",\"dt\":\"1480240800\",\"deg\":\"158\",\"humidity\":\"0\",\"pressure\":\"1006.28\",\"speed\":\"1.35\",\"temp\":{\"day\":\"13.15\",\"eve\":\"5.46\",\"max\":\"13.15\",\"min\":\"3.03\",\"morn\":\"3.03\",\"night\":\"4.03\"},\"weather\":[{\"description\":\"light rain\",\"icon\":\"10d\",\"id\":\"500\",\"main\":\"Rain\"}]},{\"clouds\":\"56\",\"dt\":\"1480327200\",\"deg\":\"329\",\"humidity\":\"0\",\"pressure\":\"1004.63\",\"speed\":\"4.38\",\"temp\":{\"day\":\"4.8\",\"eve\":\"2.11\",\"max\":\"4.8\",\"min\":\"-0.64\",\"morn\":\"2.69\",\"night\":\"-0.64\"},\"weather\":[{\"description\":\"light snow\",\"icon\":\"13d\",\"id\":\"600\",\"main\":\"Snow\"}]},{\"clouds\":\"31\",\"dt\":\"1480413600\",\"deg\":\"344\",\"humidity\":\"0\",\"pressure\":\"1009.22\",\"speed\":\"2.5\",\"temp\":{\"day\":\"2.29\",\"eve\":\"-1.17\",\"max\":\"2.29\",\"min\":\"-2.37\",\"morn\":\"-2.06\",\"night\":\"-2.37\"},\"weather\":[{\"description\":\"snow\",\"icon\":\"13d\",\"id\":\"601\",\"main\":\"Snow\"}]},{\"clouds\":\"46\",\"dt\":\"1480500000\",\"deg\":\"333\",\"humidity\":\"0\",\"pressure\":\"1020.94\",\"speed\":\"4.39\",\"temp\":{\"day\":\"-0.42\",\"eve\":\"-4.15\",\"max\":\"-0.42\",\"min\":\"-4.75\",\"morn\":\"-2.34\",\"night\":\"-4.75\"},\"weather\":[{\"description\":\"snow\",\"icon\":\"13d\",\"id\":\"601\",\"main\":\"Snow\"}]},{\"clouds\":\"42\",\"dt\":\"1480586400\",\"deg\":\"279\",\"humidity\":\"0\",\"pressure\":\"1024.29\",\"speed\":\"3.05\",\"temp\":{\"day\":\"-1.89\",\"eve\":\"-5.49\",\"max\":\"-1.89\",\"min\":\"-8.61\",\"morn\":\"-5.98\",\"night\":\"-8.61\"},\"weather\":[{\"description\":\"light snow\",\"icon\":\"13d\",\"id\":\"600\",\"main\":\"Snow\"}]},{\"clouds\":\"0\",\"dt\":\"1480672800\",\"deg\":\"201\",\"humidity\":\"0\",\"pressure\":\"1021.41\",\"speed\":\"2.36\",\"temp\":{\"day\":\"-0.83\",\"eve\":\"-7.05\",\"max\":\"-0.83\",\"min\":\"-10.64\",\"morn\":\"-9.92\",\"night\":\"-10.64\"},\"weather\":[{\"description\":\"clear sky\",\"icon\":\"01d\",\"id\":\"800\",\"main\":\"Clear\"}]},{\"clouds\":\"35\",\"dt\":\"1480759200\",\"deg\":\"285\",\"humidity\":\"0\",\"pressure\":\"1015.57\",\"speed\":\"5.2\",\"temp\":{\"day\":\"0.29\",\"eve\":\"-0.61\",\"max\":\"0.29\",\"min\":\"-9.05\",\"morn\":\"-9.05\",\"night\":\"-2.95\"},\"weather\":[{\"description\":\"light snow\",\"icon\":\"13d\",\"id\":\"600\",\"main\":\"Snow\"}]},{\"clouds\":\"4\",\"dt\":\"1480845600\",\"deg\":\"222\",\"humidity\":\"0\",\"pressure\":\"1015.28\",\"speed\":\"2.06\",\"temp\":{\"day\":\"1.71\",\"eve\":\"-6.64\",\"max\":\"1.71\",\"min\":\"-9.23\",\"morn\":\"-6.92\",\"night\":\"-9.23\"},\"weather\":[{\"description\":\"clear sky\",\"icon\":\"01d\",\"id\":\"800\",\"main\":\"Clear\"}]},{\"clouds\":\"2\",\"dt\":\"1480932000\",\"deg\":\"156\",\"humidity\":\"0\",\"pressure\":\"1010.46\",\"speed\":\"2.11\",\"temp\":{\"day\":\"3.07\",\"eve\":\"-6.05\",\"max\":\"3.07\",\"min\":\"-10.65\",\"morn\":\"-10.65\",\"night\":\"-6.83\"},\"weather\":[{\"description\":\"clear sky\",\"icon\":\"01d\",\"id\":\"800\",\"main\":\"Clear\"}]},{\"clouds\":\"70\",\"dt\":\"1481018400\",\"deg\":\"144\",\"humidity\":\"0\",\"pressure\":\"994.84\",\"speed\":\"2.54\",\"temp\":{\"day\":\"6.54\",\"eve\":\"3.5\",\"max\":\"6.54\",\"min\":\"-2.05\",\"morn\":\"-2.05\",\"night\":\"2.03\"},\"weather\":[{\"description\":\"moderate rain\",\"icon\":\"10d\",\"id\":\"501\",\"main\":\"Rain\"}]},{\"clouds\":\"72\",\"dt\":\"1481104800\",\"deg\":\"282\",\"humidity\":\"0\",\"pressure\":\"988.51\",\"speed\":\"5.76\",\"temp\":{\"day\":\"5.44\",\"eve\":\"1.22\",\"max\":\"5.44\",\"min\":\"0.02\",\"morn\":\"0.02\",\"night\":\"0.97\"},\"weather\":[{\"description\":\"light snow\",\"icon\":\"13d\",\"id\":\"600\",\"main\":\"Snow\"}]}]},\"rawResponse\":{\"body\":{\"contentLength\":3699,\"contentType\":{\"charset\":\"utf-8\",\"mediaType\":\"application/json; charset\\u003dutf-8\",\"subtype\":\"json\",\"type\":\"application\"}},\"code\":200,\"headers\":{\"namesAndValues\":[\"Server\",\"nginx\",\"Date\",\"Thu, 24 Nov 2016 13:02:27 GMT\",\"Content-Type\",\"application/json; charset\\u003dutf-8\",\"X-Source\",\"redis\",\"Access-Control-Allow-Origin\",\"*\",\"Access-Control-Allow-Credentials\",\"true\",\"Access-Control-Allow-Methods\",\"GET, POST\",\"Content-Length\",\"3699\"]},\"message\":\"OK\",\"networkResponse\":{\"code\":200,\"headers\":{\"namesAndValues\":[\"Server\",\"nginx\",\"Date\",\"Thu, 24 Nov 2016 13:02:27 GMT\",\"Content-Type\",\"application/json; charset\\u003dutf-8\",\"X-Source\",\"redis\",\"Access-Control-Allow-Origin\",\"*\",\"Access-Control-Allow-Credentials\",\"true\",\"Access-Control-Allow-Methods\",\"GET, POST\",\"Content-Length\",\"3699\"]},\"message\":\"OK\",\"protocol\":\"HTTP_1_1\",\"receivedResponseAtMillis\":1479992553405,\"request\":{\"cacheControl\":{\"isPrivate\":false,\"isPublic\":false,\"maxAgeSeconds\":-1,\"maxStaleSeconds\":-1,\"minFreshSeconds\":-1,\"mustRevalidate\":false,\"noCache\":false,\"noStore\":false,\"noTransform\":false,\"onlyIfCached\":false,\"sMaxAgeSeconds\":-1},\"headers\":{\"namesAndValues\":[\"Host\",\"api.openweathermap.org\",\"Connection\",\"Keep-Alive\",\"Accept-Encoding\",\"gzip\",\"User-Agent\",\"okhttp/3.4.2\"]},\"method\":\"GET\",\"tag\":{\"headers\":{\"namesAndValues\":[]},\"method\":\"GET\",\"url\":{\"host\":\"api.openweathermap.org\",\"password\":\"\",\"pathSegments\":[\"data\",\"2.5\",\"forecast\",\"daily\"],\"port\":80,\"queryNamesAndValues\":[\"\",null,\"id\",\"792680\",\"mode\",\"json\",\"units\",\"metric\",\"cnt\",\"14\",\"APPID\",\"2a8a37849fd28415152284ae4da09c42\"],\"scheme\":\"http\",\"url\":\"http://api.openweathermap.org/data/2.5/forecast/daily?\\u0026id\\u003d792680\\u0026mode\\u003djson\\u0026units\\u003dmetric\\u0026cnt\\u003d14\\u0026APPID\\u003d2a8a37849fd28415152284ae4da09c42\",\"username\":\"\"}},\"url\":{\"host\":\"api.openweathermap.org\",\"password\":\"\",\"pathSegments\":[\"data\",\"2.5\",\"forecast\",\"daily\"],\"port\":80,\"queryNamesAndValues\":[\"\",null,\"id\",\"792680\",\"mode\",\"json\",\"units\",\"metric\",\"cnt\",\"14\",\"APPID\",\"2a8a37849fd28415152284ae4da09c42\"],\"scheme\":\"http\",\"url\":\"http://api.openweathermap.org/data/2.5/forecast/daily?\\u0026id\\u003d792680\\u0026mode\\u003djson\\u0026units\\u003dmetric\\u0026cnt\\u003d14\\u0026APPID\\u003d2a8a37849fd28415152284ae4da09c42\",\"username\":\"\"}},\"sentRequestAtMillis\":1479992553331},\"protocol\":\"HTTP_1_1\",\"receivedResponseAtMillis\":1479992553405,\"request\":{\"headers\":{\"namesAndValues\":[]},\"method\":\"GET\",\"url\":{\"host\":\"api.openweathermap.org\",\"password\":\"\",\"pathSegments\":[\"data\",\"2.5\",\"forecast\",\"daily\"],\"port\":80,\"queryNamesAndValues\":[\"\",null,\"id\",\"792680\",\"mode\",\"json\",\"units\",\"metric\",\"cnt\",\"14\",\"APPID\",\"2a8a37849fd28415152284ae4da09c42\"],\"scheme\":\"http\",\"url\":\"http://api.openweathermap.org/data/2.5/forecast/daily?\\u0026id\\u003d792680\\u0026mode\\u003djson\\u0026units\\u003dmetric\\u0026cnt\\u003d14\\u0026APPID\\u003d2a8a37849fd28415152284ae4da09c42\",\"username\":\"\"}},\"sentRequestAtMillis\":1479992553331}}";

    public static  String quote_500_internal_error = "{\"rawResponse\":{\"body\":{\"contentLength\":80,\"contentType\":{\"mediaType\":\"text/html\",\"subtype\":\"html\",\"type\":\"text\"}},\"code\":500,\"headers\":{\"namesAndValues\":[\"Server\",\"nginx\",\"Date\",\"Thu, 24 Nov 2016 13:11:19 GMT\",\"Content-Type\",\"text/html\",\"Content-Length\",\"80\"]},\"message\":\"Internal Server Error\",\"networkResponse\":{\"code\":500,\"headers\":{\"namesAndValues\":[\"Server\",\"nginx\",\"Date\",\"Thu, 24 Nov 2016 13:11:19 GMT\",\"Content-Type\",\"text/html\",\"Content-Length\",\"80\"]},\"message\":\"Internal Server Error\",\"protocol\":\"HTTP_1_1\",\"receivedResponseAtMillis\":1479993084748,\"request\":{\"cacheControl\":{\"isPrivate\":false,\"isPublic\":false,\"maxAgeSeconds\":-1,\"maxStaleSeconds\":-1,\"minFreshSeconds\":-1,\"mustRevalidate\":false,\"noCache\":false,\"noStore\":false,\"noTransform\":false,\"onlyIfCached\":false,\"sMaxAgeSeconds\":-1},\"headers\":{\"namesAndValues\":[\"Host\",\"api.openweathermap.org\",\"Connection\",\"Keep-Alive\",\"Accept-Encoding\",\"gzip\",\"User-Agent\",\"okhttp/3.4.2\"]},\"method\":\"GET\",\"tag\":{\"headers\":{\"namesAndValues\":[]},\"method\":\"GET\",\"url\":{\"host\":\"api.openweathermap.org\",\"password\":\"\",\"pathSegments\":[\"data\",\"2.5\",\"forecast\",\"daily\"],\"port\":80,\"queryNamesAndValues\":[\"\",null,\"id\",\"79268011\",\"mode\",\"json\",\"units\",\"metric\",\"cnt\",\"14\",\"APPID\",\"2a8a37849fd28415152284ae4da09c42\"],\"scheme\":\"http\",\"url\":\"http://api.openweathermap.org/data/2.5/forecast/daily?\\u0026id\\u003d79268011\\u0026mode\\u003djson\\u0026units\\u003dmetric\\u0026cnt\\u003d14\\u0026APPID\\u003d2a8a37849fd28415152284ae4da09c42\",\"username\":\"\"}},\"url\":{\"host\":\"api.openweathermap.org\",\"password\":\"\",\"pathSegments\":[\"data\",\"2.5\",\"forecast\",\"daily\"],\"port\":80,\"queryNamesAndValues\":[\"\",null,\"id\",\"79268011\",\"mode\",\"json\",\"units\",\"metric\",\"cnt\",\"14\",\"APPID\",\"2a8a37849fd28415152284ae4da09c42\"],\"scheme\":\"http\",\"url\":\"http://api.openweathermap.org/data/2.5/forecast/daily?\\u0026id\\u003d79268011\\u0026mode\\u003djson\\u0026units\\u003dmetric\\u0026cnt\\u003d14\\u0026APPID\\u003d2a8a37849fd28415152284ae4da09c42\",\"username\":\"\"}},\"sentRequestAtMillis\":1479993084648},\"protocol\":\"HTTP_1_1\",\"receivedResponseAtMillis\":1479993084748,\"request\":{\"headers\":{\"namesAndValues\":[]},\"method\":\"GET\",\"url\":{\"host\":\"api.openweathermap.org\",\"password\":\"\",\"pathSegments\":[\"data\",\"2.5\",\"forecast\",\"daily\"],\"port\":80,\"queryNamesAndValues\":[\"\",null,\"id\",\"79268011\",\"mode\",\"json\",\"units\",\"metric\",\"cnt\",\"14\",\"APPID\",\"2a8a37849fd28415152284ae4da09c42\"],\"scheme\":\"http\",\"url\":\"http://api.openweathermap.org/data/2.5/forecast/daily?\\u0026id\\u003d79268011\\u0026mode\\u003djson\\u0026units\\u003dmetric\\u0026cnt\\u003d14\\u0026APPID\\u003d2a8a37849fd28415152284ae4da09c42\",\"username\":\"\"}},\"sentRequestAtMillis\":1479993084648}}";


    @Before
    public void setUp() throws Exception {
        super.setUp();

        super.setUp();

        mockWebServer = new MockWebServer();
        mockWebServer.start();

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client =  new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
        retrofit = new Retrofit.Builder().baseUrl(mockWebServer.url("/").toString())
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        behavior = NetworkBehavior.create();
        mockRetrofit = new MockRetrofit.Builder(retrofit)
                .networkBehavior(behavior)
                .build();

        apiInterface = retrofit.create(ApiInterface.class);
    }

    @Test
    public void testWeatherResponsePASS() throws Exception {

        MockResponse mockResponse = new MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody(quote_200_ok_response);
        mockWebServer.enqueue(mockResponse);

        Call<WeatherResponse> call = apiInterface.getDailyWeatherData("792680","json","metric",14,"2a8a37849fd28415152284ae4da09c42");
        Response<WeatherResponse> responseData = call.execute();

        assertTrue(responseData.isSuccessful());
    }

    @Test
    public void testWeatherResponseFAIL() throws Exception {

        ApiInterface service = retrofit.create(ApiInterface.class);

        Call<WeatherResponse> call = service.getDailyWeatherData("1","1","1",0,"1");

        MockResponse mockResponse = new MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody(quote_500_internal_error);
        mockWebServer.enqueue(mockResponse);
        Response<WeatherResponse> responseData = call.execute();
        assertFalse(responseData.isSuccessful());
    }

    @After
    public void tearDown() throws Exception {
        mockWebServer.shutdown();
        super.tearDown();
    }
}
