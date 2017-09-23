package com.kareem.mynursery.model;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by kareem on 9/16/17.
 */

public class Auth {
    private static User loggedUser ;
    public static User getLoggedUser()
    {
        if (loggedUser == null ){
            //TODO
            if (!FirebaseAuth.getInstance().getCurrentUser().isAnonymous())

              loggedUser = User.get(FirebaseAuth.getInstance().getCurrentUser().getUid());
            }

       return loggedUser;
    }

    public static User getLoggedUser(ObjectChangedListener objectChangedListener)
    {
        if (loggedUser == null && FirebaseAuth.getInstance().getCurrentUser() != null){
            if (!FirebaseAuth.getInstance().getCurrentUser().isAnonymous())

                loggedUser = User.get(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
        loggedUser.setOnChangeListener(objectChangedListener);
        return loggedUser;
    }
}
