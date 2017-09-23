package com.kareem.mynursery.model.FirebaseParser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Exclude;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kareem on 9/15/17.
 */

@SuppressWarnings("TryWithIdenticalCatches")
public class ObjectParser {

    public <T> T getValue(Class<T> cls, DataSnapshot dataSnapshot) {
        try {
            return  getValue(cls.newInstance() , dataSnapshot );
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T getValue( T object, DataSnapshot dataSnapshot) {
        Class<?> cls = object.getClass();
        boolean accessibility;
        for (DataSnapshot snapshot: dataSnapshot.getChildren()
             ) {
            try {
                Field field = cls.getDeclaredField(snapshot.getKey());
                accessibility = field.isAccessible();
                field.setAccessible(true);
                if (isKeyList(field)) setListValues(snapshot, field, object);
                else field.set(object, snapshot.getValue() );
                field.setAccessible(accessibility);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return object;
    }


    private <T>  void setListValues(DataSnapshot dataSnapshot, Field field, T object)
    {
        ArrayList<String> list = new ArrayList<>();
        for (DataSnapshot snapshot: dataSnapshot.getChildren()
                ) {
            list.add(snapshot.getKey());
        }
        try {
            field.set(object, list);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private boolean isKeyList(Field field)
    {
        return  (field.getAnnotation(KeyList.class) != null);

    }
    private boolean isExcluded(Field field){
        return (field.getAnnotation(Exclude.class) != null);
    }

    public <T> Map<String, Object > mapObject(T object)
    {
        Map<String, Object> stringObjectMap = new HashMap<>();
        Class<?> cls = object.getClass();
        boolean accessibility;
        for (Field field :
                cls.getDeclaredFields()) {
            accessibility = field.isAccessible();
             if (!isExcluded(field) && !isKeyList(field) && !field.isSynthetic() && !field.getName().equalsIgnoreCase("serialVersionUID")) try {
                 field.setAccessible(true);
                stringObjectMap.put(field.getName(), field.get(object));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            field.setAccessible(accessibility);
        }
        return stringObjectMap;
    }

//
}
