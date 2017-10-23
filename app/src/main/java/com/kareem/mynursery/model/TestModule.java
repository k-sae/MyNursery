package com.kareem.mynursery.model;

/**
 * Created by kareem on 10/23/17.
 */

public class TestModule {
    private String id;
    public String value;

    public TestModule(String id, String value) {
        this.id = id;
        this.value = value;
    }
    public TestModule() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
