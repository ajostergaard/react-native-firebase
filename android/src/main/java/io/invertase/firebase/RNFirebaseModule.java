package io.invertase.firebase;

import java.util.Map;
import java.util.HashMap;

// react
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

// play services
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

@SuppressWarnings("WeakerAccess")
public class RNFirebaseModule extends ReactContextBaseJavaModule implements LifecycleEventListener {
  private static final String TAG = "RNFirebase";

  public RNFirebaseModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return TAG;
  }

  private WritableMap getPlayServicesStatus() {
    GoogleApiAvailability gapi = GoogleApiAvailability.getInstance();
    final int status = gapi.isGooglePlayServicesAvailable(getReactApplicationContext());
    WritableMap result = Arguments.createMap();
    result.putInt("status", status);
    if (status == ConnectionResult.SUCCESS) {
      result.putBoolean("isAvailable", true);
    } else {
      result.putBoolean("isAvailable", false);
      result.putBoolean("isUserResolvableError", gapi.isUserResolvableError(status));
      result.putString("error", gapi.getErrorString(status));
    }
    return result;
  }

  @Override
  public void onHostResume() {
    WritableMap params = Arguments.createMap();
    params.putBoolean("isForeground", true);
    Utils.sendEvent(getReactApplicationContext(), "RNFirebaseAppState", params);
  }

  @Override
  public void onHostPause() {
    WritableMap params = Arguments.createMap();
    params.putBoolean("isForeground", false);
    Utils.sendEvent(getReactApplicationContext(), "RNFirebaseAppState", params);
  }

  @Override
  public void onHostDestroy() {

  }

  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants = new HashMap<>();
    constants.put("googleApiAvailability", getPlayServicesStatus());
    return constants;
  }
}
