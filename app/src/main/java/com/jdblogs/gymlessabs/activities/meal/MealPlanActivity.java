/*
 * Created by Jonny Deehan
 * Copyright (c) 2017. All rights reserved.
 */

package com.jdblogs.gymlessabs.activities.meal;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.jdblogs.gymlessabs.R;
import com.jdblogs.gymlessabs.datahandling.GlobalVariables;
import com.jdblogs.gymlessabs.datahandling.MealPlanGenerator;
import com.jdblogs.gymlessabs.models.DailyMealPlan;
import java.util.ArrayList;
import java.util.List;

public class MealPlanActivity extends AppCompatActivity {

    private List<DailyMealPlan> mealPlanList = new ArrayList<DailyMealPlan>();
    private MealPlanGenerator mealPlanGenerator;
    private GlobalVariables globalVariables;
    private TextView ingredientsTextView;
    private TextView currentWeekTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);

        generateMealPlan();
        setUpUI();

    }

    private void generateMealPlan(){
        globalVariables = GlobalVariables.getInstance();
        mealPlanGenerator = new MealPlanGenerator(globalVariables.getWeekSelected(),
                globalVariables.getDaySelected());
        String mealPlanData = getResources().getString(R.string.meal_plan);
        mealPlanList = mealPlanGenerator.generateMealPlan(mealPlanData);
    }

    private void setUpUI(){
        ListView listView = (ListView) findViewById(R.id.mealPlanList);
        MealPlanAdapter customAdapter = new MealPlanAdapter(this,mealPlanList);
        listView.setAdapter(customAdapter);
        currentWeekTextView = (TextView) findViewById(R.id.currentWeek);
        currentWeekTextView.setText(globalVariables.getWeekSelected());
        ingredientsTextView = (TextView) findViewById(R.id.foodIngredientsKey);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            ingredientsTextView.setText( Html.fromHtml("<font color=\"#0E84B4\">Protein</font><br />" +
                    "<font color=\"#ffff00\">Fats</font><br />" +
                    "<font color=\"#337658\">Fibrous Carbs</font><br />" +
                    "<font color=\"#6668BF\">Starchy Carbs</font><br />",Html.FROM_HTML_MODE_LEGACY));
        }
        else{
            ingredientsTextView.setText( Html.fromHtml("<font color=\"#0E84B4\">Protein</font><br />" +
                    "<font color=\"#ffff00\">Fats</font><br />" +
                    "<font color=\"#337658\">Fibrous Carbs</font><br />" +
                    "<font color=\"#6668BF\">Starchy Carbs</font><br />"));
        }
    }

    private static class MealPlanAdapter extends ArrayAdapter<DailyMealPlan>{

        MealPlanAdapter(Context context, List<DailyMealPlan> mealPlans){
            super(context,R.layout.content_meal_plan,mealPlans);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View customView = inflater.inflate(R.layout.custom_meal_plan_layout,parent,false);

            final DailyMealPlan mealPlan = getItem(position);
            TextView dayOfTheWeek = (TextView) customView.findViewById(R.id.dayTextView);
            TextView breakfast = (TextView) customView.findViewById(R.id.breakfastInfo);
            TextView snack1 = (TextView) customView.findViewById(R.id.snack1Info);
            TextView lunch = (TextView) customView.findViewById(R.id.lunchInfo);
            TextView snack2 = (TextView) customView.findViewById(R.id.snack2Info);
            TextView dinner = (TextView) customView.findViewById(R.id.dinnerInfo);

            try{
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    dayOfTheWeek.setText(Html.fromHtml(mealPlan.getDay(),Html.FROM_HTML_MODE_LEGACY));
                    breakfast.setText(Html.fromHtml(mealPlan.getBreakfast().substring(11),Html.FROM_HTML_MODE_LEGACY));
                    snack1.setText(Html.fromHtml(mealPlan.getSnack1().substring(9),Html.FROM_HTML_MODE_LEGACY));
                    lunch.setText(Html.fromHtml(mealPlan.getLunch().substring(7),Html.FROM_HTML_MODE_LEGACY));
                    snack2.setText(Html.fromHtml(mealPlan.getSnack2().substring(9),Html.FROM_HTML_MODE_LEGACY));
                    dinner.setText(Html.fromHtml(mealPlan.getDinner().substring(8),Html.FROM_HTML_MODE_LEGACY));
                }else {
                    dayOfTheWeek.setText(Html.fromHtml(mealPlan.getDay()));
                    breakfast.setText(Html.fromHtml(mealPlan.getBreakfast().substring(11)));
                    snack1.setText(Html.fromHtml(mealPlan.getSnack1().substring(9)));
                    lunch.setText(Html.fromHtml(mealPlan.getLunch().substring(7)));
                    snack2.setText(Html.fromHtml(mealPlan.getSnack2().substring(9)));
                    dinner.setText(Html.fromHtml(mealPlan.getDinner().substring(8)));
                }
            } catch(Error e){
                Log.i(getClass().getSimpleName(), e.toString());
            }

            return customView;
        }
    }

    public void onBackButton(View view){
        finish();
    }

}