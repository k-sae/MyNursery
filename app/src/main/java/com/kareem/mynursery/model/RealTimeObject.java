package com.kareem.mynursery.model;

/**
 * Created by kareem on 9/14/17.
 */

public interface RealTimeObject<T> {
    void onChange(T newObject);
}
