package com.example.janitorbylankesh;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.janitorbylankesh.adapters.AdapterPost;
import com.example.janitorbylankesh.models.ModelPost;
import com.example.janitorbylankesh.notifications.Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    FirebaseAuth firebaseAuth;

    RecyclerView recyclerView;
    List<ModelPost> postList;
    AdapterPost adapterPost;

    public HomeFragment(){
        //required empty
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        recyclerView = view.findViewById(R.id.postRecyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(layoutManager);

        postList = new ArrayList<>();

        loadPosts();

        return view;
    }

    private void loadPosts() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelPost modelPost = ds.getValue(ModelPost.class);
                    postList.add(modelPost);

                    adapterPost = new AdapterPost(getActivity(), postList);

                    recyclerView.setAdapter(adapterPost);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    //user is signed in, stay
                    //mProfileTv.setText(user.getEmail());
                }
                else{
                    //user not signed, go to main activity
                    //startActivity(new Intent(getActivity(), MainActivity.class));
                    //getActivity().finish();
                }
            }
                //Toast.makeText(getActivity(), ""+ databaseError.getMessage(), Toast.LENGTH_SHORT).show();

        });
    }

    private void searchPosts(String searchQuery){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelPost modelPost = ds.getValue(ModelPost.class);

                    if(modelPost.getpTitle().toLowerCase().contains(searchQuery.toLowerCase(Locale.ROOT)) ||
                            modelPost.getpDescription().toLowerCase(Locale.ROOT).contains(searchQuery.toLowerCase(Locale.ROOT))){

                        postList.add(modelPost);


                    }


                    adapterPost = new AdapterPost(getActivity(), postList);

                    recyclerView.setAdapter(adapterPost);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), ""+ databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void checkUserStatus(){
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null){
            //user is signed in, stay
            //mProfileTv.setText(user.getEmail());
        }
        else{
            //user not signed, go to main activity
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                if(!TextUtils.isEmpty(s)){
                    searchPosts(s);
                }
                else{
                    loadPosts();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                if(!TextUtils.isEmpty(s)){
                    searchPosts(s);
                }
                else{
                    loadPosts();
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
    //handle menu item clicks

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //get item id
        int id = item.getItemId();
        if(id==R.id.action_logout){
            firebaseAuth.signOut();
            checkUserStatus();
        }
        if(id==R.id.action_add_post){
            startActivity(new Intent(getActivity(), AddPostActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}