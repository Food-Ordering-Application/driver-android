package com.foa.driver.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.foa.driver.R;
import com.foa.driver.api.OrderService;
import com.foa.driver.dialog.QRDialog;
import com.foa.driver.model.Order;
import com.foa.driver.model.enums.DeliveryStatus;
import com.foa.driver.session.DriverModeSession;
import com.foa.driver.session.OrderSession;
import com.foa.driver.util.Helper;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

// classes needed to initialize map

// classes needed to add the location component

// classes needed to add a marker
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

// classes to calculate a route
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;

// classes needed to launch navigation UI
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;


public class MapFragment extends Fragment implements PermissionsListener {

    private View root;
    private BottomSheetBehavior bottomSheetBehavior;
    private MapView mapView;
    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private LocationComponent locationComponent;
    private DirectionsRoute currentRoute;
    private static final String TAG = "DirectionsActivity";
    private NavigationMapRoute navigationMapRoute;
    private Button startNavigationButton;
    private Order currentOrder;
    private ConstraintLayout bottomSheetLayout;

    private static final String RESTAURANT_ICON_ID = "restaurant-icon-id";
    private static final String RESTAURANT_SYMBOL_ID = "restaurant-symbol-layer-id";
    private static final String RESTAURANT_SOURCE_ID = "restaurant-source-id";

    private static final String CUSTOMER_ICON_ID = "customer-icon-id";
    private static final String CUSTOMER_SYMBOL_ID = "customer-symbol-layer-id";
    private static final String CUSTOMER_SOURCE_ID = "customer-source-id";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_map, container, false);

        bottomSheetLayout = root.findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        mapView = root.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        hiddenDeliveryInfo();

        Mapbox.getInstance(getActivity(), getString(R.string.mapbox_access_token));
        mapView.onCreate(savedInstanceState);

        bottomSheetBehavior.setPeekHeight(350);

        resetMap();

        return root;
    }

    private void addDestinationIconSymbolLayer(@NonNull Style loadedMapStyle) {
        Bitmap mBitmap = BitmapUtils.getBitmapFromDrawable(ResourcesCompat.getDrawable(getResources(),
                R.drawable.ic_maker_restaurant, null));
        loadedMapStyle.addImage(RESTAURANT_ICON_ID, mBitmap);
        GeoJsonSource geoJsonSource = new GeoJsonSource(RESTAURANT_SOURCE_ID);
        loadedMapStyle.addSource(geoJsonSource);
        SymbolLayer destinationSymbolLayer = new SymbolLayer(RESTAURANT_SYMBOL_ID, RESTAURANT_SOURCE_ID);
        destinationSymbolLayer.withProperties(
                iconImage(RESTAURANT_ICON_ID),
                iconAllowOverlap(true),
                iconIgnorePlacement(true)
        );
        loadedMapStyle.addLayer(destinationSymbolLayer);

        Bitmap mBitmap2 = BitmapUtils.getBitmapFromDrawable(ResourcesCompat.getDrawable(getResources(),
                R.drawable.ic_maker_person, null));
        loadedMapStyle.addImage(CUSTOMER_ICON_ID, mBitmap2);
        GeoJsonSource geoJsonSource2 = new GeoJsonSource(CUSTOMER_SOURCE_ID);
        loadedMapStyle.addSource(geoJsonSource2);
        SymbolLayer destinationSymbolLayer2 = new SymbolLayer(CUSTOMER_SYMBOL_ID, CUSTOMER_SOURCE_ID);
        destinationSymbolLayer2.withProperties(
                iconImage(CUSTOMER_ICON_ID),
                iconAllowOverlap(true),
                iconIgnorePlacement(true)
        );
        loadedMapStyle.addLayer(destinationSymbolLayer2);
    }

    private void resetMap() {
        mapView.getMapAsync(mapBoxMap -> mapBoxMap.setStyle(getString(R.string.navigation_guidance_day), style -> {
            this.mapboxMap = mapBoxMap;

            enableLocationComponent(style);

            addDestinationIconSymbolLayer(style);


            startNavigationButton = root.findViewById(R.id.startButton);
            startNavigationButton.setEnabled(false);
            startNavigationButton.setOnClickListener(v -> {
                boolean simulateRoute = true;
                NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                        .directionsRoute(currentRoute)
                        .shouldSimulateRoute(simulateRoute)
                        .build();
                NavigationLauncher.startNavigation(getActivity(), options);
            });

            currentOrder = OrderSession.getInstance();

            if (currentOrder != null) {
                List<Float> restaurantLngLat = currentOrder.getDelivery().getRestaurantGeom().getCoordinates();
                List<Float> customerLngLat = currentOrder.getDelivery().getCustomerGeom().getCoordinates();
                Point restaurantPoint = Point.fromLngLat(restaurantLngLat.get(0), restaurantLngLat.get(1));
                Point customerPoint = Point.fromLngLat(customerLngLat.get(0), customerLngLat.get(1));
                switch (DriverModeSession.getInstance()) {
                    case ON_GOING:
                        visibleDeliveryInfo();
                        addMarkerAndGetRoute(RESTAURANT_SOURCE_ID, restaurantPoint);
                        break;
                    case PICKED_UP:
                        visibleDeliveryInfo();
                        addMarkerAndGetRoute(CUSTOMER_SOURCE_ID, customerPoint);
                        break;
                    case COMPLETED:
                    case CANCELLED:
                    default:
                        clearRouteAndMarker(style);
                        break;
                }
                initOrderBottomSheet(currentOrder);
            } else {
                CameraPosition position = new CameraPosition.Builder()
                        .zoom(15)
                        .target(new LatLng(locationComponent.getLastKnownLocation().getLatitude(),
                                locationComponent.getLastKnownLocation().getLongitude()))
                        .tilt(30)
                        .build();
                mapBoxMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(position), 1000);
            }
        }));

    }

    private void hiddenDeliveryInfo() {
        root.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //mapView.setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    private void visibleDeliveryInfo() {
        int dp1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1,
                getActivity().getResources().getDisplayMetrics());

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, 0, 0, dp1 * 70);
        root.setLayoutParams(layoutParams);

