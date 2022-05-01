package com.ishwar.kotlinweatherapp

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.ishwar.kotlinweatherapp.databinding.ActivityMainBinding
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {
    val CITY: String = "Jodhpur"
    val API: String = "5bb030b01777492788751524212410&q"
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        weatherTask().execute()


    }

    inner class weatherTask() : AsyncTask<String, Void, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            binding.loader.visibility = View.VISIBLE
            binding.mainContainer.visibility = View.GONE
            binding.errorText.visibility = View.GONE

        }

        //       https://api.weatherapi.com/v1/forecast.json?key=5bb030b01777492788751524212410&q=jodhpur&days=1&aqi=yes&alerts=yes
        override fun doInBackground(vararg params: String?): String? {
            var response: String?
            try {
                response =
                    URL("https://api.weatherapi.com/v1/forecast.json?key=$API=$CITY&days=1&aqi=yes&alerts=yes").readText(
                        Charsets.UTF_8
                    )
            } catch (e: Exception) {
                response = null
            }
            return response

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObj = JSONObject(result)
                val locationObj = jsonObj.getJSONObject("location")
                val currentOBJ = jsonObj.getJSONObject("current")
                val foreCastObj = jsonObj.getJSONObject("forecast")
                val cityName = locationObj.getString("name")
                val lastUpdatedAt = currentOBJ.getString("last_updated")
                val temp = currentOBJ.getString("temp_c")
                val currentStatus = currentOBJ.getJSONObject("condition").getString("text")
                val wind = currentOBJ.getString("wind_mph")
                val humidity = currentOBJ.getString("humidity")

                val maxTamp =
                    foreCastObj.getJSONArray("forecastday").getJSONObject(0).getJSONObject("day")
                        .getString("maxtemp_c")
                val minTemp =
                    foreCastObj.getJSONArray("forecastday").getJSONObject(0).getJSONObject("day")
                        .getString("mintemp_c")
//               val foreCastArray : JSONArray =foreCastObj.getJSONArray("forecastday")
//                val firstObject:JSONObject=foreCastArray.getJSONObject(0)
//
//                     Log.d("length of array ", "${firstObject.getJSONObject("day").getString("maxtemp_c")}");
//                for(i in foreCastArray.length()..0){
//                Log.d("array value","${foreCastArray[i]}")
//                }
                val sunRise =
                    foreCastObj.getJSONArray("forecastday").getJSONObject(0).getJSONObject("astro")
                        .getString("sunrise")
                val sunSet =
                    foreCastObj.getJSONArray("forecastday").getJSONObject(0).getJSONObject("astro")
                        .getString("sunset")
                binding.addressTv.text = cityName.toString()
                binding.updatedTv.text = lastUpdatedAt.toString()
                binding.temp.text = temp.toString()
                binding.status.text = currentStatus.toString()
                binding.windSpped.text = wind.toString()
                binding.humidityTime.text = humidity.toString()
                binding.tempMax.text = "Max Tamp - ${maxTamp} "
                binding.minTamp.text = "Min Tamp - ${minTemp}"
                binding.sunRiseTime.text = "$sunRise"
                binding.sunsetTime.text = "$sunSet"
                binding.loader.visibility = View.GONE
                binding.mainContainer.visibility = View.VISIBLE
                binding.errorText.visibility = View.GONE


                // Log.d("location object  ", locationObj.getString("country").toString())

            } catch (e: Exception) {
                Log.d("exception", " ${e.localizedMessage}")
                binding.loader.visibility = View.GONE
                binding.errorText.visibility = View.VISIBLE

            }
        }

    }
}