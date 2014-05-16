package com.mwilliamson.d3lootlog;

import android.view.View;

public interface Injectable
{
    void injectViews(View rootView, Class clazz) throws IllegalAccessException;
}
