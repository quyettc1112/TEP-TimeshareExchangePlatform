package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.MapViewActivity

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.NearbyBottomSheet.NearByBottomSheet
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Map.OverpassResponse
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.MapsAmenityType
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.databinding.ActivityMapViewBinding
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.views.MapView
import org.osmdroid.config.Configuration.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon

@AndroidEntryPoint
class MapViewActivity : BaseActivity() {
    private lateinit var binding: ActivityMapViewBinding
    private val mapsViewModel: MapsViewModel by viewModels()
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1
    private lateinit var bottomSheet: NearByBottomSheet
    private val markers = mutableListOf<Marker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMapViewBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        getInstance().cacheMapTileCount = 20  // Số lượng tile trong bộ nhớ cache
        getInstance().tileDownloadThreads = 12  // Số luồng tải tile đồng thời
        getInstance().setTileFileSystemCacheMaxBytes(50L * 1024 * 1024) // 50 MB cho bộ nhớ cache
        //getInstance().tileDownloadThreads = 8

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.map.setTileSource(TileSourceFactory.MAPNIK)
        val mapController = binding.map.controller
        mapController.setZoom(15)
        val startPoint = GeoPoint(10.823099, 106.629662) // Hồ Chí Minh
        mapController.setCenter(startPoint);

        getIntentValue()
        eventClickBack()
        eventClickMyLocation()


    }

    private fun getIntentValue() {
        observerData()
        val latitude = intent.getDoubleExtra("latitude", 12.69279795)
        val longitude = intent.getDoubleExtra("longitude", 108.06307161563717)
        callGetReverseGeocodingAPI(latitude, longitude)
        callGetOverpassAPI(latitude, longitude)

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

    }

    private fun callGetReverseGeocodingAPI(latitude: Double, longitude: Double) {
        mapsViewModel.getReverseGeocoding(latitude, longitude)
    }

    private fun callGetOverpassAPI(latitude: Double, longitude: Double) {
        mapsViewModel.getOverpass(latitude, longitude)
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
        }
    }

    private fun eventClickMyLocation() {
        binding.ivMyLocation.setOnClickListener {
            val mapController = binding.map.controller
            mapController.setZoom(19)
            val startPoint = GeoPoint(12.69279795, 108.06307161563717) // Hồ Chí Minh
            mapController.setCenter(startPoint);
            binding.map.invalidate()
        }
    }

    private fun moveMapToLocation(overpassResponse: OverpassResponse.Element) {
        val geoPoint = GeoPoint(overpassResponse.lat, overpassResponse.lon) // Tạo GeoPoint từ tọa độ

        // Xóa tất cả các marker trước đó
        markers.forEach { marker ->
            binding.map.overlays.remove(marker)
        }
        markers.clear() // Xóa danh sách marker

        // Tạo marker mới
        val newMarker = Marker(binding.map).apply {
            position = geoPoint
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            icon = ContextCompat.getDrawable(this@MapViewActivity, R.drawable.baseline_location_pin_24_blue) // Icon của marker
            title = overpassResponse.tags.name // Tiêu đề cho marker
            subDescription = overpassResponse.tags.description // Mô tả cho marker
        }

        // Thêm marker mới vào danh sách và bản đồ
        markers.add(newMarker)
        binding.map.overlays.add(newMarker)

        // Di chuyển và zoom bản đồ đến vị trí mới
        binding.map.controller.animateTo(geoPoint, 20.0, 1500)
        binding.map.controller.setCenter(geoPoint)
        binding.map.invalidate() // Cập nhật bản đồ
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
        val permissionsToRequest = ArrayList<String>()
        var i = 0
        while (i < grantResults.size) {
            permissionsToRequest.add(permissions[i])
            i++
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }


}