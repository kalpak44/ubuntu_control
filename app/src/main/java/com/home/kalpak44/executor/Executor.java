package com.home.kalpak44.executor;

import android.content.Context;
import com.home.kalpak44.utilities.MainContext;

/**
 * Created by kalpak44 on 24.05.16.
 */
public class Executor extends BaseExecutor{
    public Executor(Context context) {
        super(((MainContext) context).getConfig());
    }

    public Executor getBaseExecutor(){
        return this;
    }

}
