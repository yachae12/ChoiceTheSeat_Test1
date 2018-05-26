package com.example.user.choicetheseat_test1;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.view.Window;
import android.view.View.OnClickListener;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;


import android.widget.Button;

//import com.androidquery.AQuery; 오류나서 일단 주석달아놓겠음

//import de.hdodenhof.circleimageview.CircleImageView; 이것도 오류나서 일단 주석

public class MainActivity extends AppCompatActivity {

    //여기서부터
    BluetoothAdapter mBluetoothAdapter;

    BluetoothLeScanner mBluetoothLeScanner;

    BluetoothLeAdvertiser mBluetoothLeAdvertiser;

    private static final int PERMISSIONS = 100;

    ScanSettings.Builder mScanSettings;

    List<ScanFilter> scanFilters;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.KOREAN);

    //여기까지

    private Button button_1,button_2,
            button_5,button_6,button_7,button_8,
            button_9,button_11,button_12,
            button_13;

    private static final int REQUEST_ENABLE_BT = 1;
    // BT
    private BluetoothAdapter mBTAdapter;
    private BluetoothLeAdvertiser mBTAdvertiser;
    private BluetoothGattServer mGattServer;
    // View
    private Button mIBeaconButton3;
    private Button mIBeaconButton4;
    private Button mIBeaconButton10;
    private Button mIBeaconButton14;

    private AdvertiseCallback mAdvCallback = new AdvertiseCallback() {
        public void onStartSuccess(android.bluetooth.le.AdvertiseSettings settingsInEffect) {

            mIBeaconButton3.setEnabled(false);
            mIBeaconButton4.setEnabled(false);
            mIBeaconButton10.setEnabled(false);
            mIBeaconButton14.setEnabled(false);
        }

    };


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent=new Intent(this.getIntent());


        BtnOnClickListener onClickListener = new BtnOnClickListener();

        button_1 = (Button) findViewById(R.id.button_1);button_1.setOnClickListener(onClickListener);
        button_2 = (Button) findViewById(R.id.button_2);button_2.setOnClickListener(onClickListener);

        button_5 = (Button) findViewById(R.id.button_5);button_5.setOnClickListener(onClickListener);
        button_6 = (Button) findViewById(R.id.button_6);button_6.setOnClickListener(onClickListener);
        button_7 = (Button) findViewById(R.id.button_7);button_7.setOnClickListener(onClickListener);
        button_8 = (Button) findViewById(R.id.button_8);button_8.setOnClickListener(onClickListener);
        button_9 = (Button) findViewById(R.id.button_9);button_9.setOnClickListener(onClickListener);

        button_11 = (Button) findViewById(R.id.button_11);button_11.setOnClickListener(onClickListener);
        button_12 = (Button) findViewById(R.id.button_12);button_12.setOnClickListener(onClickListener);
        button_13 = (Button) findViewById(R.id.button_13);button_13.setOnClickListener(onClickListener);

        //여기서부터
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS);


        init();
    }

    /*ble 스캔 시작*/
    private void startScanning() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                //여기서부터
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
                mBluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
                mScanSettings = new ScanSettings.Builder();
                mScanSettings.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY);
                ScanSettings scanSettings = mScanSettings.build();

                scanFilters = new Vector<>();
                ScanFilter.Builder scanFilter = new ScanFilter.Builder();
                scanFilter.setDeviceAddress("F0:F8:F2:6F:55:A3"); //ex) 00:00:00:00:00:00
                ScanFilter scan = scanFilter.build();
                scanFilters.add(scan);
                mBluetoothLeScanner.startScan(scanFilters, scanSettings, mScanCallback);

            }
        });
    }

    /*ble 스캔 멈추기*/
    public void stopScanning() {
        //System.out.println("stopping scanning");
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mBluetoothLeScanner.stopScan(mScanCallback);

            }
        });
    }

    /*디바이스 정보*/
        ScanCallback mScanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
                try {
                    ScanRecord scanRecord = result.getScanRecord();
                    Log.d("getTxPowerLevel()", scanRecord.getTxPowerLevel() + "");
                    Log.d("onScanResult()", result.getDevice().getAddress() + "\n" + result.getRssi() + "\n" + result.getDevice().getName()
                            + "\n" + result.getDevice().getBondState() + "\n" + result.getDevice().getType());

                    String device = result.getDevice().toString();

                    if (result.getDevice().getName() == null || result.getRssi() <= -90){
                        stopScanning();
                        android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                        dialog.setTitle("알림").setMessage("이용해주셔서 감사합니다.").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                moveTaskToBack(true);
                                finish();
                                android.os.Process.killProcess(android.os.Process.myPid());
                            }
                        })
                                .create().show();
                    }
                    final ScanResult scanResult = result;

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            /*스캔확인*/
        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            Log.d("onBatchScanResults", results.size() + "");
        }

        /*스캔확인*/
        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.d("onScanFailed()", errorCode + "");
        }
    };

        /*
        이건 나도 모르겠음
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBluetoothLeScanner.stopScan(mScanCallback);
    }
    */


    /*임산부가 아닌 일반좌석버튼들*/
    class BtnOnClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) { // you can choose four button which you can see

            switch (view.getId()) {
                // 선택이 불가능한 좌석
                case R.id.button_1:
                case R.id.button_2:
                case R.id.button_5:
                case R.id.button_6:
                case R.id.button_7:
                case R.id.button_8:
                case R.id.button_9:
                case R.id.button_11:
                case R.id.button_12:
                case R.id.button_13:
                    new AlertDialog.Builder(MainActivity.this)
                            .setMessage("선택 불가능한 좌석입니다.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                    break;
            }
        }
    }

    /*advertising 멈추기 */
    @Override
    protected void onStop() {
        super.onStop();
        stopAdvertise();
    }

    /*advertising 버튼 활성화 */
    private void init() {

        // BT check
        BluetoothManager manager = BleUtil.getManager(this);
        if (manager != null) {
            mBTAdapter = manager.getAdapter();
        }
        if ((mBTAdapter == null) || (!mBTAdapter.isEnabled())) {
            //Toast.makeText(this, R.string.bt_unavailable, Toast.LENGTH_SHORT).show();
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        mIBeaconButton3 = (Button) findViewById(R.id.button_3);
        mIBeaconButton4 = (Button) findViewById(R.id.button_4);
        mIBeaconButton10 = (Button) findViewById(R.id.button_10);
        mIBeaconButton14 = (Button) findViewById(R.id.button_14);


        mIBeaconButton3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("자리선택")
                        .setMessage("선택하신 자리를 이용하시겠습니까?\n다음선택까지 7분간 선택하실 수 없습니다.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //button_3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.pink)));
                                //이거 오류나서 주석달아놓을게
                                /*패킷보내기 활성화 */
                                startIBeaconAdvertise();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("선택확인")
                                .setMessage("선택하신 자리를 이용하고 계십니까?\n아니오 선택시 다음선택이 계속됩니다.")
                                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        stopAdvertise(); //advertising 멈추기
                                        startScanning(); //스캔시작
                                        //button_3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.pink)));
                                        //이것도 오류나서 주석 달아놓을게
                                    }
                                })
                                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        stopAdvertise(); //advertising 멈추기
                                    }
                                }).show();
                    }
                }, 10000);


            }
        });

        mIBeaconButton4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("자리선택")
                        .setMessage("선택하신 자리를 이용하시겠습니까?\n다음선택까지 7분간 선택하실 수 없습니다.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //button_3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.pink)));

                                /*패킷보내기 활성화 */
                                startIBeaconAdvertise();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("선택확인")
                                .setMessage("선택하신 자리를 이용하고 계십니까?\n아니오 선택시 다음선택이 계속됩니다.")
                                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        stopAdvertise(); //advertising 멈추기
                                        startScanning(); //스캔 시작
                                        //button_3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.pink)));
                                        //이것도 오류나서 주석 달아놓을게
                                    }
                                })
                                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        stopAdvertise();
                                    }
                                }).show();
                    }
                }, 10000);

            }
        });
        mIBeaconButton10.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("자리선택")
                        .setMessage("선택하신 자리를 이용하시겠습니까?\n다음선택까지 7분간 선택하실 수 없습니다.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //button_3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.pink)));

                                /*패킷보내기 활성화 */
                                startIBeaconAdvertise();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("선택확인")
                                .setMessage("선택하신 자리를 이용하고 계십니까?\n아니오 선택시 다음선택이 계속됩니다.")
                                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        stopAdvertise(); //advertising 멈추기
                                        startScanning(); //스캔시작
                                        //button_3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.pink)));
                                        //이것도 오류나서 주석 달아놓을게
                                    }
                                })
                                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        stopAdvertise(); //advertising 멈추기
                                    }
                                }).show();
                    }
                }, 10000);

            }
        });
        mIBeaconButton14.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("자리선택")
                        .setMessage("선택하신 자리를 이용하시겠습니까?\n다음선택까지 7분간 선택하실 수 없습니다.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //button_3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.pink)));

                                /*패킷보내기 활성화 */
                                startIBeaconAdvertise();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("선택확인")
                                .setMessage("선택하신 자리를 이용하고 계십니까?\n아니오 선택시 다음선택이 계속됩니다.")
                                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        stopAdvertise(); //advertising 멈추기
                                        startScanning(); //스캔시작
                                        //button_3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.pink)));
                                        //이것도 오류나서 주석 달아놓을게
                                    }
                                })
                                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        stopAdvertise(); //advertising 멈추기
                                    }
                                }).show();
                    }
                }, 10000);

            }
        });

    }

    /*advertising 시작하기 */
    // start Advertise as iBeacon
    private void startIBeaconAdvertise() {
        if (mBTAdapter == null) {
            return;
        }
        if (mBTAdvertiser == null) {
            mBTAdvertiser = mBTAdapter.getBluetoothLeAdvertiser();
        }
        if (mBTAdvertiser != null) {
            mBTAdvertiser.startAdvertising(
                    BleUtil.createAdvSettings(true,0),
                    BleUtil.createIBeaconAdvertiseData(),
                    mAdvCallback);
        }
    }

    /*advertising 멈추기 */
    private void stopAdvertise() {
        if (mGattServer != null) {
            mGattServer.clearServices();
            mGattServer.close();
            mGattServer = null;
        }
        if (mBTAdvertiser != null) {
            mBTAdvertiser.stopAdvertising(mAdvCallback);
            mBTAdvertiser = null;
        }
        mIBeaconButton3.setEnabled(true);
        mIBeaconButton4.setEnabled(true);
        mIBeaconButton10.setEnabled(true);
        mIBeaconButton14.setEnabled(true);
    }

}