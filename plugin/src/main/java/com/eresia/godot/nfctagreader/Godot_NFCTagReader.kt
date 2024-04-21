package com.eresia.godot.nfctagreader

import android.nfc.NfcAdapter
import android.nfc.Tag
import android.util.Log
import android.widget.Toast
import com.eresia.godot.nfctagreader.BuildConfig
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.plugin.SignalInfo
import org.godotengine.godot.plugin.UsedByGodot

class TagReader(godotPlugin: Godot_NFCTagReader) : NfcAdapter.ReaderCallback {

	private val godotPlugin : Godot_NFCTagReader

	init {
	    this.godotPlugin = godotPlugin
	}

	override fun onTagDiscovered(tag: Tag?) {
		if(tag != null) {
			godotPlugin.onTagRead(tag)
		}
		else {
			Log.e(godotPlugin.pluginName, "Tag is null")
		}
	}
}

class Godot_NFCTagReader(godot: Godot) : GodotPlugin(godot) {

	companion object {
		val READ_TAG_SIGNAL = SignalInfo("read_tag_data", String::class.java)
		val READ_COMPLETE_TAG_SIGNAL = SignalInfo("read_complete_tag_data", String::class.java, Array<String>::class.java)
		val WRITE_TAG_SIGNAL = SignalInfo("write_tag_data", String::class.java, Array<String>::class.java)
	}

	override fun getPluginName() = BuildConfig.GODOT_PLUGIN_NAME

	override fun getPluginSignals(): Set<SignalInfo> {
		return setOf(READ_TAG_SIGNAL, READ_COMPLETE_TAG_SIGNAL, WRITE_TAG_SIGNAL)
	}

	private var nfcAdapter : NfcAdapter? = null
	private var nfcStatus = 0
	private val tagReader : TagReader = TagReader(this)

	@UsedByGodot
	private fun getNFCStatus() : Int
	{
		return nfcStatus
	}

	@UsedByGodot
	private fun enableNFC() {
		nfcAdapter = NfcAdapter.getDefaultAdapter(activity)
		activateNfc()
	}

	@UsedByGodot
	private fun disableNFC() {
		inactivateNfc()
		nfcAdapter = null
	}

	override fun onMainResume() {
		super.onMainResume()
		activateNfc()
	}

	override fun onMainPause() {
		super.onMainPause()
		inactivateNfc()
	}

	public fun onTagRead(tag : Tag) {
		emitSignal(READ_TAG_SIGNAL.name, tag.id.toString())
		logInfo("Read Tag : " + tag.id.toString())
		nfcStatus = 2
	}

	private fun activateNfc()
	{
		if(nfcAdapter == null) {
			nfcStatus = -1
			return
		}

		nfcAdapter!!.enableReaderMode(activity, tagReader,
			NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS or
					NfcAdapter.FLAG_READER_NFC_A or
					NfcAdapter.FLAG_READER_NFC_B or
					NfcAdapter.FLAG_READER_NFC_F or
					NfcAdapter.FLAG_READER_NFC_V, null)

		nfcStatus = 1
		logInfo("NFC activated")
	}

	private fun inactivateNfc()
	{
		if(nfcAdapter == null) {
			nfcStatus = -1
			return
		}

		nfcAdapter!!.disableReaderMode(activity)

		nfcStatus = 0
		logInfo("NFC inactivated")
	}

	private fun logAndNotifyDebug(data : String) {
		runOnUiThread {
			Toast.makeText(activity, data, Toast.LENGTH_LONG).show()
		}

		logInfo(data)
	}

	private fun logError(data : String) {
		Log.e(pluginName, data)
	}

	private fun logInfo(data : String) {
		Log.i(pluginName, data)
	}
}
