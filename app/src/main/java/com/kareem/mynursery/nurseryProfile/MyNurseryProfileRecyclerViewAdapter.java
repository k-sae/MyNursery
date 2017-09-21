package com.kareem.mynursery.nurseryProfile;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.kareem.mynursery.R;
import com.kareem.mynursery.model.Auth;
import com.kareem.mynursery.model.Comment;
import com.kareem.mynursery.model.Nursery;
import com.kareem.mynursery.nurseryProfile.NurseryProfileFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyNurseryProfileRecyclerViewAdapter extends RecyclerView.Adapter<MyNurseryProfileRecyclerViewAdapter.ViewHolder> {

    private  Nursery nursery;
    private String nurseryId;
    private Context context;
    private boolean liked=false;
    private ImageView likeButton;
    private int likeNum=0;


    public MyNurseryProfileRecyclerViewAdapter(Context context,String nurseryId) {
       this.context=context;
       this.nurseryId=nurseryId;
        nursery =new Nursery();
        nursery.startSync();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_nurseryprofile, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
       if (position==0) setNurseryData(holder);
        else setCommentsData(holder ,position-1);
    }

    @Override
    public int getItemCount() {
        return nursery.getComments().size()+1;
    }


    private void setNurseryData(final ViewHolder holder){
        for (String image : nursery.getImagesId()){
            TextSliderView textSliderView = new TextSliderView(context);
            textSliderView
                    .description(nursery.getName())
                    .image(image)
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",nursery.getName());
            holder.slider.addSlider(textSliderView);

        }
        holder.slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        holder.slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        holder.slider.setCustomAnimation(new DescriptionAnimation());
        holder.slider.setDuration(4000);
        holder.nurseryName.setText(nursery.getName());
        holder.city.setText(nursery.getCity());
        holder.description.setText(R.string.description);
        holder.sperator.setVisibility(View.VISIBLE);
        holder.descriptionData.setText(nursery.getDescription());
        if (nursery.getActivities().contains("SWIMMING"))
        holder.swimming.setText("Swimming");
        holder.time.setText(nursery.getStartTime()+"To"+nursery.getEndTime());
        holder.age.setText("age:"+nursery.getMinAge()+"To"+nursery.getMaxAge());
        likeButton=holder.like_btn;
        holder.likesCount.setText("1");
        holder.navBar.setVisibility(View.VISIBLE);
        holder.body.setVisibility(View.VISIBLE);
        holder.slider.setVisibility(View.VISIBLE);
        loginAuth();
        checkLike();
        setListeners();

    }
    private void setCommentsData(final ViewHolder holder , int position){
        holder.commentContent.setText(nursery.getComments().get(position).getContent());
    }

    private void isLiked(){
        if (nursery.getLikes().contains(Auth.getLoggedUser().getId()))
            liked=true;
        else
            liked=false;
    }
    private void checkLike(){
        isLiked();
        if (liked)
            likeButton.setImageResource(R.drawable.favorite_main);
        else
            likeButton.setImageResource(R.drawable.favorite);

    }

    private void likeToggle(){
        if (liked){

            ArrayList<String> likes = nursery.getLikes();
            if (likes.contains(Auth.getLoggedUser().getId()))
                likes.remove(Auth.getLoggedUser().getId());
            nursery.setLikes(likes);

        }
        else {

            ArrayList<String> likes = nursery.getLikes();
            if (!likes.contains(Auth.getLoggedUser().getId()))
            likes.add(Auth.getLoggedUser().getId());
            nursery.setLikes(likes);
        }
        //TODO make sure to use sync or save
        nursery.updateData();
        checkLike();
    }
private void setListeners(){

    likeButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            likeToggle();
            checkLike();
        }
    });
}

private void loginAuth(){
    if (Auth.getLoggedUser()==null)
    {
        likeButton.setVisibility(View.INVISIBLE);
        //TODO hide comment section
    }
}




    public class ViewHolder extends RecyclerView.ViewHolder {
        public final SliderLayout slider;
        public final LinearLayout navBar;
        public final ImageView instagram;
        public final ImageView snapChat;
        public final ImageView whats;
        public final ImageView facebook;
        public final ImageView phone1;
        public final ImageView phone2;
        public final ImageView location;
        public final LinearLayout body;
        public final TextView nurseryName;
        public final TextView city;
        public final TextView description;
        public final TextView descriptionData;
        public final TextView specialNeeds;
        public final TextView bus;
        public final TextView swimming;
        public final TextView arabic;
        public final TextView english;
        public final TextView meal;
        public final TextView time;
        public final TextView age;
        public final View sperator;
        public final TextView likesCount;
        public final ImageView like_btn;
        public final TextView commentContent;

        public ViewHolder(View view) {
            super(view);

            slider =(SliderLayout) view.findViewById(R.id.nurseryProfileSlider);
            instagram =(ImageView) view.findViewById(R.id.np_instagram);
            snapChat=(ImageView) view.findViewById(R.id.np_snapchat);
            whats=(ImageView) view.findViewById(R.id.np_whats);
            facebook=(ImageView) view.findViewById(R.id.np_facebook);
            phone1=(ImageView) view.findViewById(R.id.np_phone1);
            phone2=(ImageView) view.findViewById(R.id.np_phone2);
            location=(ImageView) view.findViewById(R.id.np_location);
            body=(LinearLayout) view.findViewById(R.id.np_body);
            nurseryName = (TextView) view.findViewById(R.id.np_nurseryName);
            city = (TextView) view.findViewById(R.id.np_city);
            description = (TextView) view.findViewById(R.id.np_description);
            descriptionData = (TextView) view.findViewById(R.id.np_nurseryDescription);
            specialNeeds= (TextView) view.findViewById(R.id.np_specialNeeds);
            bus = (TextView) view.findViewById(R.id.np_bus);
            swimming= (TextView) view.findViewById(R.id.np_swimming);
            arabic = (TextView) view.findViewById(R.id.np_arabic);
            english = (TextView) view.findViewById(R.id.np_english);
            meal= (TextView) view.findViewById(R.id.np_Meal);
            age = (TextView) view.findViewById(R.id.np_age);
            time = (TextView) view.findViewById(R.id.np_time);
            navBar= (LinearLayout) view.findViewById(R.id.np_navBar);
            sperator= (View) view.findViewById(R.id.np_descriptionSp);
            likesCount=(TextView) view.findViewById(R.id.np_likesNum);
            like_btn = (ImageView) view.findViewById(R.id.np_likeBtn);
            commentContent = (TextView) view.findViewById(R.id.np_comment);


        }

        @Override
        public String toString() {
            return super.toString() + " '" +  "'";
        }
    }
}
