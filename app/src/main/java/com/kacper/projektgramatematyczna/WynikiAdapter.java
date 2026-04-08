package com.kacper.projektgramatematyczna;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WynikiAdapter extends RecyclerView.Adapter<WynikiAdapter.ViewHolder> {
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView nr, nick, punkty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nr = itemView.findViewById(R.id.nrWiersza);
            nick = itemView.findViewById(R.id.nick);
            punkty = itemView.findViewById(R.id.punkty);
        }
    }
    private List<Wyniki> listaWynikow;
    private int offset;

    public WynikiAdapter(List<Wyniki> lista, int offset) {
        this.listaWynikow = lista;
        this.offset = offset;
    }

    public void setLista(List<Wyniki> nowaLista, int offset){
        this.listaWynikow = nowaLista;
        this.offset = offset;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WynikiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_wiersza_tabeli_wynikow, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WynikiAdapter.ViewHolder holder, int position) {
        Wyniki w = listaWynikow.get(position);
        int miejsceWRankingu = position + offset;
        if(miejsceWRankingu == 0) holder.nr.setText("🥇");
        else if(miejsceWRankingu == 1) holder.nr.setText("🥈");
        else if(miejsceWRankingu == 2) holder.nr.setText("🥉");
        else holder.nr.setText(String.valueOf(miejsceWRankingu + 1));

        holder.nick.setText(w.getNickGracza());
        holder.punkty.setText(String.valueOf(w.getPunktyGracza()));
    }

    @Override
    public int getItemCount() {
        return listaWynikow.size();
    }
}
