package com.rf.locationSource.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.pay2local.modem.utils.ResponseData
import com.pay2local.modem.utils.setError
import com.pay2local.modem.utils.setLoading
import com.pay2local.modem.utils.setSuccess
import com.rf.locationSource.data.repository.GoogleMapLocationRepository
import com.rf.locationSource.data.repository.request.PlaceDetailsResponse
import com.rf.locationSource.data.repository.request.PlacesAutocompleteResponse
import com.rf.locationSource.localDB.model.Place
import com.rf.locationSource.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class GoogleMapDemoViewModel @Inject constructor(
    private val googleMapLocationRepository: GoogleMapLocationRepository
) : BaseViewModel() {

    val getPlaceSuggestionsResponseModel = MutableLiveData<ResponseData<PlacesAutocompleteResponse>>()
    fun getPlaceSuggestions(keywordSearch: String) {
        getPlaceSuggestionsResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            googleMapLocationRepository.getPlaceSuggestions({ success ->
                getPlaceSuggestionsResponseModel.setSuccess(
                    success
                )
            },
                { error -> getPlaceSuggestionsResponseModel.setError(error) },
                keywordSearch,
                { message -> getPlaceSuggestionsResponseModel.setError(message) })
        }
    }

    val getPlaceDetailsResponseModel = MutableLiveData<ResponseData<PlaceDetailsResponse>>()
    fun getPlaceDetails(placeId: String) {
        getPlaceDetailsResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            googleMapLocationRepository.getPlaceDetails({ success ->
                getPlaceDetailsResponseModel.setSuccess(
                    success
                )
            },
                { error -> getPlaceDetailsResponseModel.setError(error) },
                placeId,
                { message -> getPlaceDetailsResponseModel.setError(message) })
        }
    }

    fun insertPlace(place: Place){
        viewModelScope.launch(Dispatchers.IO) {
            googleMapLocationRepository.insertPlace(place)
        }
    }


    private val _placeList = MutableLiveData<List<Place>>()
    val placeList: LiveData<List<Place>> get() = _placeList
    init {
        loadPlaces()
    }

    private fun loadPlaces() {
        viewModelScope.launch(Dispatchers.IO) {
            val places = googleMapLocationRepository.getPlaceListFromDb()
            withContext(Dispatchers.Main) {
                _placeList.value = places
            }
        }
    }

    suspend fun deletePlace(place: Place) {
        googleMapLocationRepository.googleMapDemoDB.googleMapDemoDao().deletePlace(place)
    }

    suspend fun getFirstPlaceOrder():Place? {
        return googleMapLocationRepository.googleMapDemoDB.googleMapDemoDao().getFirstPlaceOrder()
    }

    suspend fun getPlaceListMapping() :List<Place>{
        return googleMapLocationRepository.googleMapDemoDB.googleMapDemoDao().getAllPlaces()
    }

}