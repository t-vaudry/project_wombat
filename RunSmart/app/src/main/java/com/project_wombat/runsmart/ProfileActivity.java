package com.project_wombat.runsmart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {
    EditText editAge;
    EditText editName;
    EditText editWeight;
    EditText editHeight;
    Switch editGender;
    Button save;
    DBHandler dbHandler;
    Toast back_toast;
    Toast saved;
    Toast missing_fields;
    TextView textBMI;
    TextView valueBMI;
    TextView textBMR;
    TextView valueBMR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //toast message
        back_toast = Toast.makeText(this, R.string.back_error, Toast.LENGTH_SHORT);
        saved = Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT);
        missing_fields = Toast.makeText(this, R.string.missing_fields, Toast.LENGTH_SHORT);

        //toolbar set
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //showing error or not
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                if(save.getVisibility() == View.VISIBLE )
                {
                    back_toast.show();
                }
                else
                {
                    finish();
                }
            }
        });

        //setting edit text by id
        editAge = (EditText) findViewById(R.id.editAge);
        editName = (EditText) findViewById(R.id.editName);
        editWeight = (EditText) findViewById(R.id.editWeight);
        editHeight = (EditText) findViewById(R.id.editHeight);
        editGender = (Switch) findViewById(R.id.editGender);
        save = (Button) findViewById(R.id.save);

        textBMI = (TextView) findViewById(R.id.textBMI);
        valueBMI = (TextView) findViewById(R.id.valueBMI);
        textBMR = (TextView) findViewById(R.id.textBMR);
        valueBMR = (TextView) findViewById(R.id.valueBMR);

        dbHandler = new DBHandler(this);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        //checking if name is null
        if(dbHandler.getProfile().getId() == 1)
        {
            Profile profile = dbHandler.getProfile();

            editAge.setText(String.valueOf(profile.getAge()));
            editName.setText(profile.getName());
            editGender.setChecked(profile.getSex());
            editWeight.setText(String.valueOf(profile.getWeight()));
            editHeight.setText(String.valueOf(profile.getHeight()));

            calculateStats();

            textBMI.setVisibility(View.VISIBLE);
            valueBMI.setVisibility(View.VISIBLE);
            textBMR.setVisibility(View.VISIBLE);
            valueBMR.setVisibility(View.VISIBLE);

            editAge.setEnabled(false);
            editName.setEnabled(false);
            editWeight.setEnabled(false);
            editHeight.setEnabled(false);
            editGender.setEnabled(false);
            save.setVisibility(View.INVISIBLE);

        }
        //if name is not null
        else
        {
            editAge.setEnabled(true);
            editName.setEnabled(true);
            editWeight.setEnabled(true);
            editHeight.setEnabled(true);
            editGender.setEnabled(true);

            textBMI.setVisibility(View.INVISIBLE);
            valueBMI.setVisibility(View.INVISIBLE);
            textBMR.setVisibility(View.INVISIBLE);
            valueBMR.setVisibility(View.INVISIBLE);
            save.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == R.id.edit)
        {
            editAge.setEnabled(true);
            editName.setEnabled(true);
            editWeight.setEnabled(true);
            editHeight.setEnabled(true);
            editGender.setEnabled(true);

            textBMI.setVisibility(View.INVISIBLE);
            valueBMI.setVisibility(View.INVISIBLE);
            textBMR.setVisibility(View.INVISIBLE);
            valueBMR.setVisibility(View.INVISIBLE);
            save.setVisibility(View.VISIBLE);
        }

        return super.onOptionsItemSelected(item);
    }

    public void savePreferences(View view)
    {
        Profile profile = new Profile();
        int count = 0;

        if(editAge.getText().toString().matches(""))
        {
            editAge.setError(getText(R.string.no_age));
        }
        else if(editAge.getText().toString().length() > 2)
        {
            editAge.setError(getText(R.string.large_age));
        }
        else
        {
            profile.setAge(Integer.parseInt(editAge.getText().toString()));
            count++;
        }

        if(editName.getText().toString().matches(""))
        {
            editName.setError(getText(R.string.no_name));
        }
        else
        {
            profile.setName(editName.getText().toString());
            count++;
        }

        if(editWeight.getText().toString().matches(""))
        {
            editWeight.setError(getText(R.string.no_weight));
        }
        else if(Integer.parseInt(editWeight.getText().toString()) > 200)
        {
            editWeight.setError(getText(R.string.large_weight));
        }
        else
        {
            profile.setWeight(Integer.parseInt(editWeight.getText().toString()));
            count++;
        }

        if(editHeight.getText().toString().matches(""))
        {
            editHeight.setError(getText(R.string.no_height));
        }
        else if(Integer.parseInt(editHeight.getText().toString()) > 215)
        {
            editHeight.setError(getText(R.string.large_height));
        }
        else
        {
            profile.setHeight(Integer.parseInt(editHeight.getText().toString()));
            count++;
        }

        profile.setSex(editGender.isChecked());
        count++;

        if(count == 5)
        {
            if(dbHandler.getProfile().getId() != 1)
            {
                dbHandler.addProfile(profile);
            }
            else
            {
                dbHandler.updateProfile(profile);
            }

            save.setVisibility(View.INVISIBLE);
            editAge.setEnabled(false);
            editName.setEnabled(false);
            editWeight.setEnabled(false);
            editHeight.setEnabled(false);
            editGender.setEnabled(false);

            calculateStats();

            textBMI.setVisibility(View.VISIBLE);
            valueBMI.setVisibility(View.VISIBLE);
            textBMR.setVisibility(View.VISIBLE);
            valueBMR.setVisibility(View.VISIBLE);

            saved.show();
        }
        else
        {
            missing_fields.show();
        }
    }

    @Override
    public void onBackPressed()
    {
        if(save.getVisibility() == View.VISIBLE )
        {
            back_toast.show();
        }
        else
        {
            super.onBackPressed();
        }
    }

    public void calculateStats()
    {
        Profile profile = dbHandler.getProfile();
        float age = profile.getAge();
        float height = profile.getHeight();
        float weight = profile.getWeight();

        valueBMI.setText(Float.toString((weight/((height/100)*(height/100)))));

        if(profile.getSex()) //female
        {
            valueBMR.setText(Double.toString((66)+(13.7*weight)+(5*height)-(6.8*age)));
        }
        else //male
        {
            valueBMR.setText(Double.toString((665)+(9.6*weight)+(1.8*height)-(4.7*age)));
        }
    }

}
