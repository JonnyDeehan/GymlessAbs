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
                dayOfTheWeek.setText(mealPlan.getDay());
                breakfast.setText(mealPlan.getBreakfast().substring(11));
                snack1.setText(mealPlan.getSnack1().substring(9));
                lunch.setText(mealPlan.getLunch().substring(7));
                snack2.setText(mealPlan.getSnack2().substring(9));
                dinner.setText(mealPlan.getDinner().substring(8));
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