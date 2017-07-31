package com.jdblogs.gymlessabs.activities.meal;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
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
import java.util.Arrays;
import java.util.List;

public class MealPlanActivity extends AppCompatActivity {

    private List<DailyMealPlan> mealPlanList = new ArrayList<DailyMealPlan>();
    private MealPlanGenerator mealPlanGenerator;
    private GlobalVariables globalVariables;
    private static final String FATS[] = {
            "olives",
            "bacon",
            "avocado",
            "mixed nuts",
            "string cheese",
            "cheese stick",
            "spare ribs",
            "cheese",
            "halloumi cheese",
            "thai green curry",
            "blue cheese sauce",
            "goats cheese",
            "herb butter",
            "feta",
            "pesto",
            "almond butter",
            "nut and seed granola",
            "porridge",
            "grain free cereal",
            "mozzarella"};

    private static final String PROTEINS[] = {
            "omelet",
            "meatballs",
            "meat pie",
            "Salmon steak",
            "tuna burger",
            "Smoked Salmon",
            "Scrambled egg",
            "fried egg",
            "beef mince",
            "baked fish",
            "hard boiled eggs",
            "steak burger",
            "poached egg",
            "eggs benedict",
            "ham",
            "chicken wings",
            "pepperoni slices",
            "chicken breast",
            "steak",
            "pork belly",
            "beef jerky",
            "prawn",
            "tuna",
            "turkey sausages",
            "lamb burger",
            "flat iron steak",
            "sliced cold meat(chicken, turkey, ham, beef)",
            "pork chop",
            "sardines",
            "mackerel",
            "chicken slices",
            "protein bar",
            "hamburger",
            "baked tilapia(or other white fish)",
            "cod",
            "frittata",
            "sashimi",
            "beef stroganoff",
            "sirloin steak"};

    private static final String FIBROUS_CARBS[] = {"balsamic onion",
            "mushroom",
            "rocket",
            "vegetables",
            "leek",
            "mixed veggies",
            "edamame",
            "asparagus",
            "broccoli",
            "sliced peppers",
            "mixed veg stir fry",
            "salad",
            "spinach",
            "green beans",
            "zucchini/courgette fries",
            "zucchini/courgette spirals",
            "zucchini/courgette spaghetti",
            "lettuce wrap",
            "zucchini/courgette noodles or zoodles",
            "tomato",
            "kale"};

    private static final String STARCHY_CARBS[] =
            {"sweet potato fries",
            "brown rice",
            "rice noodles",
            "sweet potato mash",
            "baked sweet potato",
            "quinoa",
            "rice pasta",
            "jasmine rice",
            "root vegetables",
            "carrot fries",
            "sticky rice",
            "sweet potato chunks",
            "sweet potato wedges"};

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
        Log.i(getClass().getSimpleName(), mealPlanData);
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

            Log.i(getClass().getSimpleName(), "BreakfastStringText: " + mealPlan.getBreakfast());

            try{
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    dayOfTheWeek.setText(Html.fromHtml(mealPlan.getDay(),Html.FROM_HTML_MODE_LEGACY));
                    breakfast.setText(Html.fromHtml(mealPlan.getBreakfast().substring(11),Html.FROM_HTML_MODE_LEGACY));
//                    breakfast.setText(Html.fromHtml("china" + "<font color='#123456'>text</font>",Html.FROM_HTML_MODE_LEGACY));
                    snack1.setText(Html.fromHtml(mealPlan.getSnack1().substring(9),Html.FROM_HTML_MODE_LEGACY));
                    lunch.setText(Html.fromHtml(mealPlan.getLunch().substring(7),Html.FROM_HTML_MODE_LEGACY));
                    snack2.setText(Html.fromHtml(mealPlan.getSnack2().substring(9),Html.FROM_HTML_MODE_LEGACY));
                    dinner.setText(Html.fromHtml(mealPlan.getDinner().substring(8),Html.FROM_HTML_MODE_LEGACY));
                }else {
                    dayOfTheWeek.setText(Html.fromHtml(mealPlan.getDay()));
                    breakfast.setText(Html.fromHtml(mealPlan.getBreakfast().substring(11)));
//                    breakfast.setText(Html.fromHtml("china" + "<font color='#123456'>text</font>"));
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

    private boolean stringContainsItemFromList(String inputStr)
    {
        for(int i =0; i < PROTEINS.length; i++)
        {
            if(inputStr.toLowerCase().contains(PROTEINS[i].toLowerCase()))
            {
                return true;
            }
        }
        for(int i =0; i < FATS.length; i++)
        {
            if(inputStr.toLowerCase().contains(FATS[i].toLowerCase()))
            {
                return true;
            }
        }
        for(int i =0; i < FIBROUS_CARBS.length; i++)
        {
            if(inputStr.toLowerCase().contains(FIBROUS_CARBS[i].toLowerCase()))
            {
                return true;
            }
        }
        for(int i =0; i < STARCHY_CARBS.length; i++)
        {
            if(inputStr.toLowerCase().contains(STARCHY_CARBS[i].toLowerCase()))
            {
                return true;
            }
        }

        return false;
    }

    private void checkForIngrediant(TextView myTextView, String mySearchedString){
        StringBuilder textBuilder = new StringBuilder(myTextView.getText().toString());
        StringBuilder searchedTextBuilder = new StringBuilder((mySearchedString));
        SpannableString spannableString = new SpannableString(myTextView.getText().toString());

        int counter = 0;
        int index = 0;

        for (int i = 0;i < textBuilder.length() - mySearchedString.length() - 1;i++)
        {
            counter = 0;
            if (Character.toLowerCase(textBuilder.charAt(i)) == Character.toLowerCase(searchedTextBuilder.charAt(index)))
            {
                counter++;
                index++;
                for (int j = 1,z = i + 1;j < mySearchedString.length() - 1;j++,z++)
                {
                    if (Character.toLowerCase(textBuilder .charAt(z)) == Character.toLowerCase(searchedTextBuilder .charAt(index)))
                    {
                        counter++;
                        index++;
                    }
                    else
                    {
                        index++;
                        if (index % mySearchedString.length() == 0)
                        {
                            index = 0;
                        }
                        break;
                    }
                }
                if (counter == mySearchedString.length() - 1) // A match
                {
                    spannableString.setSpan(new ForegroundColorSpan(Color.RED), i,
                            i + mySearchedString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // Do the change you want(In this case changing the fore ground color to red)
                    index = 0;
                    continue;
                }
                else
                {
                    index = 0;
                    continue;
                }
            }
        }
        myTextView.setText(spannableString);
    }
}