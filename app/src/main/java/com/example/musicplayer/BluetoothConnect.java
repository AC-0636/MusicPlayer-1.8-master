package com.example.musicplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.musicplayer.service.BlunoLibrary;
import com.example.musicplayer.utility.Constants;

public class BluetoothConnect extends BlunoLibrary
{
    private Button buttonScan;
    private Button buttonBack;
    private TextView State;
    private TextView ReceivedText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_connect);
        onCreateProcess();														//onCreate Process by BlunoLibrary

        serialBegin(115200);													//set the Uart Baudrate on BLE chip to 115200

        //serialReceivedText=(TextView) findViewById(R.id.serialReveicedText);	//initial the EditText of the received data

        ReceivedText=(TextView) findViewById(R.id.receivedText);
        State=(TextView)findViewById(R.id.ConnectState);
        buttonScan = (Button) findViewById(R.id.buttonScan);					//initial the button for scanning the BLE device
        buttonScan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                buttonScanOnClickProcess();										//Alert Dialog for selecting the BLE device
            }
        });
        buttonBack=(Button)findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent Back = new Intent(BluetoothConnect.this,MusicActivity.class);
                startActivity(Back);
            }
        });

    }

    protected void onResume(){
        super.onResume();
        System.out.println("BlUNOActivity onResume");
        onResumeProcess();														//onResume Process by BlunoLibrary
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        onActivityResultProcess(requestCode, resultCode, data);					//onActivityResult Process by BlunoLibrary
        super.onActivityResult(requestCode, resultCode, data);
    }
/*
    @Override
    protected void onPause() {
        super.onPause();
        onPauseProcess();														//onPause Process by BlunoLibrary
    }

    protected void onStop() {
        super.onStop();
        onStopProcess();														//onStop Process by BlunoLibrary
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onDestroyProcess();														//onDestroy Process by BlunoLibrary
    }
*/
    @Override
    public void onConectionStateChange(connectionStateEnum theConnectionState) {//Once connection state changes, this function will be called
        switch (theConnectionState) {											//Four connection state
            case isConnected:
                buttonScan.setText("Disconnect");
                State.setText("Connected");
                break;
            case isConnecting:
                State.setText("Connecting");
                break;
            case isToScan:
                buttonScan.setText("Scan");
                break;
            case isScanning:
                State.setText("Scanning");
                break;
            case isDisconnecting:
                State.setText("Disconnecting");
                break;
            default:
                break;
        }
    }

   // @Override
    public void onSerialReceived(String theString) {							//Once connection data received, this function will be called
        // TODO Auto-generated method stub
        ReceivedText.append(theString);							//append the text into the EditText
      switch (theString){
          case "UP":
              break;
          case "DOWN":
              break;
          case "LEFT":
              sendBroadcast(Constants.ACTION_PRV);
              break;
          case "RIGHT":
              sendBroadcast(Constants.ACTION_NEXT);
              break;
          case "NEAR":
              sendBroadcast(Constants.ACTION_PAUSE);
              break;
          case "FAR":
              sendBroadcast(Constants.ACTION_PLAY);
              break;
      }

        ((ScrollView)ReceivedText.getParent()).fullScroll(View.FOCUS_DOWN);
    }

    private void sendBroadcast(String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        sendBroadcast(intent);
    }


}