//        CoordinatorLayout.LayoutParams layoutParams2 = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        layoutParams2.setMargins(0, 0, 0, dp1 * 120);
//        mapView.setLayoutParams(layoutParams2);

        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void initOrderBottomSheet(Order order) {
        TextView customerAddress = bottomSheetLayout.findViewById(R.id.customerAddress);
        TextView customerName = bottomSheetLayout.findViewById(R.id.customerName);
        TextView restaurantAddress = bottomSheetLayout.findViewById(R.id.restaurantAddress);
        TextView restaurantName = bottomSheetLayout.findViewById(R.id.restaurantName);
        TextView grandTotal = bottomSheetLayout.findViewById(R.id.grandTotal);
        TextView deliveryStatus = bottomSheetLayout.findViewById(R.id.deliveryStatus);
        Button btnChangeDeliveryStatus = bottomSheetLayout.findViewById(R.id.btnChangeDeliveryStatus);
        ImageButton qRCodeButton = bottomSheetLayout.findViewById(R.id.qRCodeButton);
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
                            resetMap();
                        } else {
                            Toast.makeText(getActivity(), "Lỗi! Vui lòng thực hiện lại", Toast.LENGTH_SHORT).show();
                        }
                        btnChangeDeliveryStatus.setEnabled(true);
                        progressLoading.setVisibility(View.GONE);
                    });
                    break;
                case COMPLETED:
                    DriverModeSession.clearInstance();
                    OrderSession.clearInstance();
                    hiddenDeliveryInfo();
                    break;
            }

        });

        qRCodeButton.setOnClickListener(view -> {
            if (OrderSession.getInstance() != null) {
                QRDialog dialog = new QRDialog(getActivity(), OrderSession.getInstance().getId());
                dialog.show();
            }
        });
    }

    private void getRoute(Point origin, Point destination) {
        NavigationRoute.builder(getActivity())
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        // You can get the generic HTTP info about the response
                        Log.d(TAG, "Response code: " + response.code());
                        if (response.body() == null) {
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "No routes found");
                            return;
                        }

                        currentRoute = response.body().routes().get(0);
                        startNavigationButton.setEnabled(currentRoute != null);

                        // Draw the route on the map
                        if (mapboxMap != null) {
                            try {
                                navigationMapRoute = new NavigationMapRoute(null, mapView,
                                        mapboxMap, R.style.NavigationMapRoute);
                            } catch (NullPointerException e) {

                            }
                        }
                        try{
                            navigationMapRoute.addRoute(currentRoute);
                        }catch (NullPointerException e){

                        }

                        Log.e(TAG, "ROUTE CREATE");
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });
    }

    private void addMarkerAndGetRoute(String sourceId, Point destinationPoint) {
        Point originPoint = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
                locationComponent.getLastKnownLocation().getLatitude());
        boolean isBound = true;
        if (destinationPoint == null) {
            isBound = false;
            destinationPoint = originPoint;
        }

        GeoJsonSource source = mapboxMap.getStyle().getSourceAs(sourceId);
        if (source != null) {
            source.setGeoJson(Feature.fromGeometry(destinationPoint));
        }
        getRoute(originPoint, destinationPoint);

        if(isBound){
            LatLngBounds latLngBounds = new LatLngBounds.Builder()
                    .include(new LatLng(destinationPoint.latitude(), destinationPoint.longitude())) // Northeast
                    .include(new LatLng(originPoint.latitude(), originPoint.longitude())) // Southwest
                    .build();

            mapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds,100,100,100,500), 1000);
        }
        startNavigationButton.setBackgroundResource(R.color.mapbox_blue);
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        if (PermissionsManager.areLocationPermissionsGranted(getActivity())) {
            locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(getActivity(), loadedMapStyle);
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(getActivity(), R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationComponent(mapboxMap.getStyle());
        } else {
            Toast.makeText(getActivity(), R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    }

    private void clearRouteAndMarker(Style style) {
        addMarkerAndGetRoute(RESTAURANT_SOURCE_ID, null);
        GeoJsonSource sourceRestaurant = style.getSourceAs(RESTAURANT_SOURCE_ID);
        GeoJsonSource sourceCustomer = style.getSourceAs(CUSTOMER_SOURCE_ID);

        if (sourceRestaurant != null) {
            sourceRestaurant.setGeoJson(FeatureCollection.fromJson(""));
        }

        if (sourceCustomer != null) {
            sourceCustomer.setGeoJson(FeatureCollection.fromJson(""));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}