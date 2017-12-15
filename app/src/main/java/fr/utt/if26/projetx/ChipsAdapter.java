package fr.utt.if26.projetx;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.robertlevonyan.views.chip.Chip;
import com.robertlevonyan.views.chip.OnCloseClickListener;


import java.util.List;

/**
 * Created by Raphael on 08/12/2017.
 */


public class ChipsAdapter extends RecyclerView.Adapter<ChipsAdapter.UeViewHolder> {

    private List<String> ueList;

    public class UeViewHolder extends RecyclerView.ViewHolder {
        public Chip ue;

        public UeViewHolder(View view) {
            super(view);
            ue = view.findViewById(R.id.chip);
            ue.setOnCloseClickListener(new OnCloseClickListener() {
                @Override
                public void onCloseClick(View v) {
                    int position = ueList.indexOf(ue.getChipText());
                    ueList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, ueList.size());
                }
            });
        }
    }

    public ChipsAdapter(List<String> ue) {
        this.ueList = ue;
    }

    @Override
    public UeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chip_grid, parent, false);

        return new UeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UeViewHolder holder, int position) {
        String ue = ueList.get(position);
        holder.ue.setChipText(ue);
    }

    @Override
    public int getItemCount() {
        return ueList.size();
    }
}
