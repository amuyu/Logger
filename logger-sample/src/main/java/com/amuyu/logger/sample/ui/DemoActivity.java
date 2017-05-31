package com.amuyu.logger.sample.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.amuyu.logger.Logger;
import com.amuyu.logger.sample.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DemoActivity extends Activity {

  private boolean isRun = false;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.demo_activity);
    ButterKnife.bind(this);
    Logger.d("Activity Created");
    Logger.e("error test");

    Map<String, String> map = new HashMap<>();
    map.put("key", "value");
    map.put("key1", null);
    Logger.d(map);

    Logger.json("doc", "{ \"key\": 3, \"value\": something}");
    Logger.d(Arrays.asList("foo", "bar"));
  }

  @OnClick({ R.id.hello, R.id.hey, R.id.hi })
  public void greetingClicked(Button button) {
    Logger.i("A button with ID %s was clicked to say '%s'.", button.getId(), button.getText());
    if(!isRun) thread.start();
    else thread.interrupt();
  }

  Thread thread = new Thread(new Runnable() {
    @Override
    public void run() {
      isRun = true;
      Logger.d("thread test");
      try {
        Thread.sleep(10000);
      } catch (InterruptedException e) {
        Logger.d("interrupted",e);
      }
      Logger.d("thread end");
      isRun = false;
    }
  });
}
