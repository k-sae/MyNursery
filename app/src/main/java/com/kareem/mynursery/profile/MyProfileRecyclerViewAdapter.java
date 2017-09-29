package com.kareem.mynursery.profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kareem.mynursery.model.ObjectChangedListener;
import com.kareem.mynursery.model.RealTimeObject;
import com.kareem.mynursery.model.User;
import com.kareem.mynursery.nursery.AddNursery;
import com.kareem.mynursery.GlideSliderView;
import com.kareem.mynursery.R;
import com.kareem.mynursery.model.Auth;
import com.kareem.mynursery.model.Nursery;


import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.kareem.mynursery.nurseryProfile.NurseryProfileActivity;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link }.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyProfileRecyclerViewAdapter extends RecyclerView.Adapter<MyProfileRecyclerViewAdapter.ViewHolder> {

//    private final OnListFragmentInteractionListener mListener;
    private Activity parent;
    private final ProfileAdapterOnClickHandler onClickListener;


    public MyProfileRecyclerViewAdapter(Activity parent,ProfileAdapterOnClickHandler onClickListener) {
        this.parent=parent;
        this.onClickListener=onClickListener;

    }

    @Override
    public int getItemViewType(int position) {
        if (position==0) return 0;
        else  return 1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==0){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_profile, parent, false);

        ViewHolder viewHolder =new ViewHolder(view);
        addListners(viewHolder);
        return viewHolder;}
        else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.nursery_list_item, parent, false);

            ViewHolder viewHolder =new ViewHolder(view);

            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        if (position==0)renderUser(holder);
        else renderUserNurseries(holder,position-1);



    }
    private void renderUser(final ViewHolder holder ){
          EditText userName=holder.holderView.findViewById(R.id.userName);
        userName.setText(Auth.getLoggedUser().getName());

    }
    private  void renderUserNurseries(final ViewHolder holder, final int position){
        //TODO fix get user nurseries

         final SliderLayout sliderLayout=holder.holderView.findViewById(R.id.slider);
         final TextView title=holder.holderView.findViewById(R.id.title);
         final TextView location=holder.holderView.findViewById(R.id.location);


        Nursery nursery =new Nursery();
        nursery.setId(Auth.getLoggedUser().getNurseries().get(position));
        nursery.startSync();
        nursery.setOnChangeListener(new ObjectChangedListener() {
            @Override
            public void onChange(RealTimeObject realTimeObject) {
                Nursery nursery1=(Nursery) realTimeObject;
                location.setText(nursery1.getCity());
                title.setText(nursery1.getName());
                for (String image: nursery1.getImagesId()) {
                    GlideSliderView glideSliderView = new GlideSliderView(parent);
                    glideSliderView.image(image)
                            .setScaleType(BaseSliderView.ScaleType.Fit);
                    glideSliderView.bundle(new Bundle());
                    glideSliderView.getBundle()
                            .putString("imageUrl",image);
                    sliderLayout.addSlider(glideSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {
                            Intent intent = new Intent(parent, NurseryProfileActivity.class);
                            intent.putExtra("NurseryId",Auth.getLoggedUser().getNurseries().get(position) );
                            parent.startActivity(intent);
                        }
                    }));
                }
               sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
               sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                sliderLayout.setCustomAnimation(new DescriptionAnimation());
                sliderLayout.setDuration(4000);

            }
        });
        holder.holderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parent, NurseryProfileActivity.class);
                intent.putExtra("NurseryId",Auth.getLoggedUser().getNurseries().get(position) );
                parent.startActivity(intent);
            }
        });
      

    }



    private void addListners(final ViewHolder holder){
        final EditText userName=holder.holderView.findViewById(R.id.userName);;
        holder.holderView.findViewById(R.id.profileAddNursery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parent , AddNursery.class);
                parent.startActivity(intent);
            }
        });
        holder.holderView.findViewById(R.id.saveUsername).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = userName.getText().toString();
                if (!name.equals("")&&!name.equals(" "))
                {
                    Auth.getLoggedUser().setName(name);
                    Auth.getLoggedUser().save();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return Auth.getLoggedUser().getNurseries().size()+1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View holderView;



        public ViewHolder(View view) {
            super(view);
            holderView =view;




        }

        @Override
        public String toString() {
            return super.toString() + " '" +  "'";
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position>0 && position<getItemCount()-1)
                onClickListener.onClick(Auth.getLoggedUser().getNurseries().get(position-1));

        }
    }

    public interface ProfileAdapterOnClickHandler{
        void onClick(String nurseryId);
    }
}
