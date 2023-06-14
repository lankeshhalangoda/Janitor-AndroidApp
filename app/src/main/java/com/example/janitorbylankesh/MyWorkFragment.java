package com.example.janitorbylankesh;

import android.app.Activity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.janitorbylankesh.R;

public class MyWorkFragment extends Fragment {

    Activity context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){

        context = getActivity();

        View Root = inflater.inflate(R.layout.fragment_my_work, container, false);
        return Root;

    }

    public void onStart(){
        super.onStart();
        Button ExploreHomeBtn = (Button) context.findViewById(R.id.exploreHomeBtn);
        ExploreHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(context, JavaCal.class);
                startActivity(intent);
            }
        });
    }
}