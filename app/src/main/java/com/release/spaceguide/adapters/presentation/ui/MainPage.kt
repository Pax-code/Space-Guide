package com.release.spaceguide.adapters.presentation.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.lifecycle.Observer
import android.provider.Settings
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.release.spaceguide.R
import com.release.spaceguide.adapters.data.local.DBModel
import com.release.spaceguide.adapters.data.remote.apod.ApiModel
import com.release.spaceguide.adapters.downloadFromUrl
import com.release.spaceguide.adapters.placeHolderProgressBar
import com.release.spaceguide.adapters.viewmodel.MainPageViewModel
import com.release.spaceguide.databinding.FragmentMainPageBinding

class MainPage : Fragment() {
    private var _binding: FragmentMainPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainPageViewModel
    private lateinit var dbModel: DBModel
    private lateinit var apiModelResponse: ApiModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainPageBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        animationListener()


        viewModel = ViewModelProvider(this)[MainPageViewModel::class.java]
        viewModel.fetchData()
        viewModel.status.observe(viewLifecycleOwner) { status ->
            if (!status) {
                //if status is error then show internet connection alert
                showInternetConnectionAlert()
                binding.errorAnim.visibility = View.VISIBLE
                binding.textView2.text = "Something went wrong!"
                binding.imageView.visibility = View.INVISIBLE
                binding.textView.visibility = View.INVISIBLE
            }
        }

        viewModel.apiData.observe(viewLifecycleOwner, Observer<ApiModel> { apiModelList ->

            apiModelResponse = apiModelList

            //setting the image for main page cardView
            binding.imageView.downloadFromUrl(
                apiModelList.hdurl,
                placeHolderProgressBar(requireContext())
            )

            //setting dbmodel value with lateinit for download image
            dbModel = DBModel(apiModelList.title, apiModelList.hdurl)

            binding.textView.text = apiModelList.explanation
            binding.textView2.text = apiModelList.title

            binding.textView.visibility = View.VISIBLE
            binding.errorAnim.visibility = View.INVISIBLE
        })

        var isfavorite = false
       binding.favIcon.setOnClickListener {

           if (isfavorite){
               //set button image empty heart
               binding.favIcon.setImageResource(R.drawable.fav_icon)

           }else{
               //set button image filled heart
               binding.favIcon.setImageResource(R.drawable.fav_clicked_icon)
               viewModel.insertImageIfNotExists(dbModel)
               Toast.makeText(requireContext(), "Added to favorites", Toast.LENGTH_SHORT).show()
           }
            //change is favorite value for if is false, show bordered heart icon if is true show filled heart icon
            isfavorite = !isfavorite

       }

        binding.exloreButton.setOnClickListener {
            startScalingAnimation(binding.motionLayout,it)
        }

        binding.downloadIcon.setOnClickListener {
           viewModel.downloadImage(requireContext())
        }
    }

    private fun startScalingAnimation(viewGroup: ViewGroup, view: View) {

        //expand animation
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.exit_anim)
        animation.duration = 1500L
        viewGroup.startAnimation(animation)


        //Animation listener for scaling the screen
        animation.setAnimationListener(object : Animation.AnimationListener {

            //do something on animation start
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {

                val action = MainPageDirections.actionMainPageToExplorePage()
                Navigation.findNavController(view).navigate(action)
            }
            //do something on animation repeat
            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    @SuppressLint("SuspiciousIndentation")
    //show internet connection alert to user
    private fun showInternetConnectionAlert(){
        val alert = Dialog(requireContext())
        alert.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alert.setCancelable(false)
        alert.setContentView(R.layout.internet_connection_alert_dialog)
        alert.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alert.show()

        val close = alert.findViewById<Button>(R.id.close_button)
        val open = alert.findViewById<Button>(R.id.wifi_button)

            close.setOnClickListener {
                alert.dismiss()

            }

            open.setOnClickListener{
                //intent for wifi settings
                val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                startActivity(intent)
                alert.dismiss()
            }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchData()
    }


    private fun animationListener(){

        binding.motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                //listening the animatios is end if animation is end then set fav icon and download icon clickable
                if (p1 == R.id.end){
                    binding.favIcon.isClickable = true
                    binding.downloadIcon.isClickable = true
                }else{
                    binding.favIcon.isClickable = false
                    binding.downloadIcon.isClickable = false
                }
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
        })

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


