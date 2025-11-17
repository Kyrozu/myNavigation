package ky.paba.mynavigation

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.GestureDetector
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [BahanFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BahanFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _nama: MutableList<String> = emptyList<String>().toMutableList()
    private var _kategori: MutableList<String> = emptyList<String>().toMutableList()
    private var _gambar: MutableList<String> = emptyList<String>().toMutableList()

    var itemBahan = mutableListOf<String>()

    private var arBahan = arrayListOf<dcBahan>()
    private lateinit var _rvBahan: RecyclerView

    lateinit var spBahan: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemBahan.addAll(
            listOf(
                "Beras | Karbohidrat | https://cdn-icons-png.flaticon.com/512/1531/1531385.png",
                "Telur Ayam | Protein | https://cdn-icons-png.flaticon.com/512/837/837560.png",
                "Bawang Putih | Bumbu | https://cdn-icons-png.flaticon.com/512/4139/4139325.png",
                "Minyak Goreng | Lemak | https://cdn-icons-png.flaticon.com/512/4264/4264676.png",
                "Santan | Lemak | https://cdn-icons-png.flaticon.com/512/1475/1475932.png",
                "Gula Pasir | Bumbu | https://cdn-icons-png.flaticon.com/512/10552/10552057.png",
                "Garam | Bumbu | https://cdn-icons-png.flaticon.com/512/3387/3387326.png",
                "Tepung Terigu | Karbohidrat | https://cdn-icons-png.flaticon.com/512/5029/5029282.png",
                "Daging Sapi | Protein | https://cdn-icons-png.flaticon.com/512/3143/3143643.png",
                "Ikan Segar | Protein | https://cdn-icons-png.flaticon.com/512/10507/10507943.png",
                "Kecap Manis | Saus | https://cdn-icons-png.flaticon.com/512/3449/3449410.png",
                "Cabe Merah | Saus | https://cdn-icons-png.flaticon.com/512/1598/1598142.png",
                "Tomat | Sayuran | https://cdn-icons-png.flaticon.com/512/4264/4264717.png",
                "Wortel | Sayuran | https://cdn-icons-png.flaticon.com/512/8511/8511087.png",
                "Kentang | Sayuran | https://cdn-icons-png.flaticon.com/512/1652/1652127.png",
                "Susu | Minuman | https://cdn-icons-png.flaticon.com/512/869/869664.png",
                "Keju | Protein | https://cdn-icons-png.flaticon.com/512/819/819827.png",
                "Roti | Karbohidrat | https://cdn-icons-png.flaticon.com/512/7093/7093198.png",
                "Madu | Saus | https://cdn-icons-png.flaticon.com/512/7093/7093198.png",
                "Teh Celup | Minuman | https://cdn-icons-png.flaticon.com/512/3504/3504837.png"
            )
        )

        // Add new bahan
        val _btnTambah = view.findViewById<Button>(R.id.btnTambah)
        _btnTambah.setOnClickListener {
            showAddDialog()
        }

        // Shared Preferences
        spBahan = requireContext().getSharedPreferences("dataSPBahan", MODE_PRIVATE)
        val gson = Gson()
        val isiSP = spBahan.getString("spBahan", null)
        val type = object : TypeToken<ArrayList<dcBahan>>() {}.type
        if (isiSP != null) {
            arBahan = gson.fromJson(isiSP, type)
        }

        _rvBahan = view.findViewById(R.id.rvBahan)

        // Jika SP kosong, ambil data default
        if (arBahan.isEmpty()) {
            SiapkanData()        // isi arBahan dari itemBahan
            saveToSharedPref()   // simpan awal
        }
        TampilkanData() // display all
    }

    private fun showAddDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_bahan, null)

        val edtNama = dialogView.findViewById<EditText>(R.id.edtNama)
        val edtKategori = dialogView.findViewById<EditText>(R.id.edtKategori)
        val edtGambar = dialogView.findViewById<EditText>(R.id.edtGambar)

        AlertDialog.Builder(requireContext())
            .setTitle("Tambah Bahan")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val nama = edtNama.text.toString()
                val kategori = edtKategori.text.toString()
                val gambar = edtGambar.text.toString()

                if (nama.isNotEmpty() && kategori.isNotEmpty() && gambar.isNotEmpty()) {
                    itemBahan.add("$nama | $kategori | $gambar")

                    SiapkanData()
                    saveToSharedPref()
                    TampilkanData()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun saveToSharedPref() {
        val gson = Gson()
        val json = gson.toJson(arBahan)
        spBahan.edit().putString("spBahan", json).apply()
    }


    fun SiapkanData() {
        arBahan.clear()

        for (item in itemBahan) {
            val parts = item.split("|").map { it.trim() }

            if (parts.size == 3) {
                val nama = parts[0]
                val kategori = parts[1]
                val gambar = parts[2]

                arBahan.add(dcBahan(gambar, nama, kategori))
            }
        }
    }


    fun TampilkanData() {
        // 1 colum
        _rvBahan.layoutManager = LinearLayoutManager(requireContext())

        val adapterWayang = adapterRecView(arBahan)
        _rvBahan.adapter = adapterWayang

        adapterWayang.setOnItemClickCallback(object : adapterRecView.OnItemClickCallback {
            override fun onItemClicked(data: dcBahan) {
                TODO("Not yet implemented")
            }

            override fun toCart(pos: Int) {
                TODO("Not yet implemented")

            }

            override fun delData(pos: Int) {
                AlertDialog.Builder(requireContext())
                    .setTitle("Hapus Data")
                    .setMessage("Hapus bahan ini?")
                    .setPositiveButton("Hapus") { _, _ ->
                        arBahan.removeAt(pos)
                        saveToSharedPref()
                        TampilkanData()
                    }
                    .setNegativeButton("Batal", null)
                    .show()
            }

        })
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bahan, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BahanFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BahanFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}