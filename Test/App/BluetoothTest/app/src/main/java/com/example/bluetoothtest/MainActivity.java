package com.example.bluetoothtest;

import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    //private LeDeviceListAdapter leDeviceListAdapter;
    private Handler handler;
    private boolean mScanning;
    //private LeDeviceListAdapter leDeviceListAdapter;
    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;
    private static final long SCAN_PERIOD = 10000;
    public final static UUID UUID_Service = UUID.fromString("4fafc201-1fb5-459e-8fcc-c5c9c331914b");
    public final static UUID UUID_CHARACTERISTCS = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a8");


    TextView mStatusBlueTv, mPairedTv;
    ImageView mBlueIv;
    Button mOnBtn, mOffBtn, mDiscoverBtn, mPairedBtn;

    BluetoothAdapter mBlueAdapter;
    BluetoothLeScanner mBluetoothscanner = mBlueAdapter.getBluetoothLeScanner();
    ScanFilter.Builder builder = new ScanFilter.Builder();

    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            mPairedTv.setText("Found Device");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStatusBlueTv   = findViewById(R.id.statusBluetoothTv);
        mPairedTv       = findViewById(R.id.pairedTv);
        mBlueIv         = findViewById(R.id.bluetoothIv);
        mOnBtn          = findViewById(R.id.onBtn);
        mOffBtn         = findViewById(R.id.offBtn);
        mDiscoverBtn    = findViewById(R.id.discoverableBtn);
        mPairedBtn      = findViewById(R.id.pairedBtn);

        //adapter
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();

        //check if bluetooth is available or not
        if (mBlueAdapter == null){
            mStatusBlueTv.setText("Bluetooth is not available");
        }
        else {
            mStatusBlueTv.setText("Bluetooth is available");
        }

        //set image according to bluetooth status on/off
        if (mBlueAdapter.isEnabled()) {
            mBlueIv.setImageResource(R.drawable.ic_action_on);
        }
        else {
            mBlueIv.setImageResource(R.drawable.ic_action_off);
        }

        // on btn Click
        mOnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBlueAdapter.isEnabled()) {
                    showToast("Turning on Bluetooth...");
                    // intent to on bluetooth
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent,REQUEST_ENABLE_BT);
                }
                else {
                    showToast("Bluetooth is already on");
                }
            }
        });


        //discover bluetooth btn click
        mDiscoverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBlueAdapter.isDiscovering()) {
                    showToast("Making Your Device Discoverable");
                    //Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    //startActivityForResult(intent, REQUEST_DISCOVER_BT);

                    //mBlueAdapter.startLeScan(new UUID[]{UUID_Service}, leScanCallback);

                    // Filter BLE Device
                    builder.setServiceUuid(ParcelUuid.fromString("4fafc201-1fb5-459e-8fcc-c5c9c331914b"));
                    Vector<ScanFilter> filter = new Vector<ScanFilter>();
                    filter.add(builder.build());

                    // Scan Settings
                    ScanSettings.Builder builderScanSettings = new ScanSettings.Builder();
                    builderScanSettings.setScanMode(ScanSettings.SCAN_MODE_BALANCED);
                    builderScanSettings.setReportDelay(0);


                    mBluetoothscanner.startScan(filter, builderScanSettings.build(), mScanCallback);
                    mPairedTv.setText("Scanning for ESP32");


                    /*
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mScanning = false;
                            mBlueAdapter.stopLeScan(leScanCallback);
                        }
                    }, SCAN_PERIOD);

                    mScanning = true;
                    mBlueAdapter.startLeScan(UUID_CHARACTERISTCS, leScanCallback);*/
                }
            }
        });


        //off btn click
        mOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBlueAdapter.isEnabled()) {
                    mBlueAdapter.disable();
                    showToast("Turning off Bluetooth...");
                    mBlueIv.setImageResource(R.drawable.ic_action_off);
                }
                else {
                    showToast("Bluetooth is already off...");
                }
            }
        });

        // get paired devices btn click
        mPairedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBlueAdapter.isEnabled()) {
                    mPairedTv.setText("Paired Devices");
                    Set<BluetoothDevice> devices = mBlueAdapter.getBondedDevices();
                    for (BluetoothDevice device: devices) {
                        mPairedTv.append("\nDevice: " + device.getName() + ", " + "," + device);

                    }
                }
                else {
                    //user denied to turn bluetooth on
                    showToast("Turn on bluetooth to get paired devices");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK) {
                    //bluetooth is on
                    mBlueIv.setImageResource(R.drawable.ic_action_on);
                    showToast("Bluetooth is on");
                }
                else {
                    // user denied to turn bluetooth on
                    showToast("couldn't connected on bluetooth");
                }
                break;
        }
        super.onActivityResult(requestCode,resultCode,data);
    }

    // toast message function
    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }



    /*
    // Le Device List adapter Class
    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        }
    };

    // Adapter for holding devices found through scanning.
    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;
        private LayoutInflater mInflator;
        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<BluetoothDevice>();
            mInflator = MainActivity.this.getLayoutInflater();
        }
        public void addDevice(BluetoothDevice device) {
            if(!mLeDevices.contains(device)) {
                mLeDevices.add(device);
            }
        }
        public BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }
        public void clear() {
            mLeDevices.clear();
        }
        @Override
        public int getCount() {
            return mLeDevices.size();
        }
        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }
        @Override
        public long getItemId(int i) {
            return i;
        }
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.listitem_device, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            BluetoothDevice device = mLeDevices.get(i);
            final String deviceName = device.getName();
            if (deviceName != null && deviceName.length() > 0)
                viewHolder.deviceName.setText(deviceName);
            else
                viewHolder.deviceName.setText(R.string.unknown_device);
            viewHolder.deviceAddress.setText(device.getAddress());
            return view;
        }
    }
    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
    }*/
}
