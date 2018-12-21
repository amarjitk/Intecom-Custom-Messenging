package intercom.com.intercom

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.TextView
import io.intercom.android.sdk.Intercom
import io.intercom.android.sdk.UnreadConversationCountListener
import io.intercom.android.sdk.identity.Registration

class MainActivity : AppCompatActivity(), View.OnClickListener {

    //----------------------------------------------------------------------------------------------
    // Make sure you go to SampleApplication.java to set your app ID and API key
    //----------------------------------------------------------------------------------------------
    private val USER_ID = "123456"

    //----------------------------------------------------------------------------------------------
    // If you use Identity Verification you will need to include HMAC
    // We suggest taking these values from your app. You may need to change USER_ID above to match your HMAC
    //----------------------------------------------------------------------------------------------
    private val YOUR_HMAC = ""

    private var unreadCountView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (TextUtils.isEmpty(SampleApplication.YOUR_APP_ID) || TextUtils.isEmpty(SampleApplication.YOUR_API_KEY)) {
            findViewById<View>(R.id.not_initialized).visibility = View.VISIBLE
        } else {
            //if you have provided a HMAC and data try begin secure session
            if (!TextUtils.isEmpty(YOUR_HMAC)) {
                Intercom.client().setUserHash(YOUR_HMAC)
            }

            //Enable default launcher
            Intercom.client().setLauncherVisibility(Intercom.Visibility.VISIBLE)
            //Register a user with Intercom
            Intercom.client().registerIdentifiedUser(Registration.create().withUserId(USER_ID))
            //Custom launcher
            val messengerButton = findViewById<Button>(R.id.messenger_button)
            messengerButton.setOnClickListener(this)

            //set the unread count
            unreadCountView = findViewById(R.id.unread_counter)
            val unreadCount = Intercom.client().getUnreadConversationCount()
            unreadCountView!!.setText(unreadCount.toString())
            setBadgeVisibility(unreadCount, unreadCountView!!)
        }
    }

     override fun onClick(v: View) {
        when (v.id) {
            R.id.messenger_button -> Intercom.client().displayConversationsList()
        }
    }

    private val unreadConversationCountListener = object : UnreadConversationCountListener {
        override fun onCountUpdate(unreadCount: Int) {
            setBadgeVisibility(unreadCount, unreadCountView!!)
            unreadCountView!!.text = unreadCount.toString()
        }
    }

    private fun setBadgeVisibility(unreadCount: Int, unreadCountView: TextView) {
        val visibility = if (unreadCount == 0) View.INVISIBLE else View.VISIBLE
        unreadCountView.visibility = visibility
    }

    override fun onPause() {
        super.onPause()

        Intercom.client().removeUnreadConversationCountListener(unreadConversationCountListener)
    }

    public override fun onResume() {
        super.onResume()

        Intercom.client().addUnreadConversationCountListener(unreadConversationCountListener)
        Intercom.client().handlePushMessage()
    }
}
