package com.cesar.alseademo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.cesar.alseademo.model.DatosSismo;
import com.cesar.alseademo.utils.ConnectivityNetWorkUtils;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private ConnectivityNetWorkUtils connectivityNetWorkUtils;

    private Button nuevaConsultaBtn;
    private Button resultadosAnterioresBtn;
    private ImageButton fechaInicio;
    private ImageButton fechaFinal;
    private EditText magnitud;
    private String fechaInicioStr = "";
    private String fechaFinalStr = "";
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle(getString(R.string.formulario_sismos));

        connectivityNetWorkUtils = new ConnectivityNetWorkUtils(this);


        initElements();
    }

    private void initElements() {
        nuevaConsultaBtn = findViewById(R.id.nuevaConsultaBtnId);
        nuevaConsultaBtn.setOnClickListener(this);
        resultadosAnterioresBtn = findViewById(R.id.resultadosAnterioresBtnId);
        resultadosAnterioresBtn.setOnClickListener(this);
        fechaInicio = findViewById(R.id.fechaInicioBtnId);
        fechaInicio.setOnClickListener(this);
        fechaFinal = findViewById(R.id.fechaFinalBtnId);
        fechaFinal.setOnClickListener(this);
        magnitud = findViewById(R.id.magnitudId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nuevaConsultaBtnId:
                if (!fechaInicioStr.isEmpty() && !fechaFinalStr.isEmpty() && !magnitud.getText().toString().isEmpty()) {
                    ejecutarNuevaConsulta(fechaInicioStr, fechaFinalStr, magnitud.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Debe ingresar una magnitud y seleccionar fecha de inicio y fin para la consulta", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.resultadosAnterioresBtnId:
                ejercutarConsultaAnterior();
                break;
            case R.id.fechaInicioBtnId:
                callDatePicker(true);
                break;
            case R.id.fechaFinalBtnId:
                callDatePicker(false);
                break;
        }
    }

    private void callDatePicker(final boolean isFechaInicio) {
        final String CERO = "0";
        final String BARRA = "-";
        Calendar c = Calendar.getInstance();
        int mes = c.get(Calendar.MONTH);
        final int dia = c.get(Calendar.DAY_OF_MONTH);
        int anio = c.get(Calendar.YEAR);

        DatePickerDialog fechaPicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10) ? CERO + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10) ? CERO + String.valueOf(mesActual) : String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                Toast.makeText(getApplicationContext(), diaFormateado + BARRA + mesFormateado + BARRA + year, Toast.LENGTH_SHORT).show();
                if (isFechaInicio) {
                    fechaInicioStr = year + BARRA + mesFormateado + BARRA + diaFormateado;
                } else {
                    fechaFinalStr = year + BARRA + mesFormateado + BARRA + diaFormateado;
                }

            }
        }, anio, mes, dia);
        fechaPicker.show();
    }

    private void ejecutarNuevaConsulta(String fechaInicio, String fechaFinal, String magnitud) {
        if (connectivityNetWorkUtils.isConnected()) {
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=" + fechaInicio + "&endtime=" + fechaFinal + "&minmagnitude=" + magnitud;

            JsonRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    parseJson(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            queue.add(jsonRequest);
        } else {
            Toast.makeText(getApplicationContext(), "Necesita conexión a internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void parseJson(JSONObject json) {
        try {
            ArrayList<DatosSismo> datosSismos = new ArrayList<>();
            JSONArray jsonArray = json.getJSONArray("features");
            for (int i = 0; i < jsonArray.length(); i++) {
                DatosSismo datosSismo;
                datosSismo = new DatosSismo();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getJSONObject("properties").getString("title");
                String place = jsonObject.getJSONObject("properties").getString("place");
                String colorAlerta = jsonObject.getJSONObject("properties").getString("alert");
                String fecha = jsonObject.getJSONObject("properties").getString("time");
                String fechaActualizacion = jsonObject.getJSONObject("properties").getString("updated");
                datosSismo.setTitulo(title);
                datosSismo.setNomreLugar(place);
                datosSismo.setColorAlerta(colorAlerta);
                datosSismo.setFecha(fecha);
                datosSismo.setFechaActualización(fechaActualizacion);
                double lat = jsonObject.getJSONObject("geometry").getJSONArray("coordinates").getDouble(0);
                double lon = jsonObject.getJSONObject("geometry").getJSONArray("coordinates").getDouble(1);
                datosSismo.setLat(lat);
                datosSismo.setLon(lon);
                datosSismos.add(datosSismo);
            }
            saveConsulta(datosSismos);
            callResults(datosSismos);

        } catch (Exception e) {
            Log.e("Error parseo json", e.toString());
        }
    }

    private void ejercutarConsultaAnterior() {
        ArrayList<DatosSismo> datosSismoArrayList = new ArrayList<>();
        SharedPreferences sharedPref = getSharedPreferences("DatosSismos", Context.MODE_PRIVATE);
        int elementos = sharedPref.getInt("elementos", 0);
        for (int i = 0; i < elementos; i++) {
            DatosSismo datosSismo = new DatosSismo();
            datosSismo.setTitulo(sharedPref.getString("titulo"+ i, null));
            datosSismo.setNomreLugar(sharedPref.getString("lugar"+ i, null));
            datosSismo.setColorAlerta(sharedPref.getString("alerta"+ i, null));
            datosSismo.setLat(Double.parseDouble(sharedPref.getString("lat"+ i, null)));
            datosSismo.setLon(Double.parseDouble(sharedPref.getString("lon"+ i, null)));
            datosSismo.setFecha(sharedPref.getString("fecha"+ i, null));
            datosSismo.setFechaActualización(sharedPref.getString("fechaAct"+ i, null));
            datosSismoArrayList.add(datosSismo);
        }

        if(datosSismoArrayList.size()>0) {
            callResults(datosSismoArrayList);
        }
    }

    private void saveConsulta(ArrayList<DatosSismo> datosSismoArrayList) {
        SharedPreferences sharedPref = getSharedPreferences("DatosSismos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        for (int i = 0; i < datosSismoArrayList.size(); i++) {
            editor.putString("titulo" + i, datosSismoArrayList.get(i).getTitulo());
            editor.putString("lugar" + i, datosSismoArrayList.get(i).getNomreLugar());
            editor.putString("alerta" + i, datosSismoArrayList.get(i).getColorAlerta());
            editor.putString("lat" + i, String.valueOf(datosSismoArrayList.get(i).getLat()));
            editor.putString("lon" + i, String.valueOf(datosSismoArrayList.get(i).getLon()));
            editor.putString("color" + i, datosSismoArrayList.get(i).getColorAlerta());
            editor.putString("fecha" + i, datosSismoArrayList.get(i).getFecha());
            editor.putString("fechaAct" + i, datosSismoArrayList.get(i).getFechaActualización());
            editor.commit();
        }
        editor.putInt("elementos", datosSismoArrayList.size());
        editor.commit();

        Log.d("Shared", "datos guardados");
    }

    private void callResults(ArrayList<DatosSismo> datosSismoArrayList) {
        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
        intent.putExtra("datosSismos", datosSismoArrayList);
        startActivity(intent);
    }
}