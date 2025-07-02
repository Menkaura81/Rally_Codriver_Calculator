package com.menkaura.rallycalculator;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

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
    private  Button botonInicio;

    private Handler handlerHoraActual = new Handler();
    private Runnable runnableHoraActual;

    private Handler handlerCuentaRegresiva = new Handler();
    private Runnable runnableCuentaRegresiva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find view
        rallyTime = findViewById(R.id.rallyTime);
        horasSalida = findViewById(R.id.editTextHoras);
        minutosSalida = findViewById(R.id.editTextMinutos);
        horasTransito = findViewById(R.id.editTextHorasTransit);
        minutosTransito = findViewById(R.id.editTextMinutosTransit);
        horasATC = findViewById(R.id.editTextHorasATC);
        minutosATC = findViewById(R.id.editTextMinutosATC);
        botonInicio = findViewById((R.id.botonInicio));
        cuentaAtras = findViewById(R.id.cuentaAtras);




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

        runnableHoraActual = new Runnable() {
            @Override
            public void run() {
                actualizarHoraActual();
                handlerHoraActual.postDelayed(this, 1000);
            }
        };
        handlerHoraActual.post(runnableHoraActual);
    }

    private void actualizarHoraActual() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String horaFormateada = sdf.format(now);
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
                        cuentaAtras.setText("Hora no definida");
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
    protected void onDestroy() {
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
        if (tiempoATC.getHour() == 0){
            horasATC.setText("00");
        } else {
            horasATC.setText(String.valueOf(tiempoATC.getHour()));
        }
        minutosATC.setText(String.valueOf(tiempoATC.getMinute()));
    }
}