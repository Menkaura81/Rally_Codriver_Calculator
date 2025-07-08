package com.menkaura.rallycalculator;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class CalcFragment extends Fragment {

    // Viewmodel compartidos entre fragmentos
    private RallyViewModel viewModel;


    // Variables de clase
    LocalTime tiempoSalida;
    LocalTime tiempoTransito;
    LocalTime tiempoATC;
    EditText horasSalida;
    EditText minutosSalida;
    EditText horasTransito;
    EditText minutosTransito;
    EditText horasATC;
    EditText minutosATC;



    // Mostrar la hora del rally
    private TextView rallyTime;
    private TextView cuentaAtras;
    private Button botonInicio;
    private Button botonParar;

    private Handler handlerHoraActual = new Handler();
    private Runnable runnableHoraActual;

    private Handler handlerCuentaRegresiva = new Handler();
    private Runnable runnableCuentaRegresiva;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inicializa ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(RallyViewModel.class);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calc, container, false);

        // Find view
        rallyTime = view.findViewById(R.id.rallyTime);
        horasSalida = view.findViewById(R.id.editTextHoras);
        minutosSalida = view.findViewById(R.id.editTextMinutos);
        horasTransito = view.findViewById(R.id.editTextHorasTransit);
        minutosTransito = view.findViewById(R.id.editTextMinutosTransit);
        horasATC = view.findViewById(R.id.editTextHorasATC);
        minutosATC = view.findViewById(R.id.editTextMinutosATC);
        botonInicio = view.findViewById((R.id.botonInicio));
        botonParar = view.findViewById(R.id.botonParar);
        cuentaAtras = view.findViewById(R.id.cuentaAtras);

        // Restaurar valores del ViewModel
        horasSalida.setText(viewModel.horasSalida);
        minutosSalida.setText(viewModel.minutosSalida);
        horasTransito.setText(viewModel.horasTransito);
        minutosTransito.setText(viewModel.minutosTransito);
        horasATC.setText(viewModel.horasATC);
        minutosATC.setText(viewModel.minutosATC);

        // Listener horas salida
        horasSalida.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No se necesita aquí
                horasSalida.setTextColor(getResources().getColor(R.color.black));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No se necesita aquí
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Comprobacion datos introducidos
                if (!s.toString().isEmpty()) {
                    try {
                        int valor = Integer.parseInt(s.toString());
                        if (valor > 23 || valor < 0)  {
                            horasSalida.setTextColor(getResources().getColor(R.color.red));
                            horasSalida.setSelection(horasSalida.getText().length()); // Mueve el cursor al final
                        }
                    } catch (NumberFormatException e) {
                        // En caso de error de conversión (opcional)
                        horasSalida.setText("");
                    }
                }
                // Si todos los datos estan completos realiza la suma
                if (!horasSalida.getText().toString().isEmpty() && !minutosSalida.getText().toString().isEmpty()
                        &&!horasTransito.getText().toString().isEmpty() &&!minutosTransito.getText().toString().isEmpty()){
                    int hSalida = Integer.parseInt(horasSalida.getText().toString());
                    int mSalida = Integer.parseInt(minutosSalida.getText().toString());
                    int hTransito = Integer.parseInt(horasTransito.getText().toString());
                    int mTransito = Integer.parseInt(minutosTransito.getText().toString());
                    calculateArrivalTime(hSalida, mSalida, hTransito, mTransito);
                }
            }
        });

        // Listener minutos salida
        minutosSalida.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                minutosSalida.setTextColor(getResources().getColor(R.color.black));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No se necesita aquí
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    try {
                        int valor = Integer.parseInt(s.toString());
                        if (valor > 59 || valor < 0) {
                            minutosSalida.setTextColor(getResources().getColor(R.color.red));
                            minutosSalida.setSelection(minutosSalida.getText().length()); // Mueve el cursor al final
                        }
                    } catch (NumberFormatException e) {
                        // En caso de error de conversión (opcional)
                        minutosSalida.setText("");
                    }
                }
                // Si todos los datos estan completos realiza la suma
                if (!horasSalida.getText().toString().isEmpty() && !minutosSalida.getText().toString().isEmpty()
                        &&!horasTransito.getText().toString().isEmpty() &&!minutosTransito.getText().toString().isEmpty()){
                    int hSalida = Integer.parseInt(horasSalida.getText().toString());
                    int mSalida = Integer.parseInt(minutosSalida.getText().toString());
                    int hTransito = Integer.parseInt(horasTransito.getText().toString());
                    int mTransito = Integer.parseInt(minutosTransito.getText().toString());
                    calculateArrivalTime(hSalida, mSalida, hTransito, mTransito);
                }
            }
        });

        // Listener horasTransito
        horasTransito.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No se necesita aquí
                horasTransito.setTextColor(getResources().getColor(R.color.black));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No se necesita aquí
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    try {
                        int valor = Integer.parseInt(s.toString());
                        if (valor > 23 || valor < 0)  {
                            horasTransito.setTextColor(getResources().getColor(R.color.red));
                            horasTransito.setSelection(horasTransito.getText().length()); // Mueve el cursor al final
                        }
                    } catch (NumberFormatException e) {
                        // En caso de error de conversión (opcional)
                        horasTransito.setText("");
                    }
                }
                // Si todos los datos estan completos realiza la suma
                if (!horasSalida.getText().toString().isEmpty() && !minutosSalida.getText().toString().isEmpty()
                        &&!horasTransito.getText().toString().isEmpty() &&!minutosTransito.getText().toString().isEmpty()){
                    int hSalida = Integer.parseInt(horasSalida.getText().toString());
                    int mSalida = Integer.parseInt(minutosSalida.getText().toString());
                    int hTransito = Integer.parseInt(horasTransito.getText().toString());
                    int mTransito = Integer.parseInt(minutosTransito.getText().toString());
                    calculateArrivalTime(hSalida, mSalida, hTransito, mTransito);
                }
            }
        });

        // Listener minutos transito
        minutosTransito.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                minutosTransito.setTextColor(getResources().getColor(R.color.black));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No se necesita aquí
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    try {
                        int valor = Integer.parseInt(s.toString());
                        if (valor > 59 || valor < 0) {
                            minutosTransito.setTextColor(getResources().getColor(R.color.red));
                            minutosTransito.setSelection(minutosTransito.getText().length()); // Mueve el cursor al final
                        }
                    } catch (NumberFormatException e) {
                        // En caso de error de conversión (opcional)
                        minutosTransito.setText("");
                    }
                }
                // Si todos los datos estan completos realiza la suma
                if (!horasSalida.getText().toString().isEmpty() && !minutosSalida.getText().toString().isEmpty()
                        &&!horasTransito.getText().toString().isEmpty() &&!minutosTransito.getText().toString().isEmpty()){
                    int hSalida = Integer.parseInt(horasSalida.getText().toString());
                    int mSalida = Integer.parseInt(minutosSalida.getText().toString());
                    int hTransito = Integer.parseInt(horasTransito.getText().toString());
                    int mTransito = Integer.parseInt(minutosTransito.getText().toString());
                    calculateArrivalTime(hSalida, mSalida, hTransito, mTransito);
                }
            }
        });

        // Boton
        botonInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia el conteo regresivo
                startCuentaRegresiva();
            }
        });

        // Boton
        botonParar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlerCuentaRegresiva.removeCallbacks(runnableCuentaRegresiva);
                cuentaAtras.setText("00:00:00");
            }
        });

        runnableHoraActual = new Runnable() {
            @Override
            public void run() {
                actualizarHoraActual();
                handlerHoraActual.postDelayed(this, 1000);
            }
        };
        handlerHoraActual.post(runnableHoraActual);

        if (savedInstanceState != null) {
            horasSalida.setText(savedInstanceState.getString("horasSalida", ""));
            minutosSalida.setText(savedInstanceState.getString("minutosSalida", ""));
            horasTransito.setText(savedInstanceState.getString("horasTransito", ""));
            minutosTransito.setText(savedInstanceState.getString("minutosTransito", ""));
            horasATC.setText(savedInstanceState.getString("horasATC", ""));
            minutosATC.setText(savedInstanceState.getString("minutosATC", ""));

            // Restaurar tiempoATC si existía
            if (savedInstanceState.containsKey("tiempoATC")) {
                String tiempoATCString = savedInstanceState.getString("tiempoATC");
                tiempoATC = LocalTime.parse(tiempoATCString);
            }
        }

        if (tiempoATC != null) {
            startCuentaRegresiva();
        }

        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
        // Guardar valores actuales en ViewModel antes de salir
        viewModel.horasSalida = horasSalida.getText().toString();
        viewModel.minutosSalida = minutosSalida.getText().toString();
        viewModel.horasTransito = horasTransito.getText().toString();
        viewModel.minutosTransito = minutosTransito.getText().toString();
        viewModel.horasATC = horasATC.getText().toString();
        viewModel.minutosATC = minutosATC.getText().toString();
    }


    private void actualizarHoraActual() {
        // Mostrar la advertencia si el offset es distinto de 0
        if (viewModel.hOffset != 0 || viewModel.mOffset != 0 || viewModel.sOffset != 0){
            TextView offsetApplied = null;
            if (getView() != null) {
                offsetApplied = getView().findViewById(R.id.offset_applied);
                offsetApplied.setVisibility(View.VISIBLE);
            }
        } else {
            TextView offsetApplied = null;
            if (getView() != null) {
                offsetApplied = getView().findViewById(R.id.offset_applied);
                offsetApplied.setVisibility(View.INVISIBLE);
            }

        }
        // Obtener la hora actual
        Date now = new Date();
        Date offsetHour;
        if (viewModel.offsetSign.equals("+")){
            offsetHour = new Date(now.getTime() + viewModel.hOffset * 3600_000L + viewModel.mOffset * 60_000L + viewModel.sOffset * 1000L);
        } else {
            offsetHour = new Date(now.getTime() - viewModel.hOffset * 3600_000L - viewModel.mOffset * 60_000L - viewModel.sOffset * 1000L);
        }
        // Formatear la hora
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String horaFormateada = sdf.format(offsetHour);
        // Mostrar la hora en el TextView
        rallyTime.setText(horaFormateada);
    }


    private void startCuentaRegresiva() {
        runnableCuentaRegresiva = new Runnable() {
            @Override
            public void run() {
                try {
                    Date ahora = new Date();
                    // Obtener el tiempo actual en milisegundos desde medianoche
                    long ahoraMs = ahora.getHours() * 3600_000L + ahora.getMinutes() * 60_000L + ahora.getSeconds() * 1000L;

                    if (tiempoATC == null) {
                        cuentaAtras.setText(R.string.atc_not_defined);
                        return;
                    }

                    // Tiempo objetivo en milisegundos desde medianoche
                    long rallyMs = tiempoATC.getHour() * 3600_000L + tiempoATC.getMinute() * 60_000L + 0;

                    long diffMs = rallyMs - ahoraMs;

                    // Si la diferencia es negativa, significa que es para el día siguiente
                    if (diffMs < 0) {
                        diffMs += 24 * 3600_000L;
                    }

                    // Calcular horas, minutos y segundos restantes
                    int horas = (int) (diffMs / 3600_000L);
                    int minutos = (int) ((diffMs % 3600_000L) / 60_000L);
                    int segundos = (int) ((diffMs % 60_000L) / 1000L);

                    String tiempoRestante = String.format(Locale.getDefault(), "%02d:%02d:%02d", horas, minutos, segundos);

                    cuentaAtras.setText(tiempoRestante);

                    handlerCuentaRegresiva.postDelayed(this, 1000);

                } catch (Exception e) {
                    e.printStackTrace();
                    cuentaAtras.setText("Error");
                }
            }
        };

        handlerCuentaRegresiva.post(runnableCuentaRegresiva);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        handlerHoraActual.removeCallbacks(runnableHoraActual);
        handlerCuentaRegresiva.removeCallbacks(runnableCuentaRegresiva);
    }

    private void calculateArrivalTime(int hSalida, int mSalida, int hTransito, int mTransito){
        // Crear las horas de salida y de transito
        tiempoSalida = LocalTime.of(hSalida, mSalida);
        tiempoTransito = LocalTime.of(hTransito, mTransito);

        // Convertir ambos a minutos totales
        int totalMinutes = tiempoSalida.getHour() * 60 + tiempoSalida.getMinute()
                + tiempoTransito.getHour() * 60 + tiempoTransito.getMinute();

        // Calcular horas y minutos resultantes
        int hours = (totalMinutes / 60) % 24; // %24 para que no pase de un día
        int minutes = totalMinutes % 60;

        // Realizar la suma
        tiempoATC = LocalTime.of(hours, minutes);

        // Mostrar el resultado
        if (tiempoATC.getHour() < 10){
            String hATC = ("0" + tiempoATC.getHour());
            horasATC.setText(hATC);
        } else {
            horasATC.setText(String.valueOf(tiempoATC.getHour()));
        }
        if (tiempoATC.getMinute() < 10){
            String mATC = ("0" + tiempoATC.getMinute());
            minutosATC.setText(mATC);
        } else {
            minutosATC.setText(String.valueOf(tiempoATC.getMinute()));
        }
    }
}