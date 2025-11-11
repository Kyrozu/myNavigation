package ky.paba.mynavigation

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
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

    private lateinit var _nama: MutableList<String>
    private lateinit var _kategori: MutableList<String>
    private lateinit var _gambar: MutableList<String>

    var itemBahan = mutableListOf<String>()

    private var arBahan = arrayListOf<dcBahan>()


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


        val lvAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, itemBahan)

        val _lv1 = view.findViewById<ListView>(R.id.bahan)
        _lv1.adapter = lvAdapter

        // TODO
        val _btnTambah = view.findViewById<Button>(R.id.btnTambah)
        _btnTambah.setOnClickListener {
            showUpdateDialog(
                null,  // berarti item baru
                null,
                itemBahan,
                lvAdapter
            )
            lvAdapter.notifyDataSetChanged()
        }

        _lv1.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(
                requireContext(),
                itemBahan[position],
                Toast.LENGTH_SHORT
            ).show()
        }

        val gestureDetector = GestureDetector(
            requireContext(),
            object : GestureDetector.SimpleOnGestureListener() {
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    val position = _lv1.pointToPosition(e.x.toInt(), e.y.toInt())
                    if (position != ListView.INVALID_POSITION) {
                        val selectedItem = itemBahan[position]
                        showActionDialog(position, selectedItem, itemBahan, lvAdapter)
                    }
                    return true
                }
            })

        _lv1.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            false
        }

    }

    fun SiapkanData() {
        _nama = resources.getStringArray(R.array.namaWayang).toMutableList()
        _kategori = resources.getStringArray(R.array.karakterUtamaWayang).toMutableList()
        _gambar = resources.getStringArray(R.array.gambarWayang).toMutableList()
    }

    fun TambahData() {
        arBahan.clear()

        for (position in _nama.indices) {
            val data = dcBahan(
                _gambar[position],
                _nama[position],
                _kategori[position]
            )
            arBahan.add(data)
        }
    }

    fun TampilkanData() {
        // 1 colum
        _rvWayang.layoutManager = LinearLayoutManager(this)

        // 2 colum
//         _rvWayang.layoutManager = GridLayoutManager(this,2)

        //
//        _rvWayang.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

        val adapterWayang = adapterRecView(arWayang)
        _rvWayang.adapter = adapterWayang

        adapterWayang.setOnItemClickCallback(object : adapterRecView.OnItemClickCallback {
            override fun onItemClicked(data: dcWayang) {

                val intent = Intent(this@MainActivity, detWayang::class.java)
                intent.putExtra("kirimData", data)
                startActivity(intent)

//                Toast.makeText(this@MainActivity, data.nama, Toast.LENGTH_SHORT)
//                    .show()
            }

//            override fun delData(pos: Int) {
//                AlertDialog.Builder(this@MainActivity)
//                    .setTitle("HAPUS DATA")
//                    .setMessage("Apakah Benar Data " + _nama[pos] + " akan dihapus ?")
//                    .setPositiveButton(
//                        "HAPUS",
//                        DialogInterface.OnClickListener { dialog, which ->
//                            _gambar.removeAt(pos)
//                            _nama.removeAt(pos)
//                            _deskripsi.removeAt(pos)
//                            _karakter.removeAt(pos)
//                            TambahData()
//                            TampilkanData()
//                        }
//                    )
//                    .setNegativeButton(
//                        "BATAL",
//                        DialogInterface.OnClickListener { dialog, which ->
//                            Toast.makeText(
//                                this@MainActivity,
//                                "Data Batal Dihapus",
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
//                    ).show()
//            }
        })
    }

    // Edit, Delate, Cancel
    private fun showActionDialog(
        position: Int,
        selectedItem: String,
        data: MutableList<String>,
        adapter: ArrayAdapter<String>
    ) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("$selectedItem")
        builder.setMessage("Pilih tindakan yang ingin dilakukan:")

        builder.setPositiveButton("Update") { _, _ ->
            //untuk Update
            showUpdateDialog(position, selectedItem, data, adapter)
        }

        builder.setNegativeButton("Hapus") { _, _ ->
            data.removeAt(position)
            adapter.notifyDataSetChanged()
            Toast.makeText(
                requireContext(),
                "Hapus Item $selectedItem",
                Toast.LENGTH_SHORT
            ).show()
        }

        builder.setNeutralButton("Batal") { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }

    private fun showUpdateDialog(
        position: Int? = null,          // null berarti item baru
        oldValue: String? = null,
        data: MutableList<String>,
        adapter: ArrayAdapter<String>
    ) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(if (oldValue != null) "Update Data" else "Tambah Data")

        val layout = LinearLayout(requireContext())
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 40, 50, 10)

        // TextView untuk data lama
        oldValue?.let {
            val tvOld = TextView(requireContext())
            tvOld.text = "Data Lama: $it"
            tvOld.textSize = 16f
            layout.addView(tvOld)
        }

        // Nama dan Kategori default
        val (oldNama, oldKategori) = oldValue?.split(" | ") ?: listOf("", "")

        val etNewNama = EditText(requireContext())
        etNewNama.hint = "Masukan Nama"
        etNewNama.setText(oldNama)

        val etNewKategori = EditText(requireContext())
        etNewKategori.hint = "Masukan Kategori"
        etNewKategori.setText(oldKategori)

        val etNewImage = EditText(requireContext())
        etNewImage.hint = "URL Gambar"
//        etNewImage.setText(oldKategori)

        layout.addView(etNewNama)
        layout.addView(etNewKategori)
        layout.addView(etNewImage)

        builder.setView(layout)

        builder.setPositiveButton("Simpan") { dialog, _ ->
            val inputNama = etNewNama.text.toString().trim().ifEmpty { oldNama }
            val inputKategori = etNewKategori.text.toString().trim().ifEmpty { oldKategori }

            val newValue = "$inputNama - $inputKategori"

            if (position != null) {
                // update existing item
                data[position] = newValue
            } else {
                // add new item
                data.add(newValue)
            }

            adapter.notifyDataSetChanged()
            Toast.makeText(requireContext(), "Data tersimpan: $newValue", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        builder.setNegativeButton("Batal") { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
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