package com.example.serviceapp;

import static com.example.serviceapp.MyService.NOTIFICATION;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
        EditText pdf1EditText, pdf2EditText, pdf3EditText, pdf4EditText, pdf5EditText;
        String file1, file2, file3, file4, file5;
        String url1, url2, url3, url4, url5;
        static String downloadPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pdf1EditText = (EditText) findViewById(R.id.pdf1EditText);
        pdf2EditText = (EditText) findViewById(R.id.pdf2EditText);
        pdf3EditText = (EditText) findViewById(R.id.pdf3EditText);
        pdf4EditText = (EditText) findViewById(R.id.pdf4EditText);
        pdf5EditText = (EditText) findViewById(R.id.pdf5EditText);
        pdf1EditText.setText("https://static.googleusercontent.com/media/research.google.com/en//pubs/archive/45530.pdf");
        pdf2EditText.setText("https://developers.snowflake.com/wp-content/uploads/2020/09/SNO-eBook-7-Reference-Architectures-for-Application-Builders-MachineLearning-DataScience.pdf");
        pdf3EditText.setText("https://docs.aws.amazon.com/wellarchitected/latest/machine-learning-lens/wellarchitected-machine-learning-lens.pdf");
        pdf4EditText.setText("https://docs.aws.amazon.com/aws-technical-content/latest/cost-optimization-storage-optimization/cost-optimization-storage-optimization.pdf");
        pdf5EditText.setText("https://pages.databricks.com/rs/094-YMS-629/images/LearningSpark2.0.pdf");

        downloadPath = getExternalFilesDir(null).toString();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("FILE_DOWNLOADED_ACTION");
        registerReceiver(receiver, new IntentFilter(NOTIFICATION));
    }

    public void startDownload(View v) {
        url1 = pdf1EditText.getText().toString();
        url2 = pdf2EditText.getText().toString();
        url3 = pdf3EditText.getText().toString();
        url4 = pdf4EditText.getText().toString();
        url5 = pdf5EditText.getText().toString();

        file1 = url1.substring(url1.lastIndexOf('/') + 1);
        file2 = url2.substring(url2.lastIndexOf('/') + 1);
        file3 = url3.substring(url3.lastIndexOf('/') + 1);
        file4 = url4.substring(url4.lastIndexOf('/') + 1);
        file5 = url5.substring(url5.lastIndexOf('/') + 1);

        fileDownload();
    }

    public void fileDownload() {
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        downloadService(url1,file1);
        downloadService(url2,file2);
        downloadService(url3,file3);
        downloadService(url4,file4);
        downloadService(url5,file5);

    }

    private void downloadService(String url, String file) {
        Intent intent = new Intent(getBaseContext(), MyService.class);
        intent.putExtra("url", url);
        intent.putExtra("file",file);
        startService(intent);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {

                String filename = bundle.getString("file");
                Toast.makeText(getBaseContext(), filename+" File Downloaded Successfully", Toast.LENGTH_LONG).show();

            }
        }
    };
}