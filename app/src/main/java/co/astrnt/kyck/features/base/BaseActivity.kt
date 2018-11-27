package co.astrnt.kyck.features.base

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.collection.LongSparseArray
import butterknife.ButterKnife
import co.astrnt.kyck.AstrntApplication
import co.astrnt.kyck.R
import co.astrnt.kyck.injection.component.ActivityComponent
import co.astrnt.kyck.injection.component.ConfigPersistentComponent
import co.astrnt.kyck.injection.component.DaggerConfigPersistentComponent
import co.astrnt.kyck.injection.module.ActivityModule
import co.astrnt.kyck.util.Tools
import timber.log.Timber
import java.util.concurrent.atomic.AtomicLong

/**
 * Abstract activity that every other Activity in this application must implement. It provides the
 * following functionality:
 * - Handles creation of Dagger components and makes sure that instances of
 * ConfigPersistentComponent are kept across configuration changes.
 * - Set up and handles a GoogleApiClient instance that can be used to access the Google sign in
 * api.
 * - Handles signing out when an authentication error event is received.
 */
abstract class BaseActivity : AppCompatActivity() {

    private var activityComponent: ActivityComponent? = null
    private var activityId = 0L
    protected var context: Context = this

    companion object {
        private const val KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID"
        private val NEXT_ID = AtomicLong(0)
        private val componentsArray = LongSparseArray<ConfigPersistentComponent>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())
        ButterKnife.bind(this)
        // Create the ActivityComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.
        activityId = savedInstanceState?.getLong(KEY_ACTIVITY_ID) ?: NEXT_ID.getAndIncrement()
        val configPersistentComponent: ConfigPersistentComponent
        if (componentsArray.get(activityId) == null) {
            Timber.i("Creating new ConfigPersistentComponent id=%d", activityId)
            configPersistentComponent = DaggerConfigPersistentComponent.builder()
                    .appComponent(AstrntApplication[this].component)
                    .build()
            componentsArray.put(activityId, configPersistentComponent)
        } else {
            Timber.i("Reusing ConfigPersistentComponent id=%d", activityId)
            configPersistentComponent = componentsArray.get(activityId) as ConfigPersistentComponent
        }
        activityComponent = configPersistentComponent.activityComponent(ActivityModule(this))
        activityComponent?.inject(this)
    }

    @LayoutRes
    abstract fun layoutId(): Int

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(KEY_ACTIVITY_ID, activityId)
    }

    override fun onDestroy() {
        if (!isChangingConfigurations) {
            Timber.i("Clearing ConfigPersistentComponent id=%d", activityId)
            componentsArray.remove(activityId)
        }
        super.onDestroy()
    }

    fun initToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        Tools.setSystemBarColor(this, R.color.colorPrimaryDark)
    }

    fun initToolbar(toolbar: Toolbar, title: String) {
        initToolbar(toolbar)
        setTitle(title)
    }

    fun initToolbar(toolbar: Toolbar, title: String, showHome: Boolean) {
        initToolbar(toolbar)
        setTitle(title)
        showHome(showHome)
    }

    fun setTitle(title: String) {
        supportActionBar?.title = title
    }

    fun showHome(show: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(show)
    }

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    fun activityComponent() = activityComponent as ActivityComponent
}