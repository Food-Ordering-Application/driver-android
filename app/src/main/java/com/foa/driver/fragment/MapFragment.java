package com.foa.driver.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import com.foa.driver.MainActivity;
import com.foa.driver.R;
import com.foa.driver.api.OrderService;
import com.foa.driver.model.Order;
import com.foa.driver.model.enums.DeliveryStatus;
import com.foa.driver.session.DriverModeSession;
import com.foa.driver.session.OrderSession;
import com.foa.driver.util.Helper;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.os.Looper.getMainLooper;
import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;


public class MapFragment extends Fragment implements PermissionsListener{

    private View root;
    private BottomSheetBehavior bottomSheetBehavior;
    private static final String ROUTE_LAYER_ID = "route-layer-id";
    private static final String ROUTE_SOURCE_ID = "route-source-id";
    private DirectionsRoute drivingRoute;
    private PermissionsManager permissionsManager;
    private MapView mapView;
    private MapboxMap mapBoxMap;
    private LocationComponent locationComponent;
    private Point origin;
    private Point destination;
    private MainActivityLocationCallback callback;
    private Order currentOrder;
    private ConstraintLayout bottomSheetLayout;
    private static final String RESTAURANT_IMAGE_ID = "restaurantIcon";
    private static final String CUSTOMER_IMAGE_ID = "customerIcon";
    private final List<Lifecycle.State> states = new ArrayList<>();
    private TextView unitsText;
    private boolean isImperial = true;
    private Point restaurantPoint;
    private Point customerPoint;
    private  SymbolOptions symbolOptions;
    private  SymbolOptions symbolOptions2;
    private  SymbolManager symbolManager;
    private GeoJsonSource routeLineSource;



