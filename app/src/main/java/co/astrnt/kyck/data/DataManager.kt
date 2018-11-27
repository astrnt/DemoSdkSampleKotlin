package co.astrnt.kyck.data

import android.graphics.Bitmap
import co.astrnt.demosdk.dao.BaseApiDao
import co.astrnt.demosdk.dao.RegisterApiDao
import co.astrnt.demosdk.repository.CandidateRepository
import co.astrnt.kyck.AstrntApplication
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataManager @Inject
constructor() {

    private val repository: CandidateRepository = CandidateRepository(AstrntApplication.getApi())

    fun doRegister(name: String, email: String): Observable<RegisterApiDao> {
        return repository.registerCandidate(name, email)
    }
}