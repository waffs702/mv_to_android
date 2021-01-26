# mv_to_android



## 概要
* RPGツクールMV/RPGツクールMZで作成されたプロジェクトをAndroidアプリ化するためのAndroid Studio プロジェクトです。
* プラグインは不要、RPGツクールMV(MZ)でデプロイメントしたファイルを所定の場所に配置するだけです。
* Android側でWebViewを表示し、RPGツクールMV(MZ)で出力されたhtml/javascriptを読み込みます。
* ローカル(`file://`)で実行される `XMLHttpRequest`等のCORSを回避するために、[WebViewAssetLoader](https://developer.android.com/reference/androidx/webkit/WebViewAssetLoader)を使用しています。



## 必要なもの

* [RPGツクールMV](https://tkool.jp/mv/) (または、[RPGツクールMZ](https://tkool.jp/mz/))
* [Android Studio](https://developer.android.com/studio/index.htm)
* Android5.1以上



## 動作確認

- RPGツクールMV
  - Android Studio 4.0
  - RPGツクールMV 1.6.2
  - Android 5.1
  - Andorid 9


- RPGツクールMZ
  - Android Studio 4.0
  - RPGツクールMZ 1.0.1
  - Andorid 10



## 使い方

1. githubよりCloneもしくは[zipをダウンロード](https://github.com/waffs702/mv_to_android/archive/master.zip)します。

2. ダウンロードした場合は、zipを解凍します。

3. mv_to_androidの`app`フォルダ配下に、`htmlSource`フォルダを作成します。

4. RPGツクールMVプロジェクトをAndroid/iOS用にデプロイメントします。

![ss1](https://raw.githubusercontent.com/wiki/waffs702/mv_to_android/images/android/ss1.jpg)

​	RPGツクールMZの場合は、ウェブブラウザ用にデプロイメントします。

​	![ss12](https://raw.githubusercontent.com/wiki/waffs702/mv_to_android/images/android/ss12.jpg)

5. デプロイメントされたwwwフォルダ配下のファイルを、mv_to_androidの`app/htmlSource`フォルダ配下に配置します。

![ss2](https://raw.githubusercontent.com/wiki/waffs702/mv_to_android/images/android/ss2.jpg)

6. Android Studioを起動します。

7. `Open an existing Android Studio project`をクリックします。

![ss3](https://raw.githubusercontent.com/wiki/waffs702/mv_to_android/images/android/ss3.jpg)


8. mv_to_androidのフォルダを選択し、`OK`をクリックします。

![ss4](https://raw.githubusercontent.com/wiki/waffs702/mv_to_android/images/android/ss4.jpg)


9. PCにAndroid端末を接続し、`Debug app`アイコンをクリックすると、Androidにアプリがインストールされデバッグモードで起動します。

![ss5](https://raw.githubusercontent.com/wiki/waffs702/mv_to_android/images/android/ss5.jpg)



## プロジェクト名の変更方法
1. プロジェクトの表示を`Android`に変更します。

![ss6](https://raw.githubusercontent.com/wiki/waffs702/mv_to_android/images/android/ss6.jpg) 

2. `app` > `java` > `com.example.mv_to_mobile_andorid` を右クリック > `Refactor` > `Rename` をクリックします。

![ss7](https://raw.githubusercontent.com/wiki/waffs702/mv_to_android/images/android/ss7.jpg)

3. Warningが表示されますが、`Rename package`をクリックします。


4. 任意のパッケージ名を入力し、`Refactor`をクリックします。(com.example.mv_to_mobile_andoridの`mv_to_mobile_andorid`の部分のみ変更されます。)

![ss8](https://raw.githubusercontent.com/wiki/waffs702/mv_to_android/images/android/ss8.jpg)


5. `Gradle Scripts` > `build.gradle(Module: app)`を開き、`applicationId`を任意のパッケージ名に変更します。その後、`Sync Now`をクリックします。
![ss9](https://raw.githubusercontent.com/wiki/waffs702/mv_to_android/images/android/ss9.jpg)


6. `app` > `manifests` > `AndroidManifest.xml` を開き、`package="com.example.XXXX"` (XXXXは入力したパッケージ名)の`example`の部分を右クリック > `Refactor` > `Rename` をクリックします。


7. Warningが表示されますが、`Rename package` をクリックします。


8. `build.gradle` で入力したものと同じものを入力して、 `Refactor` をクリックします。`example`の部分が入力したものに変更されます。

![ss10](https://raw.githubusercontent.com/wiki/waffs702/mv_to_android/images/android/ss10.jpg)


9. パッケージ名が変更されました。

![ss11](https://raw.githubusercontent.com/wiki/waffs702/mv_to_android/images/android/ss11.jpg)



## 画面の向き変更方法

- `app\src\main\AndroidManifest.xml`を開き、`android:screenOrientation=`の部分を変更します。

  - 縦向き固定: `android:screenOrientation="portrait"`
  - 横向き固定: `android:screenOrientation="landscape"`

  ```xml
  <application
  ...
          <activity android:name="com.example.mv_to_mobile_android.MainActivity"
              android:screenOrientation="portrait"
              android:launchMode="singleTop">
              ...
          </activity>
  </application>
  ```


## 今後追加予定の機能

- バナー広告、動画広告
- push通知(Firebase)
- アプリ内課金
- Twitter画面スクリーンショットシェア



## License
MIT