## プロジェクトの概要
- Qiitaのmarkdownで書かれた記事を読みやすく表示するアプリを実装する練習（途中）
  - markdownのViewerを可能な限りネイティブで実装したい 
  - 現状はQiitaの最新記事一覧取得まで実装（markdownを扱う実装まで辿り着けていない）

## アーキテクチャ
- Single Activity + Fragment構成。画面遷移はNavigation Componentで管理
- 設計にMVVM + Repositoryパターンを採用。今やすっかりAndroidの定番に
  - データの扱いが複雑になりそうなのでdomain層を設けてUseCase取り入れるか検討中
  - ViewModelの状態はMutableStateFlowで保持、View側の公開にのみLiveDataを使用
    - 現状だとFlowにDataBindingの機能がないのでLiveDataと併用しているが、そこさえ解決されればいよいよFlowだけで十分かもしれない。ライフサイクルに関してもFlowで簡単に扱う手法が出揃ってきている印象
- DIライブラリはDagger Hiltを採用
  - 素のDaggerに比べてボイラープレートがかなり減った。それだけで採用する価値アリ
- 画像読み込み用ライブラリはPiccasoを選択。最近だとCoilなどKotlinで書かれたライブラリもあるので気が向いたらそちらも試したい

## 感想
RecyclerViewにRecyclerViewをネストするとスクロールの管理がちょっと辛くなったので下記のようなクラスを作って対応してみた。
もう少し良い方法はないか模索中
* https://github.com/akihiro-n/article-preview/blob/master/app/src/main/java/com/example/articlepreview/util/StatefulAdapter.kt
