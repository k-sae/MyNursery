package com.kareem.mynursery.profile;

import android.app.Activity;
import android.content.Intent;
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

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link }.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyProfileRecyclerViewAdapter extends RecyclerView.Adapter<MyProfileRecyclerViewAdapter.ViewHolder> {

//    private final OnListFragmentInteractionListener mListener;
    private int itemNumber;
    private Activity parent;
    private final ProfileAdapterOnClickHandler onClickListener;


    public MyProfileRecyclerViewAdapter(Activity parent,ProfileAdapterOnClickHandler onClickListener) {
        this.parent=parent;
        this.onClickListener=onClickListener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_profile, parent, false);

        ViewHolder viewHolder =new ViewHolder(view);
        addListners(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        if (position==0)renderUser(holder);
        else if (position==getItemCount()-1)renderaddNursery(holder);
        else renderUserNurseries(holder,position-1);



    }
    private void renderUser(final ViewHolder holder ){

        holder.userName.setVisibility(View.VISIBLE);
        holder.userName.setText(Auth.getLoggedUser().getName());
        holder.save.setVisibility(View.VISIBLE);
    }
    private  void renderUserNurseries(final ViewHolder holder,int position){
        //TODO fix get user nurseries
        holder.nurserySection.setVisibility(View.VISIBLE);
        Nursery nursery =new Nursery();
        nursery.setId(Auth.getLoggedUser().getNurseries().get(position));
        nursery.startSync();
        nursery.setOnChangeListener(new ObjectChangedListener() {
            @Override
            public void onChange(RealTimeObject realTimeObject) {
                Nursery nursery1=(Nursery) realTimeObject;
                holder.location.setText(nursery1.getCity());
                holder.title.setText(nursery1.getName());
                for (String image: nursery1.getImagesId()) {
                    GlideSliderView glideSliderView = new GlideSliderView(parent);
                    glideSliderView.image(image)
                            .setScaleType(BaseSliderView.ScaleType.Fit);
                    glideSliderView.bundle(new Bundle());
                    glideSliderView.getBundle()
                            .putString("imageUrl",image);
                    holder.sliderLayout.addSlider(glideSliderView);
                }
                holder.sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
                holder.sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                holder.sliderLayout.setCustomAnimation(new DescriptionAnimation());
                holder.sliderLayout.setDuration(4000);

            }
        });
      

    }
    private void renderaddNursery(final ViewHolder holder){holder.addNursery.setVisibility(View.VISIBLE);}


    private void addListners(final ViewHolder holder){
        holder.addNursery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parent , AddNursery.class);
                parent.startActivity(intent);
            }
        });
        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = holder.userName.getText().toString();
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
        return Auth.getLoggedUser().getNurseries().size()+2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View nurserySection;
        public final EditText userName;
        public final Button save;
        public final Button addNursery;
        public final SliderLayout sliderLayout;
        public final TextView title;
        public final TextView location;

        public ViewHolder(View view) {
            super(view);
           nurserySection=(View)view.findViewById(R.id.profileNurseriesItems);
            userName = (EditText)view.findViewById(R.id.userName);
           save = (Button)view.findViewById(R.id.saveUsername);
            addNursery=(Button)view.findViewById(R.id.profileAddNursery);
            title=(TextView) nurserySection.findViewById(R.id.title);
            location=(TextView) nurserySection.findViewById(R.id.location);
            sliderLayout=(SliderLayout)nurserySection.findViewById(R.id.slider);
            view.setOnClickListener(this);
        }
        public void bind(int index ) {


        }
        @Override
        public String toString() {
            return super.toString() + " '" +  "'";
        }

        @Override
        public void onClick(View view) {
            onClickListener.onClick("clicked");

        }
    }

    public interface ProfileAdapterOnClickHandler{
        void onClick(String nurseryId);
    }
}
