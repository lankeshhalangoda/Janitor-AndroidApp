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

import com.example.janitorbylankesh.adapters.AdapterUser;
import com.example.janitorbylankesh.models.ModelUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class UsersFragment extends Fragment {
    RecyclerView recyclerView;
    AdapterUser adapterUser;
    List<ModelUser> userList;

    FirebaseAuth firebaseAuth;


    public UsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_users, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        recyclerView = view.findViewById(R.id.users_recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        userList = new ArrayList<>();

        getAllUsers();

        return view;
    }

    private void getAllUsers() {
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelUser modelUser =ds.getValue(ModelUser.class);

                    if(!modelUser.getUid().equals(fUser.getUid())){
                        userList.add(modelUser);
                    }

                    adapterUser = new AdapterUser(getActivity(),userList);
                    recyclerView.setAdapter(adapterUser);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void searchUsers(String query) {

            final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userList.clear();
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        ModelUser modelUser =ds.getValue(ModelUser.class);

                        if(!modelUser.getUid().equals(fUser.getUid())){

                            if(modelUser.getName().toLowerCase().contains(query.toLowerCase()) ||
                                    modelUser.getEmail().toLowerCase().contains(query.toLowerCase())){

                                userList.add(modelUser);
                            }

                        }

                        adapterUser = new AdapterUser(getActivity(),userList);

                        adapterUser.notifyDataSetChanged();
                        recyclerView.setAdapter(adapterUser);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

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

        menu.findItem(R.id.action_add_post).setVisible(false);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                if(!TextUtils.isEmpty(s.trim())){

                    searchUsers(s);
                }
                else{

                    getAllUsers();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                if(!TextUtils.isEmpty(s.trim())){

                    searchUsers(s);
                }
                else{

                    getAllUsers();
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

        return super.onOptionsItemSelected(item);
    }
}
