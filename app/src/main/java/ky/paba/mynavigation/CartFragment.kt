package ky.paba.mynavigation

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import androidx.core.content.edit

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CartFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var arCart = arrayListOf<dcBahan>()
    private lateinit var _rvCart: RecyclerView

    lateinit var spData: SharedPreferences

    // bought
    private var arBought = arrayListOf<dcBahan>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ambil data Bahan
        spData = requireContext().getSharedPreferences("dataSPBahan", MODE_PRIVATE)
        val gson = Gson()

        // Cart
        val isiCart = spData.getString("dt_cart", null)
        val typeCart = object : TypeToken<ArrayList<dcBahan>>() {}.type
        if (isiCart != null) {
            arCart = gson.fromJson(isiCart, typeCart)
        }

        // Bought
        val isiBought = spData.getString("dt_bought", null)
        val typeBought = object : TypeToken<ArrayList<dcBahan>>() {}.type
        if (isiBought != null) {
            arBought = gson.fromJson(isiBought, typeBought)  // <- benar
        }



        _rvCart = view.findViewById(R.id.rvCart)

        // Jika SP kosong, ambil data default
        if (arCart.isEmpty()) {
            SiapkanData()
            saveToSharedPref()   // save
        }
        TampilkanData() // display all

    }

    private fun saveToSharedPref() {
        val gson = Gson()
        val json = gson.toJson(arCart)
        spData.edit().putString("dt_cart", json).apply()
    }


    // read file xml / sharedPreferences
    fun SiapkanData() {
        arCart.clear()

        val json = spData.getString("dt_cart", "[]")
        val arr = JSONArray(json)

        for (i in 0 until arr.length()) {
            val obj = arr.getJSONObject(i)

            val gambar = obj.getString("gambar")
            val kategori = obj.getString("kategori")
            val nama = obj.getString("nama")

            arCart.add(dcBahan(gambar, nama, kategori))
        }
    }


    fun TampilkanData() {
        // 1 colum
        _rvCart.layoutManager = LinearLayoutManager(requireContext())

        val adapterBahan = adapterRecView(arCart, "CART")
        _rvCart.adapter = adapterBahan

        adapterBahan.setOnItemClickCallback(object : adapterRecView.OnItemClickCallback {
            override fun onItemClicked(data: dcBahan) {
                TODO("Not yet implemented")
            }

            override fun toCart(position: Int) {
                TODO("Not yet implemented")
            }

            override fun bought(position: Int) {
                val item = arCart[position]

                // Tambahkan ke array cart
                arBought.add(item)

                // Simpan ke SharedPreferences
                val gson = Gson()
                val json = gson.toJson(arBought)
                spData.edit().putString("dt_bought", json).apply()

                // delete dari cart
                arCart.removeAt(position)
                saveToSharedPref()
                TampilkanData()
            }

            override fun delData(pos: Int) {
                AlertDialog.Builder(requireContext())
                    .setTitle("Hapus Data")
                    .setMessage("Hapus bahan ini?")
                    .setPositiveButton("Hapus") { _, _ ->
                        arCart.removeAt(pos)
                        saveToSharedPref()
                        TampilkanData()
                    }
                    .setNegativeButton("Batal", null)
                    .show()
            }


        })
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}