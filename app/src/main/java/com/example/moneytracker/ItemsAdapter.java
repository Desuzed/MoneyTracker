package com.example.moneytracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {
    private List<ItemRecord> data = new ArrayList<>();

    public ItemsAdapter() {
        createData();
    }

    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Log.d(ItemListActivity.TAG, "onCreateViewHolder: " + parent.getChildCount());
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Log.d(ItemListActivity.TAG, "onBindViewHolder: " + itemListActivity.recyclerView.getChildCount() + " " + position);
        ItemRecord record = data.get(position);
        holder.applyData(record);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView price;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_name);
            price = itemView.findViewById(R.id.item_price);

        }

        public void applyData(ItemRecord record) {
            //  Log.d(ItemListActivity.TAG, "applyData: " + itemListActivity.recyclerView.getChildLayoutPosition(itemView) + " " + record.getTitle());
            title.setText(record.getTitle());
            String str = String.valueOf(record.getPrice()) + " ₽";
            price.setText(str);
        }
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
}
