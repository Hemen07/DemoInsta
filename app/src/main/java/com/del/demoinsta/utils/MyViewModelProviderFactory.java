package com.del.demoinsta.utils;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.del.demoinsta.data.local.db.DatabaseService;

import java.lang.reflect.InvocationTargetException;

/**
 * Non - generic version.
 */
class MyViewModelProviderFactory implements ViewModelProvider.Factory {

    private final DatabaseService databaseService;

    private String mName = "sth";

    public MyViewModelProviderFactory(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        // way -1
        try {
            return modelClass.getConstructor(DatabaseService.class, String.class).newInstance(databaseService, mName);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();

        }
        return null;

    }
}
