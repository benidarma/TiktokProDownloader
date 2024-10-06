# TiktokProDownloader
Download Video dan Musik dari Tiktok Tanpa Watermark.

<p><strong>Screenshots</strong>：</p>

<p>
	<a target="_blank" rel="noopener noreferrer" href="https://github.com/benidarma/TiktokProDownloader/blob/master/Screenshots/Screenshot_1633774983.png">
		<img src="https://github.com/benidarma/TiktokProDownloader/blob/master/Screenshots/Screenshot_1633774983.png" width="200" style="max-width: 100%;">
	</a> 
	<a target="_blank" rel="noopener noreferrer" href="https://github.com/benidarma/TiktokProDownloader/blob/master/Screenshots/Screenshot_1633774988.png">
		<img src="https://github.com/benidarma/TiktokProDownloader/blob/master/Screenshots/Screenshot_1633774988.png" width="200" style="max-width: 100%;">
	</a> 
	<a target="_blank" rel="noopener noreferrer" href="https://github.com/benidarma/TiktokProDownloader/blob/master/Screenshots/Screenshot_1633775011.png">
		<img src="https://github.com/benidarma/TiktokProDownloader/blob/master/Screenshots/Screenshot_1633775011.png" width="200" style="max-width: 100%;">
	</a>
	<a target="_blank" rel="noopener noreferrer" href="https://github.com/benidarma/TiktokProDownloader/blob/master/Screenshots/Screenshot_1633775020.png">
		<img src="https://github.com/benidarma/TiktokProDownloader/blob/master/Screenshots/Screenshot_1633775020.png" width="200" style="max-width: 100%;">
	</a>
	<a target="_blank" rel="noopener noreferrer" href="https://github.com/benidarma/TiktokProDownloader/blob/master/Screenshots/Screenshot_1633775068.png">
		<img src="https://github.com/benidarma/TiktokProDownloader/blob/master/Screenshots/Screenshot_1633775068.png" width="200" style="max-width: 100%;">
	</a>
	<a target="_blank" rel="noopener noreferrer" href="https://github.com/benidarma/TiktokProDownloader/blob/master/Screenshots/Screenshot_1633775087.png">
		<img src="https://github.com/benidarma/TiktokProDownloader/blob/master/Screenshots/Screenshot_1633775087.png" width="200" style="max-width: 100%;">
	</a>
	<a target="_blank" rel="noopener noreferrer" href="https://github.com/benidarma/TiktokProDownloader/blob/master/Screenshots/Screenshot_1633775099.png">
		<img src="https://github.com/benidarma/TiktokProDownloader/blob/master/Screenshots/Screenshot_1633775099.png" width="200" style="max-width: 100%;">
	</a>
</p>

<p><strong>Note</strong>：</p>
<p>Ini hanya contoh aplikasi unduhan untuk Tiktok dan memerlukan api backend. Lihat file key-native-lib.cpp dibawah, ubah string api url dan apikey.</p>

<pre>
	<code>
#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_ameldev_tiktokprodownloader_utils_Keys_getApiKey(
        JNIEnv* env,
        jclass clazz) {
    std::string apiKey = ""; // ubah apikey disini
    return env->NewStringUTF(apiKey.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_ameldev_tiktokprodownloader_utils_Keys_getApiURL(
        JNIEnv* env,
        jclass clazz) {
    std::string apiUrl = ""; // ubah api url disini
    return env->NewStringUTF(apiUrl.c_str());
}</code>
</pre>

<p>Pastikan hasil json nya seperti ini</p>
<pre>
	<code>
{
    "server": 1,
    "title": "I like the new trend in Indonesia \ud83c\uddee\ud83c\udde9 \u2665 #sia_jiwoo #\ud83c\uddf0\ud83c\uddf7",
    "url": "https://v16m.tiktokcdn.com/04b753a52fc24be1b98ecfa48cdf8363/6161c79c/video/tos/alisg/tos-alisg-pve-0037/68903018fdd94613ba76e9054d1b5b6f/?a=1233&br=6444&bt=3222&cd=0%7C0%7C0&ch=0&cr=0&cs=0&cv=1&dr=0&ds=3&er=&ft=wZmd9FLkkug3-I&l=2021100910471101019018513103025EBB&lr=all&mime_type=video_mp4&net=0&pl=0&qs=0&rc=am40cTc6ZjR3NzMzODgzNEApOGhkMzs5ZTxkNzk0NTk6Z2c1azJgcjRfYV5gLS1kLy1zczNeLTM0MS1iXmIzYjJgMzQ6Yw%3D%3D&vl=&vr=",
    "thumbnail": "https://p16-sign-sg.tiktokcdn.com/obj/tos-alisg-p-0037/57eaaed9419f4078b8c654b59b4be0e6_1628821310?x-expires=1633795200&x-signature=KMy3JHc3d8km67REVZ3I5IUJ2u0%3D",
    "music": "Boka Dance",
    "music_thumb": "https://p16-sign-va.tiktokcdn.com/tos-useast2a-avt-0068-aiso/ea31ef7129afbb429f1fbf90720004c7~c5_100x100.webp?x-expires=1633860000&x-signature=wAvTGxkd7U1GJ9ADSP7nztO8Cn0%3D",
    "music_url": null,
    "username": "sia_jiwoo",
    "nickname": "\uc2dc\uc544 SiA",
    "avatar": "https://p16-sign-sg.tiktokcdn.com/aweme/100x100/tos-alisg-avt-0068/76a822615e84b8762f2f1994d9e130e3.webp?x-expires=1633860000&x-signature=LBZR%2BIWdvRNmv9P%2FOpS46J4YxVs%3D",
    "code": "200"
}</code>
</pre>
