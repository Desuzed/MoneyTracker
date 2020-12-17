package com.example.moneytracker;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {
    private static final String TAG = "ItemsAdapter";
    public static int fireBaseMaxId = 0;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("user");
    private DatabaseReference userRef = myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    private DatabaseReference typeRef;
    private List<Item> data = new ArrayList<>();
    private String type;
    ItemsAdapterListener listener = null;

    public void getDataFromDB() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (data.size() > 0) data.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Item item = ds.getValue(Item.class);
                    assert item != null;
                    if (item.getId() > fireBaseMaxId) {
                        fireBaseMaxId = item.getId();
                    }
                    data.add(item);
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        typeRef.addValueEventListener(valueEventListener);
    }

    public ItemsAdapter(String type) {
        this.type = type;
        typeRef = userRef.child(type);
    }

    public void setListener(ItemsAdapterListener listener) {
        this.listener = listener;
//        ChildEventListener childEventListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot,  String previousChildName) {
//                Log.i(TAG, "onChildAdded: " + snapshot.toString() + ",  previousChildName" + previousChildName );
//
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {
//                Log.i(TAG, "onChildChanged: " + snapshot.toString() + ",  previousChildName" + previousChildName);
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//                Log.i(TAG, "onChildRemoved: "+ snapshot.toString() );
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        };
//        typeRef.addChildEventListener(childEventListener);
    }

//    public void setData(List<Item> data) {
//        this.data = data;
//        notifyDataSetChanged();
//    }

    public void addItem(Item item) {
        data.add(item);
        typeRef.push().setValue(item);
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
        List<Integer> items = new ArrayList<>();
        for (int i = 0; i < selections.size(); i++) {
            items.add(selections.keyAt(i));
        }
        return items;
    }

    Item remove(int position) {
        final Item item = data.remove(position);
        typeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String keyDelete = ds.getKey();
                    Item itemDB = ds.getValue(Item.class);
                    if (item.getId() == itemDB.getId()) {
                        typeRef.child(keyDelete).removeValue();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        notifyItemRemoved(position);
        return item;
    }

    //===============================  VIEW  HOLDER ==================================================
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Log.d(ItemListActivity.TAG, "onCreateViewHolder: " + parent.getChildCount());
        //Метод inflate преобразует разметку(текст) из xml файла в объект класса View и ViewGroup
        if (type.equals(Item.TYPE_EXPENSES)){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
            return new ItemViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_income, parent, false);
            return new ItemViewHolder(view);
        }
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
            title.setText(item.getName());
            String str = item.getPrice() + " ₽";
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
