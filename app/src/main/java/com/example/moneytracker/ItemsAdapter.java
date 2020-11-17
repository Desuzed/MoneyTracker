package com.example.moneytracker;

import android.util.SparseBooleanArray;
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
    ItemsAdapterListener listener = null;
    public void setListener(ItemsAdapterListener listener) {
        this.listener = listener;
    }

    public void setData(List<Item> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addItem(Item item) {
        data.add(item);
        notifyItemInserted(data.size());
    }
    //===============================  Selections ==================================================

    private SparseBooleanArray selections = new SparseBooleanArray(); //Хранит позиции и их состояния (Мапа с интами и булевыми)

    public void toggleSelection(int position) {
        if (selections.get(position, false)) {
            selections.delete(position);
        } else {
            selections.put(position, true);
        }
        notifyItemChanged(position);
    }

    void clearSelections() {
        selections.clear();
        notifyDataSetChanged();
    }

    int getSelectedItemCount() {
        return selections.size();
    }

    List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selections.size());
        for (int i = 0; i < selections.size(); i++) {
            items.add(selections.keyAt(i));
        }
        return items;
    }

    Item remove(int position) {
        final Item item = data.remove(position);
        notifyItemRemoved(position);
        return item;
    }

    //===============================  VIEW  HOLDER ==================================================
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
        holder.applyData(record, position, listener, selections.get(position, false));
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

        public void applyData(final Item item, final int position, final ItemsAdapterListener listener, boolean selected) {
            //  Log.d(ItemListActivity.TAG, "applyData: " + itemListActivity.recyclerView.getChildLayoutPosition(itemView) + " " + record.getTitle());
            title.setText(item.name);
            String str = item.price + " ₽";
            price.setText(str);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(item, position);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (listener != null) {
                        listener.onItemLongClick(item, position);
                    }
                    return true;
                }
            });

            itemView.setActivated(selected);
        }
    }
}
