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
    private List<Item> data = new ArrayList<>();

    public void setData (List<Item> data ){
        this.data = data;
        notifyDataSetChanged();
    }
  /*  public ItemsAdapter() {
        createData();
    }*/

    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Log.d(ItemListActivity.TAG, "onCreateViewHolder: " + parent.getChildCount());
        //Метод inflate преобразует разметку(текст) из xml файла в объект класса View и ViewGroup
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Log.d(ItemListActivity.TAG, "onBindViewHolder: " + itemListActivity.recyclerView.getChildCount() + " " + position);
        Item record = data.get(position);
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

        public void applyData(Item item) {
            //  Log.d(ItemListActivity.TAG, "applyData: " + itemListActivity.recyclerView.getChildLayoutPosition(itemView) + " " + record.getTitle());
            title.setText(item.name);
            String str = String.valueOf(item.price + " ₽");
            price.setText(str);
        }
    }

//    private void createData() {
//        data.add(new Item("Молоко", 35));
//        data.add(new Item("Курсы", 50));
//        data.add(new Item("Работа", 500));
//        data.add(new Item("Сладкое", 523));
//        data.add(new Item("Вредная еда", 552));
//        data.add(new Item("Еда", 2412));
//        data.add(new Item("Хозяйство", 656));
//        data.add(new Item("Регулярные платежи", 8543));
//        data.add(new Item("Мобильная связь", 6321));
//        data.add(new Item("Свадьба", 3467));
//        data.add(new Item("Огород", 37));
//        data.add(new Item("Сад", 999));
//        data.add(new Item("Собака", 647));
//        data.add(new Item("Кошка", 235));
//        data.add(new Item("Компик", 65000));
//    }
}
