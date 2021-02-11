package com.example.articlepreview.domain.model

data class NewArticle(
    /** 記事のタイトル */
    val title: String,
    /** 記事の本文 */
    val body: String?,
    /** 記事に関連するタグ一覧 */
    val tags: List<String>,
    /** 記事内の画像URL一覧 */
    val images: List<String>,
    /** 記事にソースコードが含まれているかどうか */
    val hasSourceCodeBlock: Boolean,
    /** 記事を書いたユーザー名 */
    val userName: String,
    /** ユーザーのプロフィール画像のURL */
    val userProfileURL: String?
)