    public MapFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_map, container, false);

        bottomSheetLayout = root.findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        callback = new MainActivityLocationCallback((MainActivity) getActivity());
        mapView = root.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        resetMap();

        return root;
    }

    private void addMarker(Style style,LatLng restaurant, LatLng customer){
        Drawable drawableRestaurant = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_maker_restaurant, null);
        Bitmap bitmapRestaurant = BitmapUtils.getBitmapFromDrawable(drawableRestaurant);
        style.addImage(RESTAURANT_IMAGE_ID,bitmapRestaurant);

        Drawable drawableCustomer = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_maker_person, null);
        Bitmap bitmapCustomer = BitmapUtils.getBitmapFromDrawable(drawableCustomer);
        style.addImage(CUSTOMER_IMAGE_ID,bitmapCustomer );

        symbolManager = new SymbolManager(mapView, mapBoxMap, style);
        symbolManager.setIconAllowOverlap(true);
        symbolManager.setTextAllowOverlap(true);
        symbolOptions = new SymbolOptions()
                .withLatLng(restaurant)
                .withIconImage(RESTAURANT_IMAGE_ID)
                .withIconSize(1f);
        symbolOptions2 = new SymbolOptions()
                .withLatLng(customer)
                .withIconImage(CUSTOMER_IMAGE_ID)
                .withIconSize(1f);
        symbolManager.create(symbolOptions);
        symbolManager.create(symbolOptions2);
    }

    private void clearMakers(){
        symbolManager.deleteAll();
        routeLineSource.setGeoJson("");
    }

    private void resetMap() {
        mapView.getMapAsync(mapBoxMap -> mapBoxMap.setStyle(Style.MAPBOX_STREETS, style -> {
            this.mapBoxMap = mapBoxMap;

            initSource(style);

            initLayers(style);

            enableLocationComponent(style);

            origin = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
                    locationComponent.getLastKnownLocation().getLatitude());
            Location currentLocation = locationComponent.getLastKnownLocation();
            cameraPositionUpdate(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),12);

            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            currentOrder = OrderSession.getInstance();

            if (currentOrder != null) {
                List<Float> restaurantLngLat = currentOrder.getDelivery().getRestaurantGeom().getCoordinates();
                List<Float> customerLngLat = currentOrder.getDelivery().getCustomerGeom().getCoordinates();
                LatLng restaurantLatLng = new LatLng(restaurantLngLat.get(1), restaurantLngLat.get(0));
                LatLng customerLatLng = new LatLng(customerLngLat.get(1), customerLngLat.get(0));
                restaurantPoint  = Point.fromLngLat(restaurantLngLat.get(0), restaurantLngLat.get(1));
                customerPoint = Point.fromLngLat(customerLngLat.get(0), customerLngLat.get(1));
                initOrderBottomSheet(currentOrder);
                switch (DriverModeSession.getInstance()) {
                    case ON_GOING:
                        addMarker(style,restaurantLatLng,customerLatLng);
                        getSingleRoute(origin,restaurantPoint);
                        break;
                    case PICKED_UP:
                        addMarker(style,restaurantLatLng,customerLatLng);
                        getSingleRoute(origin,customerPoint);
                        break;
                    case COMPLETED:
                    case CANCELLED:
                    default:
                        hiddenDeliveryInfo();
                        OrderSession.clearInstance();
                        clearMakers();
                        break;
                }
            }else{
                hiddenDeliveryInfo();
            }

            initLocationEngine();
        }));

    }

    private void hiddenDeliveryInfo(){
        root.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    private void initOrderBottomSheet(Order order) {
        TextView customerAddress = bottomSheetLayout.findViewById(R.id.customerAddress);
        TextView customerName = bottomSheetLayout.findViewById(R.id.customerName);
        TextView restaurantAddress = bottomSheetLayout.findViewById(R.id.restaurantAddress);
        TextView restaurantName = bottomSheetLayout.findViewById(R.id.restaurantName);
        TextView grandTotal = bottomSheetLayout.findViewById(R.id.grandTotal);
        TextView deliveryStatus = bottomSheetLayout.findViewById(R.id.deliveryStatus);
        Button btnChangeDeliveryStatus = bottomSheetLayout.findViewById(R.id.btnChangeDeliveryStatus);
        LinearLayout progressLoading = bottomSheetLayout.findViewById(R.id.progressLoading);

        customerAddress.setText(order.getDelivery().getCustomerAddress());
        customerName.setText(order.getDelivery().getCustomerName());
        restaurantAddress.setText(order.getDelivery().getRestaurantAddress());
        restaurantName.setText(order.getDelivery().getRestaurantName());
        grandTotal.setText(Helper.formatMoney(order.getGrandTotal()));
        btnChangeDeliveryStatus.setOnClickListener(view -> {
            progressLoading.setVisibility(View.VISIBLE);
            btnChangeDeliveryStatus.setEnabled(false);
            switch (DriverModeSession.getInstance()) {
                case ON_GOING:
                    OrderService.pickupOrder(order.getId(), success -> {
                        if (success) {
                            DriverModeSession.setInstance(DeliveryStatus.PICKED_UP);
                            deliveryStatus.setText("ĐANG GIAO MÓN");
                            btnChangeDeliveryStatus.setText("GIAO MÓN");
                            List<Float> restaurantLatLng = currentOrder.getDelivery().getCustomerGeom().getCoordinates();
                            destination = Point.fromLngLat(restaurantLatLng.get(0), restaurantLatLng.get(1));
                            resetMap();
                        } else {
                            Toast.makeText(getActivity(), "Lỗi! Vui lòng thực hiện lại", Toast.LENGTH_SHORT).show();
                        }
                        btnChangeDeliveryStatus.setEnabled(true);
                        progressLoading.setVisibility(View.GONE);
                    });
                    break;
                case PICKED_UP:
                    DriverModeSession.setInstance(DeliveryStatus.COMPLETED);
                    OrderService.completeOrder(order.getId(), success -> {
                        if (success) {
                            deliveryStatus.setText("ĐÃ GIAO MÓN");
                            btnChangeDeliveryStatus.setText("XONG");
                        } else {
                            Toast.makeText(getActivity(), "Lỗi! Vui lòng thực hiện lại", Toast.LENGTH_SHORT).show();
                        }
                        btnChangeDeliveryStatus.setEnabled(true);
                        progressLoading.setVisibility(View.GONE);
                    });
                    break;
                case COMPLETED:
                    DriverModeSession.clearInstance();
                    hiddenDeliveryInfo();
                    OrderSession.clearInstance();
                    clearMakers();
                    break;
            }

        });
    }

    private void cameraPositionUpdate(LatLng location, int zoom) {
        CameraPosition position = new CameraPosition.Builder()
                .zoom(zoom)
                .target(location)
                .tilt(30)
                .build();
        mapBoxMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), 1000);
    }

    private void initLocationEngine() {
        LocationEngine locationEngine = LocationEngineProvider.getBestLocationEngine(getActivity());

        long DEFAULT_INTERVAL_IN_MILLISECONDS = 5000L;
        long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 2;
        LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            getActivity().finish();
            return;
        }
        locationEngine.requestLocationUpdates(request, callback, getMainLooper());
        locationEngine.getLastLocation(callback);
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {

        if (PermissionsManager.areLocationPermissionsGranted(getActivity())) {

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            LocationComponentOptions customLocationComponentOptions = LocationComponentOptions.builder(getActivity())
                    .pulseEnabled(true)
                    .build();

            locationComponent = mapBoxMap.getLocationComponent();
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(getActivity(), loadedMapStyle)
                            .locationComponentOptions(customLocationComponentOptions)
                            .build());

            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING_GPS);
            locationComponent.setRenderMode(RenderMode.GPS);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }


    private class MainActivityLocationCallback
            implements LocationEngineCallback<LocationEngineResult> {

        private final WeakReference<MainActivity> activityWeakReference;

        MainActivityLocationCallback(MainActivity activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(LocationEngineResult result) {
            MainActivity activity = activityWeakReference.get();

            if (activity != null) {
                Location location = result.getLastLocation();

                if (location == null) {
                    return;
                }
                if (OrderSession.getInstance()!=null){
                    Point origin = Point.fromLngLat(result.getLastLocation().getLongitude(), result.getLastLocation().getLatitude());
                    switch (DriverModeSession.getInstance()) {
                        case ON_GOING:
                            cameraPositionUpdate(new LatLng(result.getLastLocation().getLatitude(), result.getLastLocation().getLongitude()),18);
                            getSingleRoute(origin,restaurantPoint);
                            break;
                        case PICKED_UP:
                            cameraPositionUpdate(new LatLng(result.getLastLocation().getLatitude(), result.getLastLocation().getLongitude()),18);
                            getSingleRoute(origin,customerPoint);
                            break;
                    }
                }

                if (mapBoxMap != null && result.getLastLocation() != null) {
                    mapBoxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());
                }
            }
        }

        @Override
        public void onFailure(@NonNull Exception exception) {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapBoxMap.getStyle(this::enableLocationComponent);
        } else {
            getActivity().finish();
        }
    }


    private void initSource(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(ROUTE_SOURCE_ID));
