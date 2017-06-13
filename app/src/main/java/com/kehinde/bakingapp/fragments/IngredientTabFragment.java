package com.kehinde.bakingapp.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kehinde.bakingapp.R;
import com.kehinde.bakingapp.adapters.IngredientListAdapter;
import com.kehinde.bakingapp.models.Ingredient;
import com.kehinde.bakingapp.util.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientTabFragment extends Fragment{

    @BindView(R.id.ingredient_recycler) RecyclerView ingredient_recycler;
    private ArrayList<Ingredient> ingredientArrayList;
    private Unbinder unbinder;

    public IngredientTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().getParcelableArrayList(Constants.INGREDIENT_EXTRA) != null){
            ingredientArrayList=getArguments().getParcelableArrayList(Constants.INGREDIENT_EXTRA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ingredient_tab, container, false);
        unbinder=ButterKnife.bind(this,view);

        prepareList();
        return view;
    }

    private void prepareList() {
        IngredientListAdapter ingredientListAdapter = new IngredientListAdapter(ingredientArrayList);
        ingredient_recycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        ingredient_recycler.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        ingredient_recycler.setAdapter(ingredientListAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unbinder.unbind();
    }
}
