package com.ruslaniusupov.android.bakingapp.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruslaniusupov.android.bakingapp.R;
import com.ruslaniusupov.android.bakingapp.models.Step;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepViewHolder> {

    private List<Step> mSteps;
    private OnStepClickListener mStepClickListener;

    public interface OnStepClickListener {
        void onStepClick(int position);
    }

    public StepsAdapter(List<Step> steps, OnStepClickListener listener) {
        mSteps = steps;
        mStepClickListener = listener;
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View stepView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.step_item, parent, false);

        return new StepViewHolder(stepView);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {

        Context context = holder.itemView.getContext();

        Step step = mSteps.get(position);

        String title = step.getShortDescription();
        String number = String.valueOf(position);
        String thumbnailUrl = step.getThumbnailUrl();

        holder.mStepNumberTv.setText(number);

        if (!TextUtils.isEmpty(title)) {
            holder.mStepTitleTv.setText(title);
        }

        if (TextUtils.isEmpty(thumbnailUrl)) {
            holder.mStepImage.setImageResource(R.drawable.ic_step_placeholder);
        } else {
            Picasso.with(context).load(thumbnailUrl)
                    .placeholder(R.drawable.ic_step_placeholder)
                    .error(R.drawable.ic_step_placeholder)
                    .into(holder.mStepImage);
        }

    }

    @Override
    public int getItemCount() {

        if (mSteps == null) {
            return 0;
        }

        return mSteps.size();

    }

    class StepViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.step_title)TextView mStepTitleTv;
        @BindView(R.id.step_number)TextView mStepNumberTv;
        @BindView(R.id.step_image)ImageView mStepImage;

        public StepViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mStepClickListener.onStepClick(getAdapterPosition());
                }
            });

        }

    }

    public void swapData(List<Step> steps) {
        mSteps = steps;
        notifyDataSetChanged();
    }

}
