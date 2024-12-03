package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.MapViewActivity

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.NearbyBottomSheet.NearByBottomSheet
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Map.OverpassResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.databinding.ActivityMapViewBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.config.Configuration.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

@AndroidEntryPoint
class MapViewActivity : BaseActivity() {
    private lateinit var binding: ActivityMapViewBinding
    private val mapsViewModel: MapsViewModel by viewModels()
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1
    private lateinit var bottomSheet: NearByBottomSheet
    private val markers = mutableListOf<Marker>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentRoute: Polyline? = null
    private var resort_latitude = 0.0
    private var reosrt_longitude = 0.0
    private var user_latitude = 0.0
    private var user_longitude = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMapViewBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        getInstance().cacheMapTileCount = 20  // Số lượng tile trong bộ nhớ cache
        getInstance().tileDownloadThreads = 12  // Số luồng tải tile đồng thời
        getInstance().setTileFileSystemCacheMaxBytes(50L * 1024 * 1024) // 50 MB cho bộ nhớ cache

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.map.setTileSource(TileSourceFactory.MAPNIK)
        binding.map.setMultiTouchControls(true)
        val mapController = binding.map.controller
        mapController.setZoom(15)
        val startPoint = GeoPoint(10.823099, 106.629662) // Hồ Chí Minh
        mapController.setCenter(startPoint);

