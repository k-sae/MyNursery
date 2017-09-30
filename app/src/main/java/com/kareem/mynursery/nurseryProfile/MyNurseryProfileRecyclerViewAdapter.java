package com.kareem.mynursery.nurseryProfile;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.kareem.mynursery.R;
import com.kareem.mynursery.model.Auth;
import com.kareem.mynursery.model.Comment;
import com.kareem.mynursery.model.Nursery;
import com.kareem.mynursery.model.ObjectChangedListener;
import com.kareem.mynursery.model.RealTimeObject;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link }.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyNurseryProfileRecyclerViewAdapter extends RecyclerView.Adapter<MyNurseryProfileRecyclerViewAdapter.ViewHolder> {

    private  Nursery nursery;
    private String nurseryId;
    private Context context;
    private boolean liked;
    private ImageView likeButton;
    private int likeNum=0;


    public MyNurseryProfileRecyclerViewAdapter(final Context context, final String nurseryId) {
       this.context=context;
       this.nurseryId=nurseryId;
        nursery =new Nursery();
        nursery.setId(nurseryId);
       nursery.startSync();
        nursery.setOnChangeListener(new ObjectChangedListener() {
            @Override
            public void onChange(RealTimeObject realTimeObject) {
                nursery = (Nursery) realTimeObject;
                notifyDataSetChanged();
            }
        });


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
        else if (position == getItemCount()-1)addCommentLayout(holder);
       else setCommentsData(holder ,position-1);
        setListeners(holder);
        checkLike();

    }

    @Override
    public int getItemCount() {
        return nursery.getComments().size()+2;
    }


    private void setNurseryData(final ViewHolder holder){
        for (String image : nursery.getImagesId()){
            image=Nursery.BASE_IMAGE_URL+image;
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
        if (nursery.isArabic())
            holder.arabic.setText("Arabic");
        if (nursery.isEnglish())
            holder.english.setText("English");
        if (nursery.isBus())
            holder.bus.setText("Bus");
        if (nursery.isSupportingDisablilites())
            holder.specialNeeds.setText("Special Needs");
        holder.time.setText(nursery.getStartTime()+" To "+nursery.getEndTime());
        holder.age.setText("age:"+nursery.getMinAge()+" To "+nursery.getMaxAge());
        likeButton=holder.like_btn;
        holder.likesCount.setText(String.valueOf(nursery.getLikes().size()));
        holder.navBar.setVisibility(View.VISIBLE);
        holder.body.setVisibility(View.VISIBLE);
        holder.slider.setVisibility(View.VISIBLE);
        holder.commentContent.setVisibility(View.GONE);
        holder.addCommentSection.setVisibility(View.GONE);

        loginAuth();
       checkLike();

        holder.whats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("smsto:" + nursery.getWhatsapp());
                Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                i.putExtra("sms_body", "");
                i.setPackage("com.whatsapp");
                try {

                    context.startActivity(i);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "Whatsapp not installed", Toast.LENGTH_SHORT).show();
                }

            }
        });
        holder.instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String instaUrl;
                if (nursery.getInstagram().contains("http://instagram.com"))
                     instaUrl=nursery.getInstagram();
                else  instaUrl="http://instagram.com/"+nursery.getInstagram();
                Uri uri = Uri.parse(instaUrl);
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    context.startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(instaUrl)));
                }
            }
        });
        holder.facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String faceUrl;
                if (nursery.getFacebook().contains("https://www.facebook.com/"))
                    faceUrl=nursery.getFacebook();
                else
                    faceUrl="https://www.facebook.com/"+nursery.getFacebook();

                    Intent intent= new Intent(Intent.ACTION_VIEW,
                            Uri.parse(faceUrl)); //catches and opens a url to the desired page
                    context.startActivity(intent);


            }
        });
        holder.phone1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", nursery.getPhone1(), null));
                context.startActivity(intent);
            }
        });
        holder.phone2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", nursery.getPhone2(), null));
                context.startActivity(intent);
            }
        });
        holder.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("geo:" + nursery.getLatitude()
                                    + "," + nursery.getLongitude()
                                    + "?q=" + nursery.getLatitude()
                                    + "," + nursery.getLongitude()));

                    intent.setComponent(new ComponentName(
                            "com.google.android.apps.maps",
                            "com.google.android.maps.MapsActivity"));
                    context.startActivity(intent);
                } catch (ActivityNotFoundException e) {

                    try {
                        context.startActivity(new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=com.google.android.apps.maps")));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        context.startActivity(new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=com.google.android.apps.maps")));
                    }

                    e.printStackTrace();
                }
            }
        });

    }
    private void setCommentsData(final ViewHolder holder , int position){
        //TODO find nursery
        holder.commentContent.setText(nursery.getComments().get(position).getContent());
        holder.commentContent.setVisibility(View.VISIBLE);
        holder.navBar.setVisibility(View.GONE);
        holder.body.setVisibility(View.GONE);
        holder.slider.setVisibility(View.GONE);
        holder.addCommentSection.setVisibility(View.GONE);
    }

    private void isLiked(){

        if (nursery.getLikes().size()>0&&nursery.getLikes().contains(Auth.getLoggedUser().getId()))
            liked=true;
        else
            liked=false;

    }
    private void checkLike(){
        if (Auth.getLoggedUser()!=null) {

            isLiked();
            if (liked)
                likeButton.setImageResource(R.drawable.favorite_main);
            else
                likeButton.setImageResource(R.drawable.favorite);
        }
    }

    private void likeToggle(){

        if (liked){

            ArrayList<String> likes = nursery.getLikes();
            if (likes.contains(Auth.getLoggedUser().getId()))
              nursery.disLike();

        }
        else {

            ArrayList<String> likes = nursery.getLikes();
            if (!likes.contains(Auth.getLoggedUser().getId()))
            nursery.like();
        }
        checkLike();
    }
private void setListeners(final ViewHolder holder){

    likeButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            likeToggle();

        }
    });
    holder.addCommentBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String comment_content =holder.commentField.getText().toString();
            Comment comment = new Comment();
            if (!comment_content.equals("")){

            comment.setContent(comment_content);
                //TODO set date
            comment.setDate("");
                comment.setName(Auth.getLoggedUser().getName());
                nursery.addComment(comment);
            }

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


    public void addCommentLayout(final ViewHolder holder){

        holder.addCommentSection.setVisibility(View.VISIBLE);
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
        public final EditText commentField;
        public final Button addCommentBtn;
        public final LinearLayout addCommentSection;

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
            commentField =(EditText) view.findViewById(R.id.np_commentField);
            addCommentBtn = (Button) view.findViewById(R.id.np_addCommentBtn);
            addCommentSection = (LinearLayout) view.findViewById(R.id.np_addCommentSection);


        }

        @Override
        public String toString() {
            return super.toString() + " '" +  "'";
        }
    }
}
