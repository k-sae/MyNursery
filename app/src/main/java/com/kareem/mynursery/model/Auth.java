package com.kareem.mynursery.model;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
            if (FirebaseAuth.getInstance().getCurrentUser() ==null)
                FirebaseAuth.getInstance().signInAnonymously();
            else
            if (  !FirebaseAuth.getInstance().getCurrentUser().isAnonymous())

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
    public static Task<AuthResult> logout()
    {
        FirebaseAuth.getInstance().signOut();
        loggedUser = null;
        return FirebaseAuth.getInstance().signInAnonymously();
    }
}
