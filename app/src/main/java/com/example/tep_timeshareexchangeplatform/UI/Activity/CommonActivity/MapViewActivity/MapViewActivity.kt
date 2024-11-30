package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.MapViewActivity

import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.ActivityMapViewBinding
import org.osmdroid.views.MapView
import org.osmdroid.config.Configuration.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon

class MapViewActivity : BaseActivity() {
    private lateinit var binding: ActivityMapViewBinding
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMapViewBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        val mapController = binding.map.controller
        mapController.setZoom(20)
        val startPoint = GeoPoint(12.69279795, 108.06307161563717);
        mapController.setCenter(startPoint);

        // Bounding box từ JSON
        val nodes = listOf(
            Pair(12.6932999,  108.0631007),
            Pair(12.6926438, 108.0635546),
            Pair( 12.6923045,108.063063),
            Pair( 12.6929521,108.0625804) ,// Quay lại điểm đầu
            Pair( 12.6932999,108.0631007) ,// Quay lại điểm đầu
        )
      //  drawPolygonFromNodes(nodes, binding.map)


        val customIcon = ContextCompat.getDrawable(this, R.drawable.baseline_location_pin_24_blue)
        val marker = Marker(binding.map).apply {
            position = startPoint
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            icon = customIcon // Biểu tượng tùy chỉnh
            title = "Khách Sạn Mường Thanh"
            subDescription = "Địa chỉ: 81 Nguyễn Tất Thành, Buôn Ma Thuột"
           /* setOnMarkerClickListener { marker, mapView ->
                Toast.makeText(this@MapViewActivity, "Bạn đã nhấn vào ${marker.title}", Toast.LENGTH_SHORT).show()
                true
            }*/
        }
        binding.map.overlays.add(marker)


    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
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
                REQUEST_PERMISSIONS_REQUEST_CODE)
        }
    }

    fun drawPolygonFromNodes(nodes: List<Pair<Double, Double>>, mapView: MapView) {
        val geoPoints = nodes.map { GeoPoint(it.first, it.second) } // Chuyển node thành GeoPoint

        val polygon = Polygon().apply {
            points = geoPoints
            outlinePaint.color = Color.BLUE // Màu viền
            outlinePaint.strokeWidth = 5f  // Độ rộng viền
            fillPaint.color = Color.argb(50, 0, 0, 255) // Màu nền
        }

        // Thêm đa giác vào bản đồ
        mapView.overlays.add(polygon)
        mapView.invalidate()
    }

}