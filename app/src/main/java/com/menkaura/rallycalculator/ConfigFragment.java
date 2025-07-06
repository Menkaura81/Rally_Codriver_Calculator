package com.menkaura.rallycalculator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class ConfigFragment extends Fragment {

    private TextView correctedTime;
    private TextView about;
    private Button applyButton;
    private Handler handlerCorrectedHour = new Handler();
    private Runnable runnableCorrectedHour;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_config, container, false);

        // Configurar el spinner de idiomas
        Spinner spinnerLanguages=view.findViewById(R.id.spinner_languages);
        ArrayAdapter<CharSequence>adapter= ArrayAdapter.createFromResource(getContext(), R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerLanguages.setAdapter(adapter);

        // Configurar los elementos de la vista
        correctedTime = view.findViewById(R.id.configRallyTime);
        applyButton = view.findViewById(R.id.apply_button);
        about = view.findViewById(R.id.about);

        // Button Listener
        // Boton
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date offset = new Date();
                // TODO logica del offset
            }
        });

        // About Listener
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.rally_codriver_calculator);
                builder.setMessage(R.string.menkaurasoft);
                builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Acción al hacer clic en el botón de aceptar
                    }
                });
                builder.show();
            }
        });

        // Handler para mostrar la hora
        runnableCorrectedHour = new Runnable() {
            @Override
            public void run() {
                actualizarHora();
                handlerCorrectedHour.postDelayed(this, 1000);
            }
        };
        handlerCorrectedHour.post(runnableCorrectedHour);

        // Inflate the layout for this fragment
        return view;
    }


    private void actualizarHora() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String horaFormateada = sdf.format(now);
        correctedTime.setText(horaFormateada);
    }
}