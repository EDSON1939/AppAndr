package com.cooperativa.app.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.cooperativa.app.data.models.TipoTransaccion
import com.cooperativa.app.data.models.Transaccion


class TransaccionesViewModel : ViewModel() {

    // Lista observable de transacciones
    var transacciones = mutableStateListOf<Transaccion>()
        private set

    init {
        obtenerTransacciones()
    }

    // Esto después se conectará al backend.
    private fun obtenerTransacciones() {
        val datosPrueba = listOf(
            Transaccion(TipoTransaccion.RETIRO, 144.0, "SUCURSAL EL ALTO", "18 Sept 2019"),
            Transaccion(TipoTransaccion.RETIRO, 24.0, "SUCURSAL IRPAVI", "12 Sept 2019"),
            Transaccion(TipoTransaccion.DEPOSITO, 32.0, "SUCURSAL EL ALTO", "10 Sept 2019"),
            Transaccion(TipoTransaccion.DEPOSITO, 421.0, "SUCURSAL IRPAVI", "06 Sept 2019")
        )

        transacciones.addAll(datosPrueba)
    }
}
