package com.example.moneytracker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.moneytracker.R.id.list;

public class ItemsFragment extends Fragment {
    private RecyclerView recyclerView;
    private static final String TAG = "ItemsFragment";
    private ItemsAdapter adapter;
    private FloatingActionButton fab;
    private static final String TYPE_KEY = "type";
    private final static int DATA_LOADED = 100;
    private static final int ADD_ITEM_REQUEST_CODE = 123;
    private String type;
    private Api api;
    private SwipeRefreshLayout refreshLayout;
    //    private static final int TYPE_UNKNOWN = -1;
//    public static final int TYPE_INCOMES = 0;
//    public static final int TYPE_EXPENSES = 1;
//    public static final int TYPE_BALANCE = 2;
    public static ItemsFragment createItemsFragment(String type) {
        ItemsFragment fragment = new ItemsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ItemsFragment.TYPE_KEY, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ItemsAdapter();
        Bundle bundle = getArguments();
        type = bundle.getString(TYPE_KEY, Item.TYPE_UNKNOWN);
        if (type.equals(Item.TYPE_UNKNOWN)) {
            throw new IllegalArgumentException("Unknown type");
        }
        //Фрагмент имеет доступ к внешней активити, т.е вызываем getActivity,а активити имеет доступ к Application

        api = ((App) getActivity().getApplication()).getApi();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Строчка делает то же самое, что и setContentView в обычном активити
        View view = inflater.inflate(R.layout.fragment_items, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: create");
        recyclerView = view.findViewById(list);
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: button pressed" );
                Intent intent = new Intent(getContext(), AddItemActivity.class);
                intent.putExtra(AddItemActivity.TYPE_KEY, type);
                startActivityForResult(intent, ADD_ITEM_REQUEST_CODE);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        refreshLayout = view.findViewById(R.id.refreshSwipe);
        refreshLayout.setColorSchemeColors(Color.CYAN);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadItems();
            }
        });
        loadItems();
    }
    private void loadItems (){
        Call<List<Item>> call = api.getItems(type);
        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                adapter.setData(response.body());
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                refreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (ADD_ITEM_REQUEST_CODE== requestCode && resultCode == Activity.RESULT_OK){
            Item item = data.getParcelableExtra("item");
            Log.d(TAG, "onActivityResult: name = " + item.name + " price = " + item.price);
            adapter.addItem(item);
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}






        //С применением AcyncTask. Его использование нежелательно т.к возможны утечки памяти в связи с
// тем, что создавая его как анонимный класс, он имеет ссылку на активити. Дальше он отрабатывает в отдельном потоке
// и если активити сворачивается или закрывается, то происходят утечки памяти
//    private void loadItems (){
//        AsyncTask <Void, Void, List <Item>> acyncTask = new AsyncTask<Void, Void, List <Item>>(){
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                Log.d(TAG, "onPreExecute: thread name: " + Thread.currentThread().getName());
//            }
//
//            @Override
//            protected List<Item> doInBackground(Void... voids) {
//                Log.d(TAG, "doInBackground: thread name: " + Thread.currentThread().getName());
//                Call<List<Item>> call = api.getItems(type);
//            try {
//                List <Item> items = call.execute().body();
//                return items;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(List<Item> items) {
//                if (items!=null){
//                    adapter.setData(items);
//                }
//
//                super.onPostExecute(items);
//            }
//        };
//        acyncTask.execute();
//    }
        //Thread and handler
//    public void loadItems (){
//        Log.d(TAG, "loadItems: current thread " + Thread.currentThread().getName());
////Сохдаем новый поток т.к нельзя из главного потока обращаться в интернет или к памяти устройства, потому что
//// главный поток занимается отрисовкой
//        new LoadItemsTask(new Handler(callback)).start();
//    }
//
//    private Handler.Callback callback = new Handler.Callback() {
//        @Override
//        public boolean handleMessage(@NonNull Message message) {
//            if (message.what==DATA_LOADED){
//                List <Item> items = (List<Item>) message.obj;
//                adapter.setData(items );
//            }
//            return true;
//        }
//    };
//    private class LoadItemsTask implements Runnable {
//        private Thread thread;
//        private Handler handler;
//        public LoadItemsTask (Handler handler){
//            thread = new Thread(this);
//            this.handler = handler;
//        }
//        public void start (){
//            thread.start();
//        }
//        @Override
//        public void run() {
//            Log.d(TAG, "run: current thread " + Thread.currentThread().getName());
//            Call<List<Item>> call = api.getItems(type);
//            try {
//                List <Item> items = call.execute().body();
//                handler.obtainMessage(DATA_LOADED, items).sendToTarget();
//               // adapter.setData(items);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}