package ky.paba.mynavigation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class adapterRecView(
    private val listBahan: ArrayList<dcBahan>,
    private val mode: String   // "BAHAN" atau "CART"
) : RecyclerView.Adapter<adapterRecView.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: dcBahan)

        fun bought(position: Int)

        fun toCart(position: Int)

        fun delData(pos: Int)

    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }


    inner class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val _gambarBahan = view.findViewById<ImageView>(R.id.gambarBahan)
        val _namaBahan = view.findViewById<TextView>(R.id.namaBahan)
        val _kategoriBahan = view.findViewById<TextView>(R.id.kategoriBahan)

        var _cartBtn = view.findViewById<ImageButton?>(R.id.cartBtn)
        var _boughtBtn = view.findViewById<ImageButton?>(R.id.boughtBtn)
        var _btnHapus = view.findViewById<Button>(R.id.deleteBtn)

    }

    // Membuat suatu tampilan dan mengembalikannya
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view: View;
        if (mode == "BAHAN") {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_recycler, parent, false)
        } else {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.cart_item_recycler, parent, false)
        }
        return ListViewHolder(view)
    }

    // Mengembalikan jumlah item yang tersedia untuk ditampilkan
    override fun getItemCount(): Int {
        return listBahan.size
    }

    // Menghubungkan data dengan view holder pada posisi yang ditentukan dalam RecyclerView
    override fun onBindViewHolder(
        holder: ListViewHolder,
        position: Int
    ) {
        val bahan = listBahan[position]

        Picasso.get()
            .load(bahan.gambar)
            .resize(100, 100)
            .into(holder._gambarBahan)

        holder._gambarBahan.setOnClickListener {
//            Toast.makeText(holder.itemView.context, wayang.nama, Toast.LENGTH_SHORT).show()
            onItemClickCallback.onItemClicked(listBahan[holder.adapterPosition])
        }

        holder._namaBahan.setText(bahan.nama)
        holder._kategoriBahan.setText(bahan.kategori)

        holder._cartBtn?.setOnClickListener {
            onItemClickCallback.toCart(position)
        }

        holder._boughtBtn?.setOnClickListener {
            onItemClickCallback.bought(position)
        }

        holder._btnHapus.setOnClickListener {
            onItemClickCallback.delData(position)
        }
    }

}

