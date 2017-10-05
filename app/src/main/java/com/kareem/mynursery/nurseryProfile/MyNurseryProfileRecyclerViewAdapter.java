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
import com.kareem.mynursery.model.User;
import com.kareem.mynursery.nursery.AddNursery;

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
    private Button likeButton;
    private Button edit;
    private Button delete;
    private ImageView favBtn;
    private int likeNum=0;
private User current_user;


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
current_user=Auth.getLoggedUser();
        if (current_user!=null) {
            current_user.startSync();
            current_user.setOnChangeListener(new ObjectChangedListener() {
                @Override
                public void onChange(RealTimeObject realTimeObject) {
                    notifyDataSetChanged();
                    current_user = (User) realTimeObject;
                }
            });
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==0){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_nurseryprofile, parent, false);
            return new ViewHolder(view);
        }
        else if(viewType==1){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.comment, parent, false);
            return new ViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.add_comment, parent, false);
            return new ViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

       if (position==0) {setNurseryData(holder);
           setNurseryListeners(holder);
           checkLike();
           checkFavorite();}
        else if (position == getItemCount()-1){addCommentLayout(holder);}
       else { setCommentsData(holder ,position-1);}


    }

    @Override
    public int getItemCount() {
        return nursery.getComments().size()+2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0) return position; //nursery data
        if (position==getItemCount()-1) return 2; //comments
        return 1; // add comment
    }

    private void setNurseryData(final ViewHolder holder){


        final SliderLayout slider;
         LinearLayout navBar;
         ImageView instagram;
         ImageView snapChat;
         ImageView whats;
         ImageView facebook;
         ImageView phone1;
         ImageView phone2;
         ImageView location;
         LinearLayout body;
         TextView nurseryName;
         TextView city;
         TextView description;
         TextView descriptionData;
         TextView specialNeeds;
         TextView bus;
         TextView swimming;
         TextView arabic;
         TextView english;
         TextView meal;
         TextView time;
         TextView age;
         View sperator;
         TextView likesCount;
         Button like_btn;
        ImageView favBtn;
        Button edit;
        Button delete;
        slider =(SliderLayout) holder.holderView.findViewById(R.id.nurseryProfileSlider);
        instagram =(ImageView) holder.holderView.findViewById(R.id.np_instagram);
        snapChat=(ImageView) holder.holderView.findViewById(R.id.np_snapchat);
        whats=(ImageView) holder.holderView.findViewById(R.id.np_whats);
        facebook=(ImageView) holder.holderView.findViewById(R.id.np_facebook);
        phone1=(ImageView) holder.holderView.findViewById(R.id.np_phone1);
        phone2=(ImageView) holder.holderView.findViewById(R.id.np_phone2);
        location=(ImageView) holder.holderView.findViewById(R.id.np_location);
        body=(LinearLayout) holder.holderView.findViewById(R.id.np_body);
        nurseryName = (TextView) holder.holderView.findViewById(R.id.np_nurseryName);
        city = (TextView) holder.holderView.findViewById(R.id.np_city);
        description = (TextView) holder.holderView.findViewById(R.id.np_description);
        descriptionData = (TextView) holder.holderView.findViewById(R.id.np_nurseryDescription);
        specialNeeds= (TextView) holder.holderView.findViewById(R.id.np_specialNeeds);
        bus = (TextView) holder.holderView.findViewById(R.id.np_bus);
        swimming= (TextView) holder.holderView.findViewById(R.id.np_swimming);
        arabic = (TextView) holder.holderView.findViewById(R.id.np_arabic);
        english = (TextView) holder.holderView.findViewById(R.id.np_english);
        meal= (TextView) holder.holderView.findViewById(R.id.np_Meal);
        age = (TextView) holder.holderView.findViewById(R.id.np_age);
        time = (TextView) holder.holderView.findViewById(R.id.np_time);
        navBar= (LinearLayout) holder.holderView.findViewById(R.id.np_navBar);
        sperator= (View) holder.holderView.findViewById(R.id.np_descriptionSp);
        likesCount=(TextView) holder.holderView.findViewById(R.id.np_likesNum);
        like_btn = (Button) holder.holderView.findViewById(R.id.navigation_like);
        favBtn =(ImageView)holder.holderView.findViewById(R.id.np_favBtn) ;
        edit = (Button)holder.holderView.findViewById(R.id.navigation_edit);
        delete = (Button)holder.holderView.findViewById(R.id.navigation_delete);





        slider.removeAllSliders();
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
           slider.addSlider(textSliderView);

        }
       slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(4000);
        nurseryName.setText(nursery.getName());
        city.setText(nursery.getCity());
        description.setText(R.string.description);
        sperator.setVisibility(View.VISIBLE);
        descriptionData.setText(nursery.getDescription());
        if (nursery.getActivities().contains("SWIMMING"))
        swimming.setText("Swimming");
        if (nursery.isArabic())
            arabic.setText("Arabic");
        if (nursery.isEnglish())
            english.setText("English");
        if (nursery.isBus())
            bus.setText("Bus");
        if (nursery.isSupportingDisablilites())
            specialNeeds.setText("Special Needs");
        time.setText(nursery.getStartTime()+" To "+nursery.getEndTime());
        age.setText("age:"+nursery.getMinAge()+" To "+nursery.getMaxAge());
        likeButton=like_btn;
        this.edit=edit;
        this.delete=delete;
        this.favBtn=favBtn;
        likesCount.setText(String.valueOf(nursery.getLikes().size()));



        loginAuth();
       checkLike();

        whats.setOnClickListener(new View.OnClickListener() {
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
        instagram.setOnClickListener(new View.OnClickListener() {
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
        facebook.setOnClickListener(new View.OnClickListener() {
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
        phone1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", nursery.getPhone1(), null));
                context.startActivity(intent);
            }
        });
        phone2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", nursery.getPhone2(), null));
                context.startActivity(intent);
            }
        });
        location.setOnClickListener(new View.OnClickListener() {
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
        TextView commentContent;
        commentContent = (TextView) holder.holderView.findViewById(R.id.np_comment);

       commentContent.setText(nursery.getComments().get(position).getContent());

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
                likeButton.setText("DisLike");
            else
                likeButton.setText("Like");
        }
    }
    private void checkFavorite(){

        if (current_user!=null&&current_user.getFavourites().contains(nurseryId))
            favBtn.setImageResource(R.drawable.favorite_main);
        else
            favBtn.setImageResource(R.drawable.favorite);

    }
    private void favToggle(){
        if (!Auth.getLoggedUser().getFavourites().contains(nurseryId))
        { Auth.getLoggedUser().addFavourite(nurseryId);
        }
        else {
            Auth.getLoggedUser().removeFavourite(nurseryId);
        }
        checkFavorite();
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
private void setNurseryListeners(final ViewHolder holder){
    if (current_user!=null){
    Button like_btn;
    like_btn = (Button) holder.holderView.findViewById(R.id.navigation_like);
    like_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            likeToggle();

        }
    });
    favBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            favToggle();
        }
    });



    edit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context , AddNursery.class);
            intent.putExtra("action",AddNursery.EDIT_NURSERY);
            intent.putExtra(AddNursery.ID,nurseryId);
            context.startActivity(intent);

        }
    });
    delete.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Auth.getLoggedUser().removeNursery(nurseryId);
            nursery.delete();
        }
    });
}}

private void loginAuth() {
    if (Auth.getLoggedUser() == null) {
        likeButton.setVisibility(View.INVISIBLE);
        //TODO hide comment section
    }
    else {
        likeButton.setVisibility(View.VISIBLE);
    }
if (Auth.getLoggedUser() == null || !Auth.getLoggedUser().getNurseries().contains(nurseryId)) {
    edit.setVisibility(View.GONE);
    delete.setVisibility(View.GONE);
}
else {
    edit.setVisibility(View.VISIBLE);
    delete.setVisibility(View.VISIBLE);
}
}


    public void addCommentLayout(final ViewHolder holder){
       final EditText commentField;
        Button addCommentBtn;
        LinearLayout addCommentSection;
        commentField =(EditText) holder.holderView.findViewById(R.id.np_commentField);
        addCommentBtn = (Button)  holder.holderView.findViewById(R.id.np_addCommentBtn);

      addCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment_content =commentField.getText().toString();
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

    public class ViewHolder extends RecyclerView.ViewHolder {
         View holderView;





        public ViewHolder(View view) {
            super(view);
            holderView =view;



        }

        @Override
        public String toString() {
            return super.toString() + " '" +  "'";
        }
    }
}
