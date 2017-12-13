package com.example.user.weatherapp.ui.base;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 08.12.2017
 */

public abstract class SelectableAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>{

    public SparseBooleanArray selectedItem;
    public ArrayList<Integer> list = new ArrayList<>();

    public SelectableAdapter() {
        selectedItem = new SparseBooleanArray();
    }

    public boolean isSelected(int position){
        return getSelectedItem().contains(position);
    }

    public void toggleSelection(int position){
        if (selectedItem.get(position, false)){
            selectedItem.delete(position);
        }else {
            selectedItem.put(position, true);
        }
        notifyItemChanged(position);
    }

    public void clearSelection(){
        List<Integer> selection = getSelectedItem();
        selectedItem.clear();
        for (Integer i : selection){
            notifyItemChanged(i);
        }
    }

    public int getSelectedItemCount(){
        return selectedItem.size();
    }

    public List<Integer> getSelectedItem(){
        List<Integer> items = new ArrayList<>(selectedItem.size());
        for (int i = 0; i < selectedItem.size(); ++i){
            items.add(selectedItem.keyAt(i));
        }
        return items;
    }
}
