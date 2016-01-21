package barqsoft.footballscores;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import barqsoft.footballscores.service.myFetchService;

/**
 * Implementation of App Widget functionality.
 */
public class GameScoreWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;

        context.startService(new Intent(context, myFetchService.class));
       // context.startService(new Intent(context, ScoresWidgetIntentService.class));

        for (int i = 0; i < N; i++) {

            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }


    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {


        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context,
                GameScoreWidget.class));
        final int N = appWidgetIds.length;
        //context.startService(new Intent(context, ScoresWidgetIntentService.class));
        for (int i = 0; i < N; i++) {

            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);
        if (MainActivity.ACTION_DATA_UPDATED.equals(intent.getAction())) {

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            //context.startService(new Intent(context, ScoresWidgetIntentService.class));
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context,
                    GameScoreWidget.class));
            final int N = appWidgetIds.length;
            //context.startService(new Intent(context, ScoresWidgetIntentService.class));
            for (int i = 0; i < N; i++) {

                updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
            }
        }
    }

/*
    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(
                new ComponentName("barqsoft.footballscores.MainActivity",".GameScoreWidget"),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(
                new ComponentName("barqsoft.footballscores.MainActivity",".GameScoreWidget"),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
*/


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        /*Cursor cursor = context.getContentResolver().query(
                DatabaseContract.scores_table.buildScoreWithId(),
                null,
                null,
                new String[]{MainActivity.current_match_info}, null
        );
        */

        CharSequence widgetText = context.getString(R.string.appwidget_text);
         //Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.game_score_widget);
        views.setTextViewText(R.id.appwidget_text, MainActivity.current_match_info);
        /*if(cursor != null)
        {
            cursor.moveToFirst();
            views.setTextViewText(R.id.appwidget_text, cursor.getString(6) + " " + cursor.getString(7));
            //cursor.getString()
            //views.setTextViewText(R.id.appwidget_text, MainActivity.current_match_info);
        }*/



        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}

