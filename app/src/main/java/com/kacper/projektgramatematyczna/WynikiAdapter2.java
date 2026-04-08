package com.kacper.projektgramatematyczna;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WynikiAdapter2 extends RecyclerView.Adapter<WynikiAdapter2.ViewHolder> {
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nr, nick, punkty;

        public ViewHolder(View itemView) {
            super(itemView);
            nr = itemView.findViewById(R.id.nrWiersza);
            nick = itemView.findViewById(R.id.nick);
            punkty = itemView.findViewById(R.id.punkty);
        }
    }

    private List<Wyniki> lista;
    private int offset;

    public WynikiAdapter2(List<Wyniki> lista, int offset) {
        this.lista = lista;
        this.offset = offset;
    }

    public void setLista(List<Wyniki> nowaLista, int offset) {
        this.lista = nowaLista;
        this.offset = offset;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_wiersza_tabeli_wynikow, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Wyniki w = lista.get(position);

        int miejsce = position + offset;

        if (miejsce == 0) holder.nr.setText("🥇");
        else if (miejsce == 1) holder.nr.setText("🥈");
        else if (miejsce == 2) holder.nr.setText("🥉");
        else holder.nr.setText(String.valueOf(miejsce + 1));

        holder.nick.setText(w.getNickGracza());
        holder.punkty.setText(String.valueOf(w.getPunktyGracza()));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}