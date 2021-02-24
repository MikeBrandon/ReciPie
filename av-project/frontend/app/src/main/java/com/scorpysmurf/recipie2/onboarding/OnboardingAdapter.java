package com.scorpysmurf.recipie2.onboarding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scorpysmurf.recipie2.R;

import java.util.List;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>{

    private List<OnboardingItem> onboardingItemList;

    public OnboardingAdapter(List<OnboardingItem> onboardingItemList) {
        this.onboardingItemList = onboardingItemList;
    }

    @NonNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnboardingViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_onboarding, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
        holder.setOnboardingData(onboardingItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return onboardingItemList.size();
    }

    class OnboardingViewHolder extends RecyclerView.ViewHolder {

        private TextView textTitle;
        private TextView textDescription;
        private ImageView imageOnBoarding;

        public OnboardingViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textTitle = itemView.findViewById(R.id.textTitle);
            this.textDescription = itemView.findViewById(R.id.textDescription);
            this.imageOnBoarding = itemView.findViewById(R.id.imageOnboarding);
        }

        void setOnboardingData(OnboardingItem onboardingItem) {
            textTitle.setText(onboardingItem.getTitle());
            textDescription.setText(onboardingItem.getDescription());
            imageOnBoarding.setImageResource(onboardingItem.getImage());
        }
    }

}
