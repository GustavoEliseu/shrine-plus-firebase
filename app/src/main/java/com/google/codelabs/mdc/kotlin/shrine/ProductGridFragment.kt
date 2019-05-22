package com.google.codelabs.mdc.kotlin.shrine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.shr_product_grid_fragment.*
import kotlinx.android.synthetic.main.shr_product_grid_fragment.view.*

class ProductGridFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view:View =inflater.inflate(R.layout.shr_product_grid_fragment, container, false)

        view.signout_button.setOnClickListener({
            FirebaseAuth.getInstance().signOut()
            (activity as NavigationHost).navigateTo(LoginFragment(), false)
        })

        return view
    }
}
