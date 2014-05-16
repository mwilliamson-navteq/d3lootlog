package com.mwilliamson.d3lootlog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;

public abstract class BaseActivity extends Activity implements Injectable
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        // Finds the activity's root content view and injects the view-tree marked by @InjectView
        ViewGroup rootView = (ViewGroup) findViewById(android.R.id.content);
        try
        {
            injectViews(rootView, this.getClass());
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    public void injectViews(View parentView, Class clazz) throws IllegalAccessException
    {
        for (Field field : clazz.getDeclaredFields())
        {
            if (field.isAnnotationPresent(InjectView.class))
            {
                Class<?> c = field.getType();
                View view = parentView.findViewById(field.getAnnotation(InjectView.class).value());
                field.setAccessible(true); // In case the compiler/runtime environment decides we can't access the field
                field.set(this, c.cast(view));
            }
        }

        // Probably isn't necessary for Activities since it's rare that one would extends an Activity but still use its layout
        // Still, best to make sure
        Class superClass = clazz.getSuperclass();
        if (superClass != null && Injectable.class.isAssignableFrom(superClass))
            injectViews(parentView, superClass);
    }

    protected abstract int getLayoutResource();
}
