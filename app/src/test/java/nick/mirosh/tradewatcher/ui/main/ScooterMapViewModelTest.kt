package nick.mirosh.tradewatcher.ui.main

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import nick.mirosh.tradewatcher.ui.MainActivityViewModel
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.kotlin.whenever
import org.robolectric.annotation.Config


@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class ScooterMapViewModelTest {

    /*@get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var scooterMapViewModel: MainActivityViewModel

    private lateinit var mockMainRepoImpl: MainRepo


    @Before
    fun setUp() {
        mockMainRepoImpl = Mockito.mock(MainRepo::class.java)
        scooterMapViewModel = MainActivityViewModel(mockMainRepoImpl)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getScooters() with valid scooter from MainRepo, they get converted to ScooterView correctly`()  = runBlockingTest {

        val scooter = Scooter("vin")
        val scooter2 = Scooter("vin2", location = "55.45, 43.99", mileage = 300, name= "stuff")


        val myFlow = MutableStateFlow(listOf(scooter, scooter2))
        whenever(mockMainRepoImpl.getScooters()).thenReturn(myFlow)

        scooterMapViewModel.getScooters()

        val res = arrayListOf<List<ScooterView>>()

        val job = launch {
            scooterMapViewModel.scooterUI.toList(res)
        }

        val scooterView = ScooterView(vin = "vin", latLng = LatLng(0.0, 0.0), mileage = "0", name = "")
        val scooterView1 = ScooterView(vin = "vin2", latLng = LatLng(55.45, 43.99), mileage = "300", name = "stuff")

        val toMatch = arrayListOf(scooterView, scooterView1)

        assertThat(res.first(), equalTo(toMatch))

        job.cancel()
    }*/
}