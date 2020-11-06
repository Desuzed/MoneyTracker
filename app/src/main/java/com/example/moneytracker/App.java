package com.example.moneytracker;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//К этому классу будет доступ из всех остальных классов, т.к он работает всегда. Ретрофит
// будет использоваться на всех экранах и чтобы заново не инициализировать и не настраивать в каждом
// активити ретрофит, инициализируем его в том классе, который работает постоянно
public class App extends Application {
    private Api api;
    @Override
    public void onCreate() {
        super.onCreate();
        //Gson отвечает за парсинг информации
        Gson gson = new  GsonBuilder()
                .setDateFormat("dd.mm.yyyy")
                .create();
        //Отвечает за соединение с сервером. Interceptor - перехватчик запросов, который может как-то изменить запрос
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(
                BuildConfig.DEBUG
                        ? HttpLoggingInterceptor.Level.BODY
                        : HttpLoggingInterceptor.Level.NONE);//Описали так, чтобы логи писались только
        // в дебажной (нерелизной) сборке ( т.к мы не хотим, чтобы информация об логинах,
        // паролях и тд была видна в логах в релизном приложении)
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        //настраиваем ретрофит, прикрепляем базовую ссылку и настраиваем Gson Converter, который
        //понимает какой тип данных обрабатывать. Можно например и в формате xml обрабатывать и др.

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://moneytrackerartkhromts.getsandbox.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
        //ретрофит создает реализацию интерфейса
        api = retrofit.create(Api.class);
    }
    public Api getApi (){
        return api;
    }
}
