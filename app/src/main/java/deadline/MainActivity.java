package deadline;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import deadline.uclayout.R;
import deadline.uclayout.UcVoiceView;

public class MainActivity extends AppCompatActivity {

    UcVoiceView voiceView;
    AppCompatButton start;
    boolean visible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        voiceView = (UcVoiceView) findViewById(R.id.ucVoice);
        start = (AppCompatButton) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!visible){
                    voiceView.showVoice();
                }else{
                    voiceView.hideVoice();
                }

                visible = !visible;
            }
        });
    }
}
