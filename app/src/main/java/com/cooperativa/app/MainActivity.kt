package com.cooperativa.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.cooperativa.app.ui.navigation.AppNavGraph
import com.cooperativa.app.ui.screens.accounts.viewmodel.AccountDetailViewModelFactory
import com.cooperativa.app.ui.screens.accounts.viewmodel.AccountsViewModelFactory
import com.cooperativa.app.ui.theme.CooperativaAppTheme
import com.cooperativa.app.ui.viewmodel.AuthViewModelFactory

import javax.inject.Inject

class MainActivity : ComponentActivity() {

    // Inyectamos la fábrica del ViewModel
    @Inject
    lateinit var authViewModelFactory: AuthViewModelFactory
    @Inject
    lateinit var accountsViewModelFactory: AccountsViewModelFactory
    @Inject
    lateinit var accountDetailViewModelFactory: AccountDetailViewModelFactory  // <-- agrega esto

    override fun onCreate(savedInstanceState: Bundle?) {
        // Inyectamos la Activity usando el AppComponent
        (application as CooperativaApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        setContent {
            CooperativaAppTheme {
                val navController = rememberNavController()
                // Pasamos la fábrica a la navegación
                AppNavGraph(
                    navController = navController,
                    authViewModelFactory = authViewModelFactory,
                    accountsViewModelFactory = accountsViewModelFactory,
                    accountDetailViewModelFactory = accountDetailViewModelFactory  // <-- y esto


                )
            }
        }
    }
}
