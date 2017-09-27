package tw.com.tcc.ultimatetictactoe;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button restartButton = (Button)findViewById(R.id.buttonRestart);
        final UtttView utttView = (UtttView)findViewById(R.id.utttView);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utttView.init();

            }
        });

    }

}
