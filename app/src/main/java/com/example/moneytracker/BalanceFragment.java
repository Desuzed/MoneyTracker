package com.example.moneytracker;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BalanceFragment extends Fragment {
    private static final String TAG = "BalanceFragment";
    private TextView total;
    private TextView expense;
    private TextView income;
    private DiagramView diagramView;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    private BalanceResult balanceResult;
    private Thread thread;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_balance, container, false);
        return view;
    }

//    private BalanceResult initBalanceResult() {
//
//        return balanceResult;
//    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        total = view.findViewById(R.id.totalTV);
        expense = view.findViewById(R.id.expenseTV);
        income = view.findViewById(R.id.incomeTV);
        diagramView = view.findViewById(R.id.diagramView);
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser){
//            Log.i(TAG, "setUserVisibleHint: ");
//        }
//    }

    @Override
    public void onResume() {
        super.onResume();
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                updateData();
                Log.i(TAG, "run: " + Thread.currentThread().getName());
            }
        });
        thread.start();
    }

    private void updateData() {
        balanceResult = new BalanceResult();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //   if (data.size() > 0) data.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Item item = ds.getValue(Item.class);
                        if (item.getType().equals(Item.TYPE_EXPENSES)) {
                            balanceResult.setExpense(balanceResult.getExpense() + Integer.parseInt(item.getPrice()));
                        } else if (item.getType().equals(Item.TYPE_INCOMES)) {
                            balanceResult.setIncome(balanceResult.getIncome() + Integer.parseInt(item.getPrice()));
                        }
                    }
                }
                balanceResult.setTotal(balanceResult.getIncome() - balanceResult.getExpense());
                total.setText(String.valueOf(balanceResult.getTotal()));
                expense.setText(String.valueOf(balanceResult.getExpense()));
                income.setText(String.valueOf(balanceResult.getIncome()));
                diagramView.update(balanceResult.getIncome(), balanceResult.getExpense());
                Log.i(TAG, "onDataChange: "  + ", BalanceFragment: EXPENSES = " + balanceResult.getExpense() + ", INCOMES = " + balanceResult.getIncome() + ", TOTAL = " + balanceResult.getTotal());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        myRef.addValueEventListener(valueEventListener);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!(thread.isInterrupted())){
            thread.interrupt();
        }
    }
}
