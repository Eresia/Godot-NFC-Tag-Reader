# Godot NFC Tag Reader

This Godot Android plugin can be used to read NFC tag ids.

You can follow [these instructions](https://docs.godotengine.org/en/stable/tutorials/platform/android/android_plugin.html) to build et install the plugin on your project. You can check the project on plugin/demo/project.godot to have an example of how use the plugin (check [plugin/demo/Scripts/nfc_manager.gd](plugin/demo/Scripts/nfc_manager.gd))

A beginning version of a more complete plugin with data reading and writing can be found [here](plugin/scr/main/java/com/eresia/godotnfc/GodotNFC_complete.kt). Il needs a modification of GodotActivity to works and have some issues with Intent API.