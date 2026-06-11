package com.example.vetapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CitaAdapter(
    private val lista: List<Cita>,
    private val onLongClick: (Cita) -> Unit
) : RecyclerView.Adapter<CitaAdapter.CitaViewHolder>() {

    inner class CitaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardCita: MaterialCardView = view.findViewById(R.id.cardCita)
        val tvMascota: TextView = view.findViewById(R.id.tvMascota)
        val tvInfoMascota: TextView = view.findViewById(R.id.tvInfoMascota)
        val tvMotivo: TextView = view.findViewById(R.id.tvMotivo)
        val tvFechaHora: TextView = view.findViewById(R.id.tvFechaHora)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cita, parent, false)
        return CitaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CitaViewHolder, position: Int) {
        val cita = lista[position]
        val esPasada = citaEsPasada(cita)

        holder.tvMascota.text = cita.mascota

        holder.tvInfoMascota.text =
            "${cita.especie} • ${cita.raza ?: "Sin raza"} (${cita.edad ?: "?"} años)"

        holder.tvMotivo.text = "Motivo: ${cita.motivo}"

        holder.tvFechaHora.text =
            "Fecha: ${cita.fecha} a las ${cita.hora}"

        if (esPasada) {
            // CITA PASADA
            holder.cardCita.alpha = 0.45f
            holder.cardCita.isClickable = false
            holder.cardCita.isLongClickable = false
        } else {
            // CITA FUTURA
            holder.cardCita.alpha = 1f
            holder.cardCita.setOnLongClickListener {
                onLongClick(cita)
                true
            }
        }
    }

    override fun getItemCount(): Int = lista.size

    // ===================== FECHA PASADA (API 24 SAFE) =====================
    private fun citaEsPasada(cita: Cita): Boolean {
        return try {
            val formato = SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                Locale.getDefault()
            )

            val fechaCita: Date =
                formato.parse("${cita.fecha} ${cita.hora}") ?: return false

            fechaCita.before(Date())

        } catch (e: Exception) {
            false
        }
    }
}
