package com.example.workpod.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.workpod.R;

public class Fragment_Dialog_Invita_Amigo extends Fragment {


    public Fragment_Dialog_Invita_Amigo() {
        // Required empty public constructor
    }

    public static Fragment_Dialog_Invita_Amigo newInstance(String param1, String param2) {
        Fragment_Dialog_Invita_Amigo fragment = new Fragment_Dialog_Invita_Amigo();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_dialog_invita_amigo, container, false);
        // Inflate the layout for this fragment
        return view;
    }
}