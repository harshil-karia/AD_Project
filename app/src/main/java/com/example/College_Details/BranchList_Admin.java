package com.example.College_Details;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

public class BranchList_Admin extends AppCompatActivity {

    BranchAdapter branchAdapter;
    RecyclerView recyclerView;
    FloatingActionButton addBranch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_branch_list_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addBranch = findViewById(R.id.addBranch);
        recyclerView = findViewById(R.id.recyclerViewBranches);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        addBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BranchList_Admin.this,AddBranch.class));
            }
        });

        FirebaseRecyclerOptions<BranchModel> options =
                new FirebaseRecyclerOptions.Builder<BranchModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("branch"),BranchModel.class )
                        .build();

        branchAdapter = new BranchAdapter(options,1);
        recyclerView.setAdapter(branchAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();

        assert searchView != null;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchBranch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchBranch(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void searchBranch(String text){
        FirebaseRecyclerOptions<BranchModel> options =
                new FirebaseRecyclerOptions.Builder<BranchModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("branch").orderByChild("name").startAt(text).endAt(text+"~"),BranchModel.class )
                        .build();

        branchAdapter = new BranchAdapter(options,1);
        branchAdapter.startListening();
        recyclerView.setAdapter(branchAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        branchAdapter.startListening();
    }
}