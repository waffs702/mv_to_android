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

    * 広告機能などが無いバージョンもあります。こちらから[zipをダウンロード](https://github.com/waffs702/mv_to_android/archive/vanilla.zip)できます。

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


## 変更方法/追加機能

- [プロジェクト名の変更方法](https://github.com/waffs702/mv_to_android/wiki/%E3%83%97%E3%83%AD%E3%82%B8%E3%82%A7%E3%82%AF%E3%83%88%E5%90%8D%E3%81%AE%E5%A4%89%E6%9B%B4%E6%96%B9%E6%B3%95)
- [画面の向き変更方法](https://github.com/waffs702/mv_to_android/wiki/%E7%94%BB%E9%9D%A2%E3%81%AE%E5%90%91%E3%81%8D%E5%A4%89%E6%9B%B4%E6%96%B9%E6%B3%95)
- [動画広告再生(リワード広告)](https://github.com/waffs702/mv_to_android/wiki/%E5%8B%95%E7%94%BB%E5%BA%83%E5%91%8A%E5%86%8D%E7%94%9F(%E3%83%AA%E3%83%AF%E3%83%BC%E3%83%89%E5%BA%83%E5%91%8A))



## 今後追加予定の機能

- バナー広告
- push通知(Firebase)
- アプリ内課金
- Twitter画面スクリーンショットシェア


## License
MIT