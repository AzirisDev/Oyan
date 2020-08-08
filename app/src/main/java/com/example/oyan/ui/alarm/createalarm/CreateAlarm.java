package com.example.oyan.ui.alarm.createalarm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.example.oyan.R;
import com.example.oyan.ui.alarm.data.Alarm;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateAlarm extends Fragment {
    @BindView(R.id.create_alarm_timePicker)
    TimePicker timePicker;
    @BindView(R.id.create_alarm_title)
    EditText title;
    @BindView(R.id.create_alarm_addAlarm)
    Button addAlarm;
    @BindView(R.id.create_alarm_recurring)
    CheckBox recurring;
    @BindView(R.id.create_alarm_checkMon)
    CheckBox mon;
    @BindView(R.id.create_alarm_checkTue)
    CheckBox tue;
    @BindView(R.id.create_alarm_checkWed)
    CheckBox wed;
    @BindView(R.id.create_alarm_checkThu)
    CheckBox thu;
    @BindView(R.id.create_alarm_checkFri)
    CheckBox fri;
    @BindView(R.id.create_alarm_checkSat)
    CheckBox sat;
    @BindView(R.id.create_alarm_checkSun)
    CheckBox sun;
    @BindView(R.id.create_alarm_weekDaysOptions)
    LinearLayout weekDaysOptions;

    private CreateAlarmViewModel createAlarmViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createAlarmViewModel = ViewModelProviders.of(this).get(CreateAlarmViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_createalarm, container, false);

        ButterKnife.bind(this, view);

        recurring.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    weekDaysOptions.setVisibility(View.VISIBLE);
                } else {
                    weekDaysOptions.setVisibility(View.GONE);
                }
            }
        });

        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleAlarm();
                Navigation.findNavController(v).navigate(R.id.action_createAlarmFragment_to_alarmsListFragment);
            }
        });

        return view;
    }




    private void scheduleAlarm() {
        int alarmId = new Random().nextInt(Integer.MAX_VALUE);

        Alarm alarm = new Alarm(
                alarmId,
                TimePickerUtil.getTimePickerHour(timePicker),
                TimePickerUtil.getTimePickerMinute(timePicker),
                true,
                recurring.isChecked(),
                mon.isChecked(),
                tue.isChecked(),
                wed.isChecked(),
                thu.isChecked(),
                fri.isChecked(),
                sat.isChecked(),
                sun.isChecked(),
                title.getText().toString()
        );

        createAlarmViewModel.insert(alarm);

        alarm.schedule(getContext());
    }





}