        eventClickMyLocation()
        getIntentValue()
        eventClickBack()
        eventClickRouteToResort()


    }

    private fun getIntentValue() {
        observerData()
        resort_latitude = intent.getStringExtra(Constant.RESORT_LATITUDE)?.toDouble() ?: 0.0
        reosrt_longitude = intent.getStringExtra(Constant.RESORT_LONGITUDE)?.toDouble() ?: 0.0

        if(reosrt_longitude == 0.0 || resort_latitude == 0.0){
            Toast.makeText(this, "Không thể lấy vị trí của khu nghỉ dưỡng", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        callGetReverseGeocodingAPI(resort_latitude, reosrt_longitude)
        callGetOverpassAPI(resort_latitude, reosrt_longitude)

    }

    private fun observerData() {
        // Get Reverse Geocoding API
        mapsViewModel.geoJsonResponseLiveData.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    val geoJsonResponse = it.data
                    if (geoJsonResponse != null) {
                        val mapController = binding.map.controller
                        mapController.setZoom(12)
                        val startPoint = GeoPoint(
                            geoJsonResponse.features[0].geometry.coordinates[1],
                            geoJsonResponse.features[0].geometry.coordinates[0]
                        );
                        mapController.setCenter(startPoint);
                        val customIcon = ContextCompat.getDrawable(
                            this,
                            R.drawable.baseline_location_pin_24_blue
                        )
                        val marker = Marker(binding.map).apply {
                            position = startPoint
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            icon = customIcon // Biểu tượng tùy chỉnh
                            title = geoJsonResponse.features[0].properties.name
                            subDescription = geoJsonResponse.features[0].properties.displayName
                        }
                        binding.map.overlays.add(marker)
                        binding.map.controller.animateTo(startPoint, 20.0, 1500)
                        binding.map.invalidate()
                    }
                }

                Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    Log.d("MapViewActivitsadsy", it.message.toString())

                }

                Status.LOADING -> {
                    showLoadingWaiting(true)
                }
            }
        })

        // Get Around Location
        mapsViewModel.overpassResponseLiveData.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    val overpassResponse = it.data
                    if (overpassResponse != null) {
                        bottomSheet = NearByBottomSheet(overpassResponse.elements)
                        eventClickNearBy()

                    }
                }

                Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    Log.d("MapViewActivitsadsy", it.message.toString())
                }

                Status.LOADING -> {
                    showLoadingWaiting(true)
                }
            }
        })

        // Get Route
        mapsViewModel.directionResponseLiveData.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    val directionResponse = it.data
                    if (directionResponse != null) {
                        drawRoute(directionResponse.routes[0].geometry.coordinates)
                    }
                }

                Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    Log.d("MapViewActivitsadsy", it.message.toString())
                    hideLoadingWaiting()
                }

                Status.LOADING -> {
                    showLoadingWaiting(true)
                }
            }
        })

    }

    private fun callGetReverseGeocodingAPI(latitude: Double, longitude: Double) {
        mapsViewModel.getReverseGeocoding(latitude, longitude)
    }

    private fun callGetOverpassAPI(latitude: Double, longitude: Double) {
        mapsViewModel.getOverpass(latitude, longitude)
    }

    private fun callGetRouteAPI(start: String, end: String) {
        mapsViewModel.getRoute(start, end)
    }

    private fun eventClickBack() {
        binding.cvBack.setOnClickListener {
            finish()
        }
    }

    private fun eventClickNearBy() {
        binding.btnNearbyLocation.setOnClickListener {
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }

        bottomSheet.nearByAdapter.onItemClickListener = { element ->
            moveMapToLocation(element)
            bottomSheet.dismiss()
        }
    }

    private fun eventClickMyLocation() {
        binding.ivMyLocation.setOnClickListener {
            Toast.makeText(this, "Đang dò Vị trí của bạn", Toast.LENGTH_SHORT).show()
            getCurrentLocation()
        }
    }

    private fun eventClickRouteToResort() {
        binding.ivRouteToResort.setOnClickListener {
            routeToResort()
        }
    }

    private fun moveMapToLocation(overpassResponse: OverpassResponse.Element) {
        val geoPoint =
            GeoPoint(overpassResponse.lat, overpassResponse.lon) // Tạo GeoPoint từ tọa độ

        // Xóa tất cả các marker trước đó
        markers.forEach { marker ->
            binding.map.overlays.remove(marker)
        }
        markers.clear() // Xóa danh sách marker

        // Tạo marker mới
        val newMarker = Marker(binding.map).apply {
            position = geoPoint
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            icon = ContextCompat.getDrawable(
                this@MapViewActivity,
                R.drawable.baseline_location_pin_24_blue
            ) // Icon của marker
            title = overpassResponse.tags.name // Tiêu đề cho marker
            subDescription = overpassResponse.tags.description // Mô tả cho marker
        }

        // Thêm marker mới vào danh sách và bản đồ
        markers.add(newMarker)
        binding.map.overlays.add(newMarker)


        val start = "$reosrt_longitude,$resort_latitude" // Điểm bắt đầu
        val end = "${geoPoint.longitude},${geoPoint.latitude}"

        callGetRouteAPI(start, end)

    }

    private fun drawRoute(coordinates: List<List<Double>>) {
        val geoPoints =
            coordinates.map { GeoPoint(it[1], it[0]) } // Chuyển từ [lon, lat] sang GeoPoint

        // Xóa tuyến đường cũ nếu tồn tại
        currentRoute?.let {
            binding.map.overlays.remove(it)
        }

        // Tạo tuyến đường mới
        val polyline = Polyline().apply {
            setPoints(geoPoints)
            color = Color.BLUE // Màu của tuyến đường
            width = 10.0f       // Độ dày của tuyến đường
        }

        // Lưu tuyến đường hiện tại
        currentRoute = polyline

        // Thêm tuyến đường mới vào bản đồ
        binding.map.overlays.add(polyline)

        if (geoPoints.isNotEmpty()) {
            val boundingBox = BoundingBox.fromGeoPoints(geoPoints) // Tạo bounding box từ các GeoPoint
            binding.map.zoomToBoundingBox(boundingBox, true)        // Zoom để hiển thị toàn bộ bounding box
        }

        Toast.makeText(this, "Vẽ tuyến đường thành công", Toast.LENGTH_SHORT).show()

        // Cập nhật lại bản đồ
        binding.map.invalidate()
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Vui lòng cấp quyền truy cập vị trí", Toast.LENGTH_SHORT).show()
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                user_latitude = location.latitude
                user_longitude = location.longitude

                Log.d("MapViewActivity", "Latitude: $user_latitude, Longitude: $user_longitude")

                // Thêm Marker tại vị trí hiện tại
                addCurrentLocationMarker(user_latitude, user_longitude)
            } else {
                Toast.makeText(this, "Không thể lấy vị trí hiện tại", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun routeToResort() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Vui lòng cấp quyền truy cập vị trí", Toast.LENGTH_SHORT).show()
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                user_latitude = location.latitude
                user_longitude = location.longitude


                val start = "$user_longitude,$user_latitude"
                val end = "$reosrt_longitude,$resort_latitude"
                callGetRouteAPI(start, end)
            } else {
                Toast.makeText(this, "Không thể lấy vị trí hiện tại", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun addCurrentLocationMarker(lat: Double, lon: Double) {
        val geoPoint = GeoPoint(lat, lon) // Tạo GeoPoint từ tọa độ

        // Tạo Marker
        val marker = Marker(binding.map).apply {
            position = geoPoint
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            icon = ContextCompat.getDrawable(
                this@MapViewActivity,
                R.drawable.baseline_location_pin_24_blue
            )
            title = "Vị trí hiện tại của bạn"
        }

        // Thêm Marker vào bản đồ
        binding.map.overlays.add(marker)

        // Zoom đến vị trí Marker
        binding.map.controller.setCenter(geoPoint)
        binding.map.controller.setZoom(19.0)

        // Cập nhật bản đồ
        binding.map.invalidate()
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                Toast.makeText(this, "Cần cấp quyền để sử dụng tính năng này", Toast.LENGTH_SHORT).show()
            }
        }
    }


}