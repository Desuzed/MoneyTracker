package com.example.moneytracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

public class AddItemActivity extends AppCompatActivity {
    private static final String TAG = "AddItemActivity";
    public static final String TYPE_KEY = "type";
    private String type;
    private EditText name;
    private EditText price;
    private Button addButton;
    private boolean hasTextPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true); //Добавляет стрелочку "назад"
        name = findViewById(R.id.add_item_name);
        price = findViewById(R.id.add_item_price);
        addButton = findViewById(R.id.add_item_btn);
        type = getIntent().getExtras().getString(TYPE_KEY);
        price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                hasTextPrice = TextUtils.isEmpty(editable);
            }
        });
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                addButton.setEnabled(!TextUtils.isEmpty(editable) && !hasTextPrice);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameValue = name.getText().toString();
                String priceValue = price.getText().toString();
                Item item = new Item(nameValue, priceValue, type);
                Intent intent = new Intent();
                intent.putExtra("item", item);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull final MenuItem item) {
//        if (item.getItemId() == android.R.id.home){
//            Log.i(TAG, "onOptionsItemSelected: ");
//
//           // onBackPressed();
//           // finish();
//        }
//        return super.onOptionsItemSelected(item);
//    }
}