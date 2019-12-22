package com.rs.weatherapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.rs.weatherapp.R
import com.rs.weatherapp.network.data.ApiError
import com.rs.weatherapp.network.data.CurrentWeatherDetail
import com.rs.weatherapp.network.data.Resource
import com.rs.weatherapp.network.data.WeatherResponse
import com.rs.weatherapp.toDate
import com.rs.weatherapp.toTime
import com.rs.weatherapp.viewmodel.WeatherViewModel
import com.rs.weatherapp.viewmodel.WeatherViewModelFactory
import kotlinx.android.synthetic.main.weather_fragment_layout.*
import kotlinx.android.synthetic.main.weather_item_layout.view.*

class WeatherFragment : Fragment() {

    private lateinit var weatherViewModel: WeatherViewModel

    companion object {

        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"

        fun newInstance(latitude: Double, longitude: Double): WeatherFragment {

            val bundle = Bundle()
            bundle.putDouble(KEY_LATITUDE, latitude)
            bundle.putDouble(KEY_LONGITUDE, longitude)

            return WeatherFragment().apply { arguments = bundle }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.weather_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleUI(arguments?.getDouble(KEY_LATITUDE), arguments?.getDouble(KEY_LONGITUDE))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_refresh -> {
                handleRefreshData()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun handleRefreshData() {

        showLoading()
        val latitude = arguments?.getDouble(KEY_LATITUDE)
        val longitude = arguments?.getDouble(KEY_LONGITUDE)
        if (latitude != null && longitude != null) {
            weatherViewModel.refreshData(
                latitude,
                longitude
            )
        }
    }

    private fun handleUI(latitude: Double?, longitude: Double?) {

        weatherViewModel = ViewModelProviders.of(
            requireActivity(),
            latitude?.let {
                longitude?.let { longitude ->
                    WeatherViewModelFactory(
                        latitude,
                        longitude, requireActivity().application
                    )
                }
            }
        )[WeatherViewModel::class.java]

        weatherViewModel.weatherResponse.observe(
            this,
            Observer<Resource<WeatherResponse>> {
                // show on UI
                showOnUI(it)
            })
    }

    private fun showOnUI(resource: Resource<WeatherResponse>?) {

        when (resource?.status) {
            Resource.Status.SUCCESS -> showDataOnUI(resource.data)
            Resource.Status.LOADING -> showLoading()
            Resource.Status.ERROR -> showError(resource.apiError)
        }
    }

    private fun showError(apiError: ApiError?) {
        loading_bar.visibility = View.GONE
        data_layout.visibility = View.GONE
        error_layout.visibility = View.VISIBLE
        error_layout.text = String.format(getString(R.string.error_message), apiError?.message)
    }

    private fun showLoading() {
        loading_bar.visibility = View.VISIBLE
        data_layout.visibility = View.GONE
        error_layout.visibility = View.GONE
    }

    private fun showDataOnUI(data: WeatherResponse?) {
        loading_bar.visibility = View.GONE
        data_layout.visibility = View.VISIBLE
        latitude_info.text = String.format(getString(R.string.latitude), data?.latitude.toString())
        longitude_info.text =
            String.format(getString(R.string.longitude), data?.longitude.toString())
        timezone_info.text = String.format(getString(R.string.timezone), data?.timezone.toString())
        current_time.text =
            String.format(getString(R.string.current_time), data?.weatherDetails?.time?.toDate())
        weather_summary.text =
            String.format(getString(R.string.weather_summary), data?.weatherDetails?.summary)
        temperature.text =
            String.format(getString(R.string.temperature), data?.weatherDetails?.temperature)
        precipitation.text = String.format(
            getString(R.string.precipitation),
            data?.weatherDetails?.precipProbability
        )
        weather_summary_hourly.text = data?.hourlyWeatherList?.summary
        val viewManager = LinearLayoutManager(requireContext())
        val viewAdapter = RecyclerViewAdapter(data?.hourlyWeatherList?.data)

        hourly_weather_rv.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }
}

class RecyclerViewAdapter(private val data: List<CurrentWeatherDetail>?) :
    RecyclerView.Adapter<RecyclerViewAdapter.WeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.weather_item_layout, parent, false) as MaterialCardView
        return WeatherViewHolder(view)
    }

    override fun getItemCount() = data?.size ?: 0

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.timeView.text = data?.get(position)?.time?.toTime().toString()
        holder.summary.text = data?.get(position)?.summary
        holder.temperature.text = data?.get(position)?.temperature.toString()
    }

    class WeatherViewHolder(view: MaterialCardView) : RecyclerView.ViewHolder(view) {
        val timeView: TextView = view.time
        val summary: TextView = view.weather_summary
        val temperature: TextView = view.temperature
    }
}