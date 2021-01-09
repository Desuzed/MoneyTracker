package com.example.moneytracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private static final String TAG = "MainActivity";
    private ViewPager pager;
    private TabLayout tabLayout;
    private FloatingActionButton fab;
    private ActionMode actionMode = null;
    private Toolbar toolbar;
    private  FirebaseAuth mAuth;
    private boolean isInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar =(Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);
        toolbar.inflateMenu(R.menu.log_out_menu);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPage = pager.getCurrentItem();
                String type = null;
                if (currentPage == MainPagesAdapter.PAGE_INCOMES) {
                    type = Item.TYPE_INCOMES;
                } else if (currentPage == MainPagesAdapter.PAGE_EXPENSES) {
                    type = Item.TYPE_EXPENSES;
                }
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                intent.putExtra(AddItemActivity.TYPE_KEY, type);
                startActivityForResult(intent, ItemsFragment.ADD_ITEM_REQUEST_CODE);
            }
        });
        pager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabLayout);
        pager.addOnPageChangeListener(this);
        tabLayout.setupWithViewPager(pager);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //TODO Метод не срабатывает почему то, но срабатывает в onCreate toolbar.inflateMenu(); так, что появляются просто три точки, без картинки
        getMenuInflater().inflate(R.menu.log_out_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.log_out) {
            Intent intent = new Intent(MainActivity.this, AuthActivity.class);
            FirebaseAuth.getInstance().signOut();
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case MainPagesAdapter.PAGE_INCOMES:
            case MainPagesAdapter.PAGE_EXPENSES:
                fab.show();
                break;
            case MainPagesAdapter.PAGE_BALANCE:
                fab.hide();
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_IDLE: //Делается для того, чтобы нажимать на кнопку можно было только в состоянии покоя
                fab.setEnabled(true);
                break;
            case ViewPager.SCROLL_STATE_DRAGGING:
            case ViewPager.SCROLL_STATE_SETTLING:
                if (actionMode != null) {      //Чтобы при перелистывании actionMode закрывался
                    actionMode.finish();
                }
                fab.setEnabled(false);
                break;
        }
    }

    //Когда вызываем из активити метод startActivityForResult, то результтат метода не передается во вложенные фрагменты
    //и поэтому приходится переопределять метод onActivityResult в активити и во фрагменте и связывать их друг с другом
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //ViewPager руководит фрагментами через менеджер. Получаем все фрагменты, пробегаемся по ним в цикле и вызываем у каждого onActivityResult
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onActionModeStarted(ActionMode mode) {
        super.onActionModeStarted(mode);
        fab.hide();
        actionMode = mode;
    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
        super.onActionModeFinished(mode);
        fab.show();
        actionMode = null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
          //  if (!isInitialized){
                MainPagesAdapter adapter = new MainPagesAdapter(getSupportFragmentManager(), this);
                pager.setAdapter(adapter);
                isInitialized= true;
              //  Toast.makeText(this, "SUCCESS, " + currentUser.getUid(), Toast.LENGTH_SHORT).show();
           // }

        }else{
            Intent intent = new Intent(MainActivity.this, AuthActivity.class);
            startActivity(intent);
           // Toast.makeText(this, "USER NULL", Toast.LENGTH_SHORT).show();
        }
    }
}