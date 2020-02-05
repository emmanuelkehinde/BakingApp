package com.kehinde.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import com.kehinde.bakingapp.R;
import com.kehinde.bakingapp.models.Ingredient;
import com.kehinde.bakingapp.widget.db.IngredientContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kehinde on 6/12/17.
 */

public class WidgetIntentService extends RemoteViewsService {


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }


    class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        private Context mContext;
        private int mAppWidgetId;
        private Cursor cursor;


        public WidgetRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }



        private void initCursor(){
            if (cursor != null) {
                cursor.close();
            }

            Uri uri = IngredientContract.CONTENT_URI;
            cursor = mContext.getContentResolver().query(uri,null,null,null,null);

        }

        @Override
        public void onCreate() {
            initCursor();

        }

        @Override
        public void onDataSetChanged() {
            initCursor();
        }

        @Override
        public void onDestroy() {
            cursor.close();
        }

        @Override
        public int getCount() {
            return cursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            // Construct a remote views item based on the app widget item XML file,
            // and set the text based on the position.
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);

            if (cursor.getCount()!=0) {
                cursor.moveToPosition(position);

                rv.setTextViewText(R.id.widget_ingredient_name, cursor.getString(3));

                String measure = cursor.getString(1) + " " +
                        cursor.getString(2);

                rv.setTextViewText(R.id.widget_ingredient_measure, measure);
            }


            // Return the remote views object.
            return rv;

        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }


    }

}
