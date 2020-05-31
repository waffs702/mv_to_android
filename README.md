# mv_to_android



## 概要
* RPGツクールMVで作成されたプロジェクトをAndroidアプリ化するためのAndroid Studio プロジェクトです。
* プラグインは不要、RPGツクールMVでデプロイメントしたファイルを所定の場所に配置するだけです。
* Android側でWebViewを表示し、RPGツクールMVで出力されたhtml/javascriptを読み込みます。
* ローカル(`file://`)で実行される `XMLHttpRequest`等のCORSを回避するために、[WebViewAssetLoader](https://developer.android.com/reference/androidx/webkit/WebViewAssetLoader)を使用しています。



## 必要なもの

* [RPGツクールMV](http://tkool.jp/mv/)
* [Android Studio](https://developer.android.com/studio/index.htm)
* Android5.1以上



## 使い方

1. RPGツクールMVプロジェクトをAndroid/iOS用にデプロイメントします。
![ss1](D:\github\mv_to_android\doc\ss1.jpg)
2. デプロイメントされたwwwフォルダ配下のファイルを、`app/htmlSource`フォルダ配下に配置します。
![ss2](D:\github\mv_to_android\doc\ss2.jpg)
3. Android Studioで本プロジェクトをインポートします。
4. PCにAndroid端末を接続してアプリを起動します。



## 動作確認

- Android Studio 4.0
- RPGツクールMV 1.6.2
- Android 5.1
- Andorid 9



## プロジェクト名の変更方法



## 画面の向き変更方法

- `app\src\main\AndroidManifest.xml`の`android:screenOrientation=`を変更します。

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
