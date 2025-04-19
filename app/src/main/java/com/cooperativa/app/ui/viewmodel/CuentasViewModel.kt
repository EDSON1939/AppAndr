package com.cooperativa.app.ui.viewmodel

/*
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.cooperativa.app.data.models.*



class CuentasViewModel : ViewModel() {

    val cuentas = mutableStateListOf<Cuenta>()
    val cuentaSeleccionada = mutableStateListOf<Cuenta>()

    init {
        cargarCuentas() // Ejemplo local
    }

    private fun cargarCuentas() {
        // Ejemplo con datos dummy
        cuentas.addAll(
            listOf(
                Cuenta(
                    id = "1",
                    tipoCuenta = TipoCuenta.AHORRO,
                    nombreCuenta = "AHORRO",
                    numeroCuenta = "112000730",
                    saldo = 0.01,
                    transacciones = listOf(
                        Transaccion(TipoTransaccion.RETIRO, 144.0, "SUCURSAL EL ALTO", "18 Sept 2019"),
                        Transaccion(TipoTransaccion.DEPOSITO, 421.0, "SUCURSAL IRPAVI", "06 Sept 2019"),
                        Transaccion(TipoTransaccion.RETIRO, 421.0, "SUCURSAL IRPAVI", "06 Sept 2019"),
                        Transaccion(TipoTransaccion.DEPOSITO, 421.0, "SUCURSAL IRPAVI", "06 Sept 2019"),
                        Transaccion(TipoTransaccion.RETIRO, 421.0, "SUCURSAL IRPAVI", "06 Sept 2019")
                    )
                ),
                Cuenta(
                    id = "2",
                    tipoCuenta = TipoCuenta.AHORRO,
                    nombreCuenta = "AHORRO-2",
                    numeroCuenta = "112000999",
                    saldo = 500.0,
                    transacciones = listOf(
                        Transaccion(TipoTransaccion.RETIRO, 200.0, "SUCURSAL CENTRO", "10 Sept 2019")
                    )
                ),
                Cuenta(
                    id = "3",
                    tipoCuenta = TipoCuenta.CREDITO,
                    nombreCuenta = "CRÉDITO",
                    numeroCuenta = "223344556",
                    saldo = 1500.0,
                    transacciones = listOf(
                        Transaccion(TipoTransaccion.DEPOSITO, 300.0, "SUCURSAL IRPAVI", "01 Sept 2019")
                    )
                )
            )
        )
        // Por defecto, seleccionamos la primera
        cuentaSeleccionada.add(cuentas.first())
    }

    fun seleccionarCuenta(cuenta: Cuenta) {
        cuentaSeleccionada.clear()
        cuentaSeleccionada.add(cuenta)
    }

    fun obtenerCuentasPorTipo(tipoCuenta: TipoCuenta): List<Cuenta> {
        return cuentas.filter { it.tipoCuenta == tipoCuenta }
    }
}*/