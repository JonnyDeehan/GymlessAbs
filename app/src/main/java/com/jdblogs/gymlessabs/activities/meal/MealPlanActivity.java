package com.jdblogs.gymlessabs.activities.meal;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);

        globalVariables = GlobalVariables.getInstance();

        mealPlanGenerator = new MealPlanGenerator(globalVariables.getWeekSelected(),
                globalVariables.getDaySelected());
        String mealPlanData = getResources().getString(R.string.meal_plan);
        mealPlanList = mealPlanGenerator.generateMealPlan(mealPlanData);

        ListView listView = (ListView) findViewById(R.id.mealPlanList);
        MealPlanAdapter customAdapter = new MealPlanAdapter(this,mealPlanList);
        listView.setAdapter(customAdapter);
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
            TextView mealPlanInfoTextView = (TextView) customView.findViewById(R.id.mealPlanInfo);

            try{
                mealPlanInfoTextView.setText(mealPlan.getWeek() + "\n" + mealPlan.getDay() + "\n"
                        + mealPlan.getBreakfast() + "\n" + mealPlan.getSnack1() + "\n"
                        + mealPlan.getLunch() + "\n" + mealPlan.getSnack2() + "\n"
                        + mealPlan.getDinner());
            } catch(Error e){
                Log.i(getClass().getSimpleName(), e.toString());
            }

            return customView;
        }
    }

    public void onBackButton(View view){
        finish();
    }


    public void logMessage(String message){
        Log.i(getClass().getSimpleName(), message);
    }

}