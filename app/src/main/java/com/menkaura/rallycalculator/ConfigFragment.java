package com.menkaura.rallycalculator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Fragmento de configuracion de la app
 */
public class ConfigFragment extends Fragment {

    private RallyViewModel viewModel;

    private TextView correctedTime;
    private TextView about;
    private Button applyButton;
    private Handler handlerCorrectedHour = new Handler();
    private Runnable runnableCorrectedHour;

    private EditText hoursOffset;
    private EditText minutesOffset;
    private EditText secondsOffset;

    int hOffset;
    int mOffset;
    int sOffset;


    /**
     * Metodo que crea la vista del fragmento
      * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inicializa ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(RallyViewModel.class);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_config, container, false);

        hoursOffset = view.findViewById(R.id.offSetHours);
        minutesOffset = view.findViewById(R.id.offSetMinutes);
        secondsOffset = view.findViewById(R.id.offSetSeconds);


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
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hoursOffset.getText().toString().isEmpty()){
                    hOffset = 0;
                    viewModel.hOffset = 0;
                } else {
                    hOffset = Integer.parseInt(hoursOffset.getText().toString());
                    viewModel.hOffset = Integer.parseInt(hoursOffset.getText().toString());
                }
                if (minutesOffset.getText().toString().isEmpty()){
                    mOffset = 0;
                    viewModel.mOffset = 0;
                } else {
                    mOffset = Integer.parseInt(minutesOffset.getText().toString());
                    viewModel.mOffset = Integer.parseInt(minutesOffset.getText().toString());
                }
                if (secondsOffset.getText().toString().isEmpty()){
                    sOffset = 0;
                    viewModel.sOffset = 0;
                } else {
                    sOffset = Integer.parseInt(secondsOffset.getText().toString());
                    viewModel.sOffset = Integer.parseInt(secondsOffset.getText().toString());
                }
                Toast.makeText(getContext(), "Offset aplicado", Toast.LENGTH_SHORT).show();
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


    /**
     * Metodo que actualiza la hora en el TextView. Suma el offset que se introduce al pulsar el boton
     */
    private void actualizarHora() {
        // Obtener la hora actual
        Date now = new Date();
        // Aplicar el offset a la hora actual
        Date offsetHour = new Date(now.getTime() + viewModel.hOffset * 3600_000L + viewModel.mOffset * 60_000L + viewModel.sOffset * 1000L);
        // Formatear la hora
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String horaFormateada = sdf.format(offsetHour);
        // Mostrar la hora en el TextView
        correctedTime.setText(horaFormateada);
    }
}