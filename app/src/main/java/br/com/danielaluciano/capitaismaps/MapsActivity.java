package br.com.danielaluciano.capitaismaps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap; //Para o mapa
    private LatLng loc = null; //tradução da localização atual para colocar no mapa (um location deve ser traduzido para um LatLng)
    private LocationManager locationManager; //permite acesso ao GPS
    private static final int REQUEST_GPS = 1; //notificado quando eventos de GPS acontecem
    private TextInputLayout capitalTextInputLayout;
    private TextInputEditText capitalTextInputEditText;
    Localizacoes[] localizacoes = new Localizacoes[10]; //Declaração do array de Localizações
    class Localizacoes {
        private String capitais;
        private String paises;
        private Double lat;
        private Double log;
        private Bitmap imageBitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);  // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment.getMapAsync(this);  //Chama a função para requisitar o mapa
        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        capitalTextInputLayout = (TextInputLayout)
                findViewById(R.id.capitalTextInputLayout);
        capitalTextInputEditText = (TextInputEditText)
                findViewById(R.id.capitalTextInputEditText);
        assignLocations(); //Função para definir as localizações

    }

    public void assignLocations () {

        for (int i=0;i<localizacoes.length;i++) { //Cria o vetor de localizações
            localizacoes[i] = new Localizacoes();
        }

        //Popular Capitais
        localizacoes[0].capitais = getString(R.string.brasilia) ;
        localizacoes[1].capitais = getString(R.string.washington);
        localizacoes[2].capitais = getString(R.string.ottawa);
        localizacoes[3].capitais = getString(R.string.madrid);
        localizacoes[4].capitais = getString(R.string.pretoria);
        localizacoes[5].capitais = getString(R.string.tokyo);
        localizacoes[6].capitais = getString(R.string.seul);
        localizacoes[7].capitais = getString(R.string.berlin);
        localizacoes[8].capitais = getString(R.string.buenos_aires);
        localizacoes[9].capitais = getString(R.string.mexico_city);

        //Popular países
        localizacoes[0].paises = getString(R.string.brazil);
        localizacoes[1].paises = getString(R.string.united_states);
        localizacoes[2].paises = getString(R.string.canada);
        localizacoes[3].paises = getString(R.string.spain);
        localizacoes[4].paises = getString(R.string.south_africa);
        localizacoes[5].paises = getString(R.string.japan);
        localizacoes[6].paises = getString(R.string.south_korea);
        localizacoes[7].paises = getString(R.string.germany);
        localizacoes[8].paises = getString(R.string.argentina);
        localizacoes[9].paises = getString(R.string.mexico);

        //Popular latitudes
        localizacoes[0].lat = -15.794229;
        localizacoes[1].lat = 45.9760287;
        localizacoes[2].lat = 45.42153;
        localizacoes[3].lat = 40.416775;
        localizacoes[4].lat = -25.747868;
        localizacoes[5].lat = 35.689488;
        localizacoes[6].lat = 37.566535;
        localizacoes[7].lat = 52.520007;
        localizacoes[8].lat = -34.603684;
        localizacoes[9].lat = 19.432608;

        //Popuplar Longitude
        localizacoes[0].log = -47.882166;
        localizacoes[1].log = -120.484462;
        localizacoes[2].log = -75.697193;
        localizacoes[3].log = -3.70379;
        localizacoes[4].log = 28.229271;
        localizacoes[5].log = 139.691706;
        localizacoes[6].log = 126.977969;
        localizacoes[7].log = -13.404954;
        localizacoes[8].log = -58.381559;
        localizacoes[9].log = -99.133208;

        //Popular Bitmaps
        localizacoes[0].imageBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.brasil);
        localizacoes[1].imageBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.estados_unidos);
        localizacoes[2].imageBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.canada);
        localizacoes[3].imageBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.espanha);
        localizacoes[4].imageBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.africa_do_sul);
        localizacoes[5].imageBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.japao);
        localizacoes[6].imageBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.coreia_do_sul);
        localizacoes[7].imageBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.alemanha);
        localizacoes[8].imageBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.argentina);
        localizacoes[9].imageBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.mexico);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED){
            //verifica se deve exibir uma explicação sobre a necessidade da permissão
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                Toast.makeText(this, getString(R.string.message_gps),
                        Toast.LENGTH_SHORT).show();
            }
            //pede permissão
            ActivityCompat.requestPermissions(this, new String
                    []{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_GPS);
        }
        else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                    0, locationListener);
        }
    }

    private LocationListener locationListener =
            new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                }
                @Override
                public void onStatusChanged(String provider, int status,Bundle extras) {

                }
                @Override
                public void onProviderEnabled(String provider) {

                }
                @Override
                public void onProviderDisabled(String provider) {

                }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]
            permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_GPS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                                0, locationListener);
                    }
                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {  //Requisita acesso aos mapas do google
        mMap = googleMap;
    }

    public void checkin (View view) {  //Button OK para verificar capital
        String capitalName = capitalTextInputEditText.getText().toString().trim();  //O que foi digitado
        if (TextUtils.isEmpty(capitalName)) {   //Se estiver vazio
            capitalTextInputLayout.setError(getString(R.string.no_type_capital).toString());  //aparece abaixo do TextInputLayout
        }
        else{ //mandar as localizações
            int i;
            for (i=0; i<localizacoes.length; i++) {   //Verificar qual é a localizacao
                if (capitalName.equalsIgnoreCase(localizacoes[i].capitais)) {
                    loc = new LatLng(localizacoes[i].lat,   //Atribui as localizações
                            localizacoes[i].log);
                    searchOnMap (localizacoes[i].imageBitmap,localizacoes[i].paises); //Envia para realizar a busca no map
                    break;  //Sai do loop porque encontrou
                }
            }
            if (i >= localizacoes.length) {     //Se foi até o 10, não há dados para essa capital
                Toast.makeText(this,getString(R.string.not_found), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void searchOnMap (Bitmap imageBitmap, String countryName) { //Função que recebe a imagem correta, ajusta o tamanho e busca no mapa
        imageBitmap = Bitmap.createScaledBitmap(imageBitmap, 130, 110, false);
        mMap.addMarker(new MarkerOptions().position(loc).title(countryName).icon(BitmapDescriptorFactory.fromBitmap(imageBitmap)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));

    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }
}
