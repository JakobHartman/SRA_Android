package com.special.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import org.rbdc.sra.R;
import org.rbdc.sra.helperClasses.CRUDFlinger;
import org.rbdc.sra.objects.Datapoint;
import org.rbdc.sra.objects.DatapointTypes;
import org.rbdc.sra.objects.Question;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class is used when editing a single question from a question set
 */

public class EditQuestionFrag extends android.support.v4.app.Fragment {

    private View parentView;
    private LayoutInflater inflater;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.parentView = inflater.inflate(R.layout.edit_question_dialog, container, false);
        this.inflater = inflater;
        this.context = container.getContext();
        Bundle args = getArguments();

        final Question q = CRUDFlinger.getQuestionSet(args.getInt("question set")).getQuestions().get(args.getInt("question"));


        // Name of the question
            final EditText nameField = (EditText) parentView.findViewById(R.id.name_field);
            if (!q.getName().equals("")) {
                nameField.setText(q.getName());
            }
            nameField.setSelection(nameField.length());
            nameField.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) { }
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    q.setName(nameField.getText().toString());
                }
            });


            // A checkbox for whether or not the question can be used multiple times
            final CheckBox multiUseBox = (CheckBox) parentView.findViewById(R.id.multi_use_checkbox);
            multiUseBox.setChecked(q.getMultiUse());
            multiUseBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    q.setMultiUse(multiUseBox.isChecked());
                }
            });

            // List of data points
            final LinearLayout dataPointList = (LinearLayout) parentView.findViewById(R.id.list_view);
            final ArrayList<Datapoint> datapoints = q.getDataPoints();
            for (Datapoint dp : datapoints) {
                addDataPoint(dataPointList, dp, q);
            }

            Button finishButton = (Button) parentView.findViewById(R.id.finish_button);
            finishButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean needsLabel = false;
                    for (Datapoint dp : datapoints) {
                        if (dp.getLabel().toString().equals("")) {
                            needsLabel = true;
                            break;
                        }
                    }
                    if (nameField.getText().toString().equals("")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Translucent);
                        builder.setTitle("Error: Empty title");
                        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else if (datapoints.isEmpty()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Translucent);
                        builder.setTitle("Please add a data point");
                        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else if (needsLabel) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Translucent);
                        builder.setTitle("One or more data points need a label");
                        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {
                        CRUDFlinger.saveQuestionSets();
                        getFragmentManager().popBackStackImmediate();
                    }

                }
            });

            // Add a new data point to the question
            Button addButton = (Button) parentView.findViewById(R.id.add_button);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Datapoint dp = new Datapoint();
                    q.addDataPoint(dp);
                    addDataPoint(dataPointList, dp, q);
                }
            });

            return parentView;
    }

    //Populate the list of data points
    private void addDataPoint(final LinearLayout dpList, final Datapoint dp, final Question q) {
        final LinearLayout dpItemView = (LinearLayout) inflater.inflate(R.layout.data_point_list_item, null);
        dpList.addView(dpItemView);

        // Data point Label
        final EditText label = (EditText) dpItemView.findViewById(R.id.label_field);
        label.setText(dp.getLabel());
        label.setSelection(label.length());
        label.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) { }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dp.setLabel(label.getText().toString());
            }
        });

        final LinearLayout optionsContainer = (LinearLayout) dpItemView.findViewById(R.id.options_container);
        final LinearLayout optionsList = (LinearLayout) dpItemView.findViewById(R.id.options_list_view);

        //Get the options for the data point
        final ArrayList<String> options = dp.getOptions();
        for (int i = 0; i < options.size(); i++) {
            addOption(options.get(i), optionsList, dp);
        }

        Button addOption = (Button) dpItemView.findViewById(R.id.add_option_button);
        addOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String option = "";
                options.add(option);
                addOption(option, optionsList, dp);
            }
        });

        // The spinner that describes the type of data point
        Spinner dataTypeSpinner = (Spinner) dpItemView.findViewById(R.id.data_type_spinner);
        String[] types = context.getResources().getStringArray(R.array.data_point_types_array);
        ArrayList<String> typesList = new ArrayList<>(Arrays.asList(types));
        ArrayAdapter<String> typesAdapter = new ArrayAdapter<String>(context,
                R.layout.spinner_dropdown_item, typesList);
        dataTypeSpinner.setAdapter(typesAdapter);
        dataTypeSpinner.setSelection(DatapointTypes.getTypeIndex(dp.getType()));

        // If it is a list show the options view
        if (dp.dataTypeIsAList()) {
            optionsContainer.setVisibility(View.VISIBLE);
        }

        dataTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                dp.setType(DatapointTypes.getTypeFromIndex(position));
                System.out.println("data type is " + DatapointTypes.getTypeFromIndex(position));

                if (dp.dataTypeIsAList()) optionsContainer.setVisibility(View.VISIBLE);

                //if it is not a list
                else {
                    optionsContainer.setVisibility(View.GONE);
                    dp.getOptions().clear();
                    optionsList.removeAllViews();
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parentView) {
                optionsContainer.setVisibility(View.GONE);
            }
        });

        Button delete = (Button) dpItemView.findViewById(R.id.delete_button);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q.deleteDataPoint(dp);
                dpList.removeView(dpItemView);
                CRUDFlinger.saveQuestionSets();
            }
        });
    }

    private void addOption(String option, final LinearLayout optionsList, final Datapoint dp) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final LinearLayout optionListItem = (LinearLayout) inflater.inflate(R.layout.option_list_item, null);
        final EditText optionView = (EditText) optionListItem.findViewById(R.id.option_field);
        optionView.setText(option);
        optionView.setSelection(optionView.length());
        optionView.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) { }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int index = optionsList.indexOfChild(optionListItem);
                dp.getOptions().set(index, optionView.getText().toString());
            }
        });

        Button deleteButton = (Button) optionListItem.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = optionsList.indexOfChild(optionListItem);
                dp.getOptions().remove(index);
                optionsList.removeView(optionListItem);
            }
        });
        optionsList.addView(optionListItem);
    }

}
