package androidadministration.com.deviceadministrator;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    public static final int DPM_ACTIVATION_REQUEST_CODE = 100;

    private ComponentName adminComponent;
    private DevicePolicyManager devicePolicyManager;
    private Switch cameraSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        adminComponent = new ComponentName(getPackageName(),getPackageName() + ".DeviceAdministrator");

        // Request device admin activation if not enabled.
        if (!devicePolicyManager.isAdminActive(adminComponent)) {

            Intent activateDeviceAdmin = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            activateDeviceAdmin.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponent);
            startActivityForResult(activateDeviceAdmin, DPM_ACTIVATION_REQUEST_CODE);

        }

        setContentView(R.layout.activity_main); // This must be called before initializing our switch!

        cameraSwitch = (Switch)this.findViewById(R.id.cameraSwitch);
        cameraSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isEnabled) {
                try {
                    if (isEnabled) {
                        devicePolicyManager.setCameraDisabled(adminComponent, false); // Enable camera.
                    } else {
                        devicePolicyManager.setCameraDisabled(adminComponent, true); // Disable camera.
                    }
                } catch (SecurityException securityException) {
                    Log.i("Device Administrator", "Error occurred while disabling/enabling camera - " + securityException.getMessage());
                }
            }
        });

        if (devicePolicyManager.getCameraDisabled(adminComponent)) {
            cameraSwitch.setChecked(false);
        } else {
            cameraSwitch.setChecked(true);
        }

    }

}
