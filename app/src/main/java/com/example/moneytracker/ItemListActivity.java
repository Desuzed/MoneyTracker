package com.example.moneytracker;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static com.example.moneytracker.R.id.list;

public class ItemListActivity extends AppCompatActivity {
    private static final String TAG = "ItemListActivity";
    private RecyclerView recyclerView;
    private List<ItemRecord> data  = new ArrayList<>();
    private ItemListAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        recyclerView = findViewById(list);
        adapter = new ItemListAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        createData();
        recyclerView.setAdapter(adapter);
    }

    private void createData() {
        data.add(new ItemRecord("Молоко", 35));
        data.add(new ItemRecord("Курсы", 50));
        data.add(new ItemRecord("Работа", 500));
        data.add(new ItemRecord("Сладкое", 523));
        data.add(new ItemRecord("Вредная еда", 552));
        data.add(new ItemRecord("Еда", 2412));
        data.add(new ItemRecord("Хозяйство", 656));
        data.add(new ItemRecord("Регулярные платежи", 8543));
        data.add(new ItemRecord("Мобильная связь", 6321));
        data.add(new ItemRecord("Свадьба", 3467));
        data.add(new ItemRecord("Огород", 37));
        data.add(new ItemRecord("Сад", 999));
        data.add(new ItemRecord("Собака", 647));
        data.add(new ItemRecord("Кошка", 235));
        data.add(new ItemRecord("Компик", 65000));
    }

    private class ItemListAdapter extends RecyclerView.Adapter <ItemListAdapter.RecordViewHolder>{

        @NonNull
        @Override
        public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Log.d(TAG, "onCreateViewHolder: " + parent.getChildCount());
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, parent, false);
            return new RecordViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
            Log.d(TAG, "onBindViewHolder: " + recyclerView.getChildCount() + " " + position);
            ItemRecord record = data.get(position);
             holder.applyData(record);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        private class RecordViewHolder extends RecyclerView.ViewHolder {
            private final TextView title;
            private final TextView price;
            public RecordViewHolder(@NonNull View itemView) {
                super(itemView);
                title =  itemView.findViewById(R.id.item_name);
                price = itemView.findViewById(R.id.item_price);

            }
            public void applyData (ItemRecord record){
                Log.d(TAG, "applyData: " + recyclerView.getChildLayoutPosition(itemView) + " " + record.getTitle());
                title.setText(record.getTitle());
                price.setText(String.valueOf(record.getPrice()));
            }
        }

    }
}
