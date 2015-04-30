
package com.special;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.rbdc.sra.R;
import org.rbdc.sra.helperClasses.CRUDFlinger;
import org.rbdc.sra.objects.Datapoint;
import org.rbdc.sra.objects.DatapointTypes;
import org.rbdc.sra.objects.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/*
    This class is called when someone selects a question and
    is what is displayed inside of the ViewPager inside of the
    DataCollect Activity
 */
public class DataCollectQuestionFragment extends Fragment {

    private int questionIndex;
    private Question question;
    private LinearLayout table; //Root element

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_data_collect_question, container, false);
        CRUDFlinger.setApplication(getActivity().getApplication());

        final DataCollect activity = (DataCollect) getActivity();
        Bundle args = getArguments();
        questionIndex = args.getInt("questionIndex");
        question = activity.getQuestion(questionIndex);

        Button addButton = (Button) rootView.findViewById(R.id.add_button);
        if (question.getMultiUse()) {
            addButton.setVisibility(View.VISIBLE);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // For the question, get all of the data points
                    ArrayList<Datapoint> dps = question.getDataPoints();
                    // When you add the data point, have it set to null by default
                    for (Datapoint dp : dps) {
                        dp.addAnswer("");
                    }

                    // Redraw the table
                    populateTable();
                }
            });
        }

        // List of data points
        // table is the root linear layout
        table = (LinearLayout) rootView.findViewById(R.id.layout);
        populateTable();

        return rootView;
    }

    private void removeAnswers(int index) {
        ArrayList<Datapoint> points = question.getDataPoints();
        for (Datapoint dp : points) {
            dp.getAnswers().remove(index);
        }
    }

    // Populates the question views
    private void populateTable() {
        table.removeAllViews();
        ArrayList<Datapoint> points = question.getDataPoints();
        int numAnswers = 1;
        if (question.getMultiUse() && !points.isEmpty()) {
            numAnswers = points.get(0).getAnswers().size();
            Log.i("Multiuse = ", "TRUE");
        }

        // Create a Linear Layout container for question
        for (int i = 0; i < numAnswers; i++) {
            final int answerPosition = i;
            final LinearLayout questionContainer = new LinearLayout(getActivity());
            questionContainer.setOrientation(LinearLayout.VERTICAL);
            table.addView(questionContainer);

            // Create a Linear Layout container for Question
            for (final Datapoint dp : points) {
                final ArrayList<String> answers = dp.getAnswers();
                final LinearLayout row = new LinearLayout(getActivity());
                questionContainer.addView(row);
                row.setOrientation(LinearLayout.HORIZONTAL);
                String answer = "";
                if (answerPosition < answers.size()) {
                    answer = answers.get(i);
                    //Testing
                    Log.i("answers size = ", answers.size() +" ");
                    Log.i("answer at position " + i + " = ", answers.get(i));
                }

                TextView label = new TextView(getActivity());
                label.setText(dp.getLabel());
                label.setTextSize(22);
                label.setTypeface(null,Typeface.BOLD);
                questionContainer.addView(label);

                String dataType = dp.getType();
                if (dataType.equals(DatapointTypes.TEXT)) {
                    final EditText input = new EditText(getActivity());
                    Log.i("Data Type = ", "Text");
                    Log.i("Text answer = ", answer);

                    // What is this for??
                    //ViewGroup.LayoutParams params = input.getLayoutParams();

                    input.setText(answer);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);

                    input.addTextChangedListener(new TextWatcher() {
                        public void afterTextChanged(Editable s) {}
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (answers.isEmpty())
                                answers.add(answerPosition, input.getText().toString());
                            else
                                answers.set(answerPosition, input.getText().toString());
                        }
                    });
                    // *******************************



                    // Add question to Question Container
                    questionContainer.addView(input);
                }
                else if (dataType.equals(DatapointTypes.NUMBER)) {
                    final EditText input = new EditText(getActivity());
                    input.setText(answer);
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    input.setHint("Type here");
                    input.addTextChangedListener(new TextWatcher() {
                        public void afterTextChanged(Editable s) {}
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (answers.isEmpty())
                                answers.add(answerPosition, input.getText().toString());
                            else
                                answers.set(answerPosition, input.getText().toString());
                        }
                    });

                    // Add response to Question Container
                    questionContainer.addView(input);
                }
                else if (dataType.equals(DatapointTypes.DATE)) {
                    Calendar calendar = Calendar.getInstance();
                    int month = calendar.get(Calendar.MONTH);
                    int year = calendar.get(Calendar.YEAR);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    DatePicker datePicker = new DatePicker(getActivity());
                    DatePicker.OnDateChangedListener listener = new DatePicker.OnDateChangedListener() {
                        @Override
                        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Calendar c = Calendar.getInstance();
                            c.set(year, monthOfYear, dayOfMonth);
                            if (answers.isEmpty())
                                answers.add(answerPosition, "" + c.getTimeInMillis());
                            else
                                answers.set(answerPosition, "" + c.getTimeInMillis());
                        }
                    };
                    if (!dp.getSingleAnswer().equals("")) {
                        long ms = Long.parseLong(answer);
                        calendar.setTimeInMillis(ms);
                        month = calendar.get(Calendar.MONTH);
                        year = calendar.get(Calendar.YEAR);
                        day = calendar.get(Calendar.DAY_OF_MONTH);
                    }
                    datePicker.init(year, month, day, listener);
                    datePicker.setCalendarViewShown(false);

                    // add response to question container
                    questionContainer.addView(datePicker);
                }
                else if (dataType.equals(DatapointTypes.LIST_SINGLE_ANSWER)) {
                    //final Spinner options = new Spinner(getActivity());
                    final Spinner options = new Spinner(getActivity(), Spinner.MODE_DROPDOWN);

                    ArrayList<String> list = new ArrayList<>();
                    list.add("");
                    for(String option : dp.getOptions()) {
                        list.add(option);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                            R.layout.spinner_dropdown_item, list);
                    options.setAdapter(adapter);
                    if (answer == null) {
                        answer = "";
                    }
                    options.setSelection(list.indexOf(answer));
                    options.setOnItemSelectedListener(new OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            if (answers.isEmpty())
                                answers.add(answerPosition, (String) options.getSelectedItem());
                            else
                                answers.set(answerPosition, (String) options.getSelectedItem());
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {}
                    });

                    // add response to question container
                    questionContainer.addView(options);
                }
                //********** MULTI ANSWER LIST ********
                else if (dataType.equals(DatapointTypes.LIST_MULTI_ANSWER)) {
                    final MultiSelectionSpinner options = new MultiSelectionSpinner(getActivity(),answerPosition, answers);
                    Button saveButton = new Button(getActivity());
                    saveButton.setText("Save Checked Answers");

                    if (!dp.getOptions().isEmpty())
                        options.setItems(dp.getOptions());
                    String json = answer;
                    Gson gson = new GsonBuilder().create();
                    List<String> selected = (List<String>) gson.fromJson(json, new TypeToken<List<String>>() {}.getType());
                    if (answers != null) options.setSelection(selected);
//                    saveButton.setOnTouchListener(new View.OnTouchListener() {
//                        @Override
//                        public boolean onTouch(View v, MotionEvent event) {
//                            List<String> items = options.getSelectedStrings();
//                            Gson gson = new GsonBuilder().create();
//                            String json = gson.toJson(items);
//                            System.out.println("You touched the mulit answer" + json);
//                            if (answers.isEmpty())
//                                answers.add(answerPosition, json);
//                            else
//                                answers.set(answerPosition, json);
//
//                            return false;
//                        }
//                    });

                    //add question to question container
                    questionContainer.addView(options);
                    //questionContainer.addView(saveButton);
                }
            }

            if (question.getMultiUse()) {
                Button deleteDataPoint = new Button(getActivity());
                deleteDataPoint.setText("Delete");
                deleteDataPoint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        table.removeView(questionContainer);
                        removeAnswers(answerPosition);
                        populateTable();
                    }
                });

                // if it is a multi-use response add delete option
                questionContainer.addView(deleteDataPoint);
            }

            TextView spacer = new TextView(getActivity());
            spacer.setTextSize(24);
            spacer.setText("\n");
            questionContainer.addView(spacer);
        }
    }




    public class MultiSelectionSpinner extends Spinner implements
            OnMultiChoiceClickListener {
        String[] _items = null;
        boolean[] mSelection = null;
        int position;
        List<String> answers;

        ArrayAdapter<String> simple_adapter;

        public MultiSelectionSpinner(Context context, int answerPosition, List<String> answers) {
            super(context);
            position = answerPosition;
            this.answers = answers;

            simple_adapter = new ArrayAdapter<String>(context,
                    android.R.layout.simple_spinner_item);
            super.setAdapter(simple_adapter);
        }

        public MultiSelectionSpinner(Context context, AttributeSet attrs) {
            super(context, attrs);

            simple_adapter = new ArrayAdapter<String>(context,
                    android.R.layout.simple_spinner_item);
            super.setAdapter(simple_adapter);
        }

        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            if (mSelection != null && which < mSelection.length) {
                mSelection[which] = isChecked;

                simple_adapter.clear();
                simple_adapter.add(buildSelectedItemString());

                //Save selected
                List<String> items = getSelectedStrings();
                Gson gson = new GsonBuilder().create();
                String json = gson.toJson(items);
                System.out.println("You touched the mulit answer" + json);
                if (answers.isEmpty())
                    answers.add(position, json);
                else
                    answers.set(position, json);

            } else {
                throw new IllegalArgumentException(
                        "Argument 'which' is out of bounds.");
            }
        }

        @Override
        public boolean performClick() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMultiChoiceItems(_items, mSelection, this);
            builder.show();
            return true;
        }

        @Override
        public void setAdapter(SpinnerAdapter adapter) {
            throw new RuntimeException(
                    "setAdapter is not supported by MultiSelectSpinner.");
        }

        public void setItems(String[] items) {
            _items = items;
            mSelection = new boolean[_items.length];
            simple_adapter.clear();
            simple_adapter.add(_items[0]);
            Arrays.fill(mSelection, false);
        }

        public void setItems(List<String> items) {
            _items = items.toArray(new String[items.size()]);
            mSelection = new boolean[_items.length];
            simple_adapter.clear();
            simple_adapter.add(_items[0]);
            Arrays.fill(mSelection, false);
        }

        public void setSelection(String[] selection) {
            for (String cell : selection) {
                for (int j = 0; j < _items.length; ++j) {
                    if (_items[j].equals(cell)) {
                        mSelection[j] = true;
                    }
                }
            }
        }

        public void setSelection(List<String> selection) {
            if (selection == null) return;
            for (int i = 0; i < mSelection.length; i++) {
                mSelection[i] = false;
            }
            for (String sel : selection) {
                for (int j = 0; j < _items.length; ++j) {
                    if (_items[j].equals(sel)) {
                        mSelection[j] = true;
                    }
                }
            }
            simple_adapter.clear();
            simple_adapter.add(buildSelectedItemString());
        }

        public void setSelection(int index) {
            for (int i = 0; i < mSelection.length; i++) {
                mSelection[i] = false;
            }
            if (index >= 0 && index < mSelection.length) {
                mSelection[index] = true;
            } else {
                throw new IllegalArgumentException("Index " + index
                        + " is out of bounds.");
            }
            simple_adapter.clear();
            simple_adapter.add(buildSelectedItemString());
        }

        public void setSelection(int[] selectedIndicies) {
            for (int i = 0; i < mSelection.length; i++) {
                mSelection[i] = false;
            }
            for (int index : selectedIndicies) {
                if (index >= 0 && index < mSelection.length) {
                    mSelection[index] = true;
                } else {
                    throw new IllegalArgumentException("Index " + index
                            + " is out of bounds.");
                }
            }
            simple_adapter.clear();
            simple_adapter.add(buildSelectedItemString());
        }

        public List<String> getSelectedStrings() {
            List<String> selection = new LinkedList<String>();
            for (int i = 0; i < _items.length; ++i) {
                if (mSelection[i]) {
                    selection.add(_items[i]);
                }
            }
            return selection;
        }

        public List<Integer> getSelectedIndicies() {
            List<Integer> selection = new LinkedList<Integer>();
            for (int i = 0; i < _items.length; ++i) {
                if (mSelection[i]) {
                    selection.add(i);
                }
            }
            return selection;
        }

        private String buildSelectedItemString() {
            StringBuilder sb = new StringBuilder();
            boolean foundOne = false;

            for (int i = 0; i < _items.length; ++i) {
                if (mSelection[i]) {
                    if (foundOne) {
                        sb.append(", ");
                    }
                    foundOne = true;

                    sb.append(_items[i]);
                }
            }
            return sb.toString();
        }

        public String getSelectedItemsAsString() {
            StringBuilder sb = new StringBuilder();
            boolean foundOne = false;

            for (int i = 0; i < _items.length; ++i) {
                if (mSelection[i]) {
                    if (foundOne) {
                        sb.append(", ");
                    }
                    foundOne = true;
                    sb.append(_items[i]);
                }
            }
            return sb.toString();
        }
    }
}
