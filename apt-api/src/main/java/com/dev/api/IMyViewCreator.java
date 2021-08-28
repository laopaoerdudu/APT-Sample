package com.dev.api;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public interface IMyViewCreator {
    View createView(String name, Context context, AttributeSet attributeSet);
}
