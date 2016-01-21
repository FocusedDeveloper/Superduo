package barqsoft.footballscores.service;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.GameScoreWidget;
import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.Utilies;

public class ScoresWidgetIntentService extends IntentService {

    public static final String ACTION_DATA_UPDATED = "barqsoft.footballscores.ACTION_DATA_UPDATED";
    private static final String[] SCORE_COLUMNS = {
            DatabaseContract.scores_table.HOME_COL,
            DatabaseContract.scores_table.AWAY_COL,
            DatabaseContract.scores_table.HOME_GOALS_COL,
            DatabaseContract.scores_table.AWAY_GOALS_COL,
            DatabaseContract.scores_table.MATCH_DAY,
            DatabaseContract.scores_table.MATCH_ID,
            DatabaseContract.scores_table.TIME_COL,
    };


    public static final int INDEX_COL_HOME = 0;
    public static final int INDEX_COL_AWAY = 1;
    public static final int INDEX_COL_HOME_GOALS =2;
    public static final int INDEX_COL_AWAY_GOALS = 3;
    public static final int INDEX_COL_MATCHDAY = 4;
    public static final int INDEX_COL_ID = 5;
    public static final int INDEX_COL_MATCHTIME = 6;

    public ScoresWidgetIntentService() {
        super("WidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        // Retrieve all of the Today widget ids: these are the widgets we need to update
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                GameScoreWidget.class));

        // Get today's data from the ContentProvider
        Uri scoreWithIDUri = DatabaseContract.scores_table.buildScoreWithId();
        //String date = Long.toString(System.currentTimeMillis());
        //String[] dates = {date};
        int j = MainActivity.selected_match_id;

       // Cursor cursor = getContentResolver().query(scoreWithIDUri, SCORE_COLUMNS, null,
            //    dates, DatabaseContract.scores_table.MATCH_ID);

        Cursor cursor = getContentResolver().query(
                scoreWithIDUri,
                SCORE_COLUMNS,
                null,
                new String[]{MainActivity.current_match_info}, null
        );

        if (cursor == null) {
            Log.i("LOG cursor: ", "We Do NOT have a result");
            return;
        }
        if (!cursor.moveToFirst()) {
            cursor.close();
            Log.i("LOG cursor: ", "We have a result");
            return;
        }
        // Extract the data from the Cursor
        //double scoreID = cursor.getDouble(INDEX_COL_ID);
        String homeName = cursor.getString(INDEX_COL_HOME);
        String awayName = cursor.getString(INDEX_COL_AWAY);
        int homeCrest = Utilies.getTeamCrestByTeamName(homeName,this);
        int awayCrest = Utilies.getTeamCrestByTeamName(awayName,this);
        int homeScore = cursor.getInt(INDEX_COL_HOME_GOALS);
        int awayScore = cursor.getInt(INDEX_COL_AWAY_GOALS);
        String time = cursor.getString(INDEX_COL_MATCHTIME);
        cursor.close();

        Log.i("LOG Home Name: ", homeName);
        Log.i("LOG Away Name: ", awayName);
        Log.i("LOG Score: ", homeScore + " " + awayScore);
        Log.i("LOG Time: ", time);

      /* for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.game_score_widget);

            // Add the data to the RemoteViews
            //display team icons
            views.setImageViewResource(R.id.widget_home_crest, homeCrest);
            views.setImageViewResource(R.id.widget_away_crest, awayCrest);
            //display team names
            views.setTextViewText(R.id.widget_home_name,homeName);
            views.setTextViewText(R.id.widget_home_name,awayName);
            //display scores and time
            views.setTextViewText(R.id.widget_score_textview,Utilies.getScores(homeScore,awayScore));
            views.setTextViewText(R.id.widget_data_textview,time);

            // Content Descriptions for RemoteViews were only added in ICS MR1
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                views.setContentDescription(R.id.widget_home_crest, homeName);
                views.setContentDescription(R.id.widget_away_crest, awayName);
            }

//             Create an Intent to launch MainActivity
            Intent launchIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.scores_widget, pendingIntent);



            // Tell the AppWidgetManager to perform an update on the current app widget_info
            appWidgetManager.updateAppWidget(appWidgetId, views);


        }
        */


    }
}