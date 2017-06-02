[ ![Download](https://api.bintray.com/packages/amuyu/maven/logger/images/download.svg) ](https://bintray.com/amuyu/maven/logger/_latestVersion)
# Logger
안드로이드 로그 메시지를 사용할 때, 항상 TAG를 넣는게 불편하다
TAG를 적어놓긴 햇지만 로그 메시지가 찍히는 위치가 헷갈린다
이런 불편함들을 편하게 하는 로그 라이브러리 입니다
# Download
Dependencies 추가
```
compile 'com.amuyu:logger:1.0.0'
```
# How to use
1. Start Logger
앱 시작 시, Logger를 생성하기 위해 다음을 호출합니다.
```java
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Logger.addLogPrinter(new DefaultLogPrinter(this));
    }
```

2. Log message
Logger.d("") 메시지를 호출해서 메시지를 남깁니다
```java
Logger.d("log message");
```

3. Log level
android.Log에서 사용하는 Log Level과 동일합니다
```java
Logger.v() Logger.d() Logger.e() Logger.i() ...
```

# Result
Logger를 사용해서 출력한 결과입니다
### String 출력
호출
```
Logger.d("Activity Created");
```
결과
```
LogLevel/클래스명/Thread: 메소드명(라인) 메시지
... D/DemoActivity#main: onCreate(25) Activity Created
```

# 파일 저장
파일로 저장하고 싶을 때는 Logger 시작 명령에 `writeFileLog(true)`를 추가합니다
```java
Logger.addLogPrinter(new DefaultLogPrinter(this)
                        .writeFileLog(true));
```
파일은 `$android_dir/data/[패키지명]/cache` 에 저장됩니다
ex) /storage/extSdCard/Android/data/com.amuyu.logger.sample/cache


# Sample
더 자세한 사용 방법 /logger-sample 에서 보실 수 있습니다