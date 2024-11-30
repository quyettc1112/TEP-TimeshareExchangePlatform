package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.MapViewActivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.MapsAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Map.GeoJsonResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Map.OverpassResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val mapsAPIRepository: MapsAPIRepository
) : ViewModel() {

    // Call Get Reverse Geocoding API base on latitude and longitude
    private val geoJsonResponse = MutableLiveData<Resource<GeoJsonResponse>>()
    val geoJsonResponseLiveData: MutableLiveData<Resource<GeoJsonResponse>>
        get() = geoJsonResponse

    fun getReverseGeocoding(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            geoJsonResponse.postValue(Resource.loading(null))
            mapsAPIRepository.getReverseGeocoding(latitude, longitude).let {
                geoJsonResponse.postValue(it)
            }
        }
    }

    // Get Location Around Reverse Geocoding
    private val overpassResponse = MutableLiveData<Resource<OverpassResponse>>()
    val overpassResponseLiveData: MutableLiveData<Resource<OverpassResponse>>
        get() = overpassResponse
    fun getOverpass(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            overpassResponse.postValue(Resource.loading(null))
            mapsAPIRepository.getNodes(latitude, longitude).let {
                overpassResponse.postValue(it)
            }
        }
    }

}