//        GeoJsonSource iconGeoJsonSource = new GeoJsonSource(ICON_SOURCE_ID,
//                Feature.fromGeometry(Point.fromLngLat(destination.longitude(),
//                        destination.latitude())));
//        loadedMapStyle.addSource(iconGeoJsonSource);
    }

    private void showRouteLine() {
        if (mapBoxMap != null) {
            mapBoxMap.getStyle(style -> {
                routeLineSource = style.getSourceAs(ROUTE_SOURCE_ID);
                if (routeLineSource != null) {
                    routeLineSource.setGeoJson(LineString.fromPolyline(drivingRoute.geometry(),
                            PRECISION_6));
                }
            });
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initLayers(@NonNull Style loadedMapStyle) {
        LineLayer routeLayer = new LineLayer(ROUTE_LAYER_ID, ROUTE_SOURCE_ID);

        routeLayer.setProperties(
                lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
                lineWidth(5f),
                lineColor(Color.parseColor("#006eff"))
        );
        loadedMapStyle.addLayer(routeLayer);

//        loadedMapStyle.addImage(RED_PIN_ICON_ID, BitmapUtils.getBitmapFromDrawable(
//                getResources().getDrawable(R.drawable.ic_marker_customer)));
//
//        loadedMapStyle.addLayer(new SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID).withProperties(
//                iconImage(RED_PIN_ICON_ID),
//                iconIgnorePlacement(true),
//                iconAllowOverlap(true),
//                iconOffset(new Float[]{0f, -9f})));
    }

    private void getSingleRoute(Point origin, Point destination) {
        MapboxDirections client = MapboxDirections.builder()
                .origin(origin)
                .destination(destination)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .accessToken(getString(R.string.mapbox_access_token))
                .build();


        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(@NotNull Call<DirectionsResponse> call, @NotNull Response<DirectionsResponse> response) {
                if (response.body() == null) {
                    return;
                } else if (response.body().routes().size() < 1) {
                    return;
                }
                drivingRoute = response.body().routes().get(0);
                showRouteLine();
            }

            @Override
            public void onFailure(@NotNull Call<DirectionsResponse> call, @NotNull Throwable throwable) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}