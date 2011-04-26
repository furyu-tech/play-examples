= Play frameworkのインストール

Play framework 1.2 を適当な場所にインストールにしてください。
ここでは~/Applications/play-1.2以下にインストールします。

$ wget http://download.playframework.org/releases/play-1.2.zip
$ unzip play-1.2.zip
$ mv play-1.2 ~/Applications

= Playのテストサーバを起動

本ファイルがあるディレクトリに移動して、playのテストサーバを起動してください。

$ cd <本ファイルのあるディレクトリ>
$ ~/Applications/play-1.2/play test

= 接続確認

ブラウザでlocalhost:9000にアクセス
