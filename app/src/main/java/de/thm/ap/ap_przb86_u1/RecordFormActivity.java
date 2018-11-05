package de.thm.ap.ap_przb86_u1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;

import de.thm.ap.ap_przb86_u1.model.Record;

public class RecordFormActivity extends AppCompatActivity {
    private EditText moduleNum;
    private EditText creditPoints;
    private EditText mark;
    private AutoCompleteTextView moduleName;
    private Spinner year;
    private CheckBox soseCheckbox;
    private CheckBox halfWeightedCheckbox;

    private ArrayList<Integer> years;
    private int positionInList;
    private boolean updateFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_records_form);

        Optional.ofNullable(getSupportActionBar()).ifPresent(actionBar -> actionBar.setDisplayHomeAsUpEnabled(true));

        moduleNum    = findViewById(R.id.module_num);
        moduleName   = findViewById(R.id.module_name);
        creditPoints = findViewById(R.id.credits_number);
        mark         = findViewById(R.id.mark_percent_number);
        year         = findViewById(R.id.semester_spinner);
        soseCheckbox = findViewById(R.id.sose_checkbox);

        halfWeightedCheckbox = findViewById(R.id.half_weighted_checkbox);

        String [] names = getResources().getStringArray(R.array.module_names);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, names);
        moduleName.setAdapter(adapter);

        ArrayAdapter<Integer> adapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, getYears());
        year.setAdapter(adapter2);

        if(getIntent().getExtras() != null && getIntent().hasExtra("position")){
            int position = getIntent().getExtras().getInt("position");
            Log.d("EDIT", "parsed position in list: " + position);
            showRecordValues(position);
        }
    }

    public void showRecordValues(int position){
        Record record = AppDatabase.getDb(this).recordDAO().findById(position);
        this.positionInList = position;
        this.updateFlag = true;

            moduleNum.setText(record.getModuleNum());
            moduleName.setText(record.getModuleName());
            mark.setText(record.getMark());
            soseCheckbox.setChecked(record.isSummerTerm());
            halfWeightedCheckbox.setChecked(record.isHalfWeighted());
            creditPoints.setText(String.valueOf(record.getCredits()));
            year.setSelection(years.indexOf(record.getYear()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSave(View view){
        Record record = new Record();

        boolean isValid = true;
        record.setModuleName(moduleName.getText().toString().trim());
        if("".equals(record.getModuleName())){
            moduleName.setError(getString(R.string.module_name_not_empty));
            isValid = false;
        }

        try{
            int credits = Integer.parseInt(creditPoints.getText().toString().trim());

            record.setCredits(Integer.parseInt(creditPoints.getText().toString().trim()));
            if(credits < 3 || credits > 15){
                creditPoints.setError(getString(R.string.illegal_cpr_number));
                isValid = false;
            }

            int note = 0;
            try {
                note = Integer.parseInt(mark.getText().toString().trim());

                if (!(note == 0 || note > 49 && note < 101)) {
                    throw new NumberFormatException();
                }
            }catch (NumberFormatException e){
                mark.setError(getString(R.string.illegal_mark));
                isValid = false;
            }

            record.setMark(note);
        }catch (NumberFormatException e){
            isValid = false;
        }

        Toast.makeText(this, year.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
        int yearValue = Integer.parseInt(String.valueOf(year.getSelectedItem().toString()));


        if(isValid){
            record.setModuleName(moduleName.getText().toString().trim());
            record.setModuleNum(moduleNum.getText().toString().trim());
            record.setCredits(Integer.parseInt(creditPoints.getText().toString().trim()));
            record.setYear(yearValue);
            record.setIsHalfWeighted(halfWeightedCheckbox.isChecked());
            record.setSummerTerm(soseCheckbox.isChecked());

            if(updateFlag && AppDatabase.getDb(this).recordDAO().findById(positionInList) != null){
                this.updateFlag = false;

                record.setId(++positionInList);
                AppDatabase.getDb(this).recordDAO().update(record);

            }else {
                AppDatabase.getDb(this).recordDAO().persist(record);
            }
            finish();
        }
    }

    private ArrayList<Integer> getYears(){
        years = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int yearsCount  = 5;

        for(int i = 0; i < yearsCount; i++){
            years.add(currentYear--);
        }
        return years;
    }
}
