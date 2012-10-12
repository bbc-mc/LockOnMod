MOD name     : mod_LockOn
Author       : bbc_mc (bbc-mc on github)
publish date : 2012/10/12
-----------------------------------------------------------
■機能

Mob に視点を合わせたままにするモードを追加

■環境

MC 1.2.5 + ModLoader

■導入

zip ファイルごと mods フォルダへ投入

■利用方法

  ○モード
    3 つのモードがあり、デフォルトでは " L " キーを押すたびに切り替わります。
      ・off
        動作しません
      ・no cursor
        動作します。ロックオン候補にカーソルが表示されません
      ・cursor
        動作します。ロックオン候補にカーソルが表示されます

  ○ロックオン候補選択
    画面のカーソル( + みたいなやつ) を Mob に合わせると、ロックオン候補になります。
    ロックオン候補は "cursor" モードにする事で確認できます。

  ○ロックオン
    デフォルトでは " TAB " キーを押すたびにロックオン/解除が切り替わります。
      ※ロックオン候補が認識されていない場合、ロックオンできません。

■ライセンス
  ・ソースコードのライセンスは、MIT License と GPLv3 の Dual License とします

■免責
  ・ご利用は自己責任でお願いします

■謝辞
  ・Scouter のソースを参考にさせて頂きました。公開されている作者様に感謝です。

■公開先
  ・Minecraft 日本非公式ユーザーフォーラム
    http://forum.minecraftuser.jp/viewtopic.php?f=21&t=278&p=58047#p58028
  ・github (ソースコード)
    https://github.com/bbc-mc/LockOnMod

■更新

  20121012-2
    - ロックオンした Mob が死んだ時に、カーソルが Mob が死んだ場所に残る問題を修正
    - range で指定した距離以上はなれた場合に、ロックオンを解除するように修正

----------
Copyright &copy; 2012 bbc_mc (bbc-mc on github)  
Dual licensed under the [MIT license][MIT] and [GPL license][GPL].  
[MIT]: http://www.opensource.org/licenses/mit-license.php  
[GPL]: http://www.gnu.org/licenses/gpl.html
