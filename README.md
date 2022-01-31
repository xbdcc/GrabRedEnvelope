# GrabRedEnvelope
[![Gitter](https://badges.gitter.im/xbdcc/GrabRedEnvelope.svg)](https://gitter.im/xbdcc/GrabRedEnvelope?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
[![api](https://img.shields.io/badge/API-18+-brightgreen.svg)](https://android-arsenal.com/api?level=19)
[![Build Status](https://travis-ci.org/xbdcc/GrabRedEnvelope.svg?branch=master)](https://travis-ci.org/xbdcc/GrabRedEnvelope)

## APP下载地址及说明：
http://xbdcc.cn/GrabRedEnvelope/index.html

## 实现过程介绍：
https://www.jianshu.com/p/e1099a94b979

## 分析用到的命令相关
https://github.com/xbdcc/CCommand

## 使用到的工具库
https://github.com/xbdcc/CUtils


## Gif效果图
![gif1](http://xbdcc.cn/image/GrabRedEnvelope/records/record1.gif)
![gif2](http://xbdcc.cn/image/GrabRedEnvelope/records/record2.gif)

## 使用指南
为正常使用，建议APP权限和通知都打开。Android机型众多，可能有些机型效果好，有些没机型不方便适配的暂时还没适配。
- 辅助功能开启【抢微信红包】，若使用默认参数则通知或聊天列表页面或群聊发现有红包，则会自动点击红包并拆开跳转到详情页自动关闭。
- 通知监控开关说明：需要微信通知权限开启，此时若开启此功能收到通知有红包消息会自动跳转点击。
- 聊天列表页监控开关说明：若打开则在微信首页列表也发现红包消息则会跳转到详情页点击。
- 是否抢自己发的红包说明：若打开则自己发的红包也会去抢。
- 延迟时间说明：为防止秒抢拉仇恨加了延迟功能，延迟可以选择0-9秒。
- 自定义拆红包坐标点说明：自定义红包拆按钮的横纵坐标，解决部分Android7.0以上机型无法自动拆红包的问题。
- 3.3.0版本增加了自动发送表情功能，可实现聊天页连续放烟花的效果：支持设置自动发送文本，发送次数，发送间隔时间功能。
- 3.4.0版本增加不抢的群或人功能：设置后当前页面有设置的关键字则不会点击

最新版适配微信7.0.3,7.0.4,7.0.5,7.0.8,7.0.9,7.0.10,7.0.11,7.0.12,7.0.16,8.0.0,8.0.1,8.0.18,8.0.19版本，代码开源仅供学习使用，请勿用作商业用途。



## 打赏
<!--
<table>
    <tr>
        <td>
            <img src="http://xbdcc.cn/image/GrabRedEnvelope/wechat_reward.jpg" height="300"/>
        </td>
        <td>
            <img src="http://xbdcc.cn/image/GrabRedEnvelope/alipay.jpg" height="300"/>
        </td>
    </tr>
</table>
-->

如果觉得还不错，欢迎赏我吃碗热干面或奶茶支持继续，谢谢！

|微信赞赏|支付宝打赏|
|:-:|:-:|
|![微信赞赏](http://xbdcc.cn/image/GrabRedEnvelope/wechat_reward.jpg)|![支付宝打赏](http://xbdcc.cn/image/GrabRedEnvelope/alipay.jpg)|


## 版本说明
### v3.4.0(2022.01.31)
适配微信8.0.18,8.0.19，增加不抢的群或人功能。
### v3.3.0(2021.02.09)
适配微信8.0.0,8.0.1，增加自动发送表情功能。
### v3.2.0(2020.07.27)
适配微信7.0.16，增加是否抢自己发的红包开关。
### v3.1.0(2020.03.14)
适配微信7.0.11,7.0.12。
### v3.0.0(2020.01.22)
UI调整，功能完善，适配微信7.0.10。
### v2.2.0(2019.12.31)
适配微信7.0.8,7.0.9。
### v2.1.0(2019.07.21)
适配微信7.0.4,7.0.5，修复bug。
### v2.0.1(2019.03.04)
修复bug。
### v2.0.0(2019.02.24)
去掉抢QQ红包及支付宝咻一咻功能，加入抢微信红包功能。
### v1.0.4(2016.06.22)
修复bug。
### v1.0.3(2016.05.31)
修复Android高版本卡顿bug，新增红包记录功能。
### v1.0.2(2016.03.29)
优化功能。
### v1.0.1(2016.02.24)
修复抢QQ红包bug，完善推送功能。
### v1.0(2016.02.23)
实现抢QQ红包、QQ特殊口令生成和支付宝咻一咻功能。

## 基于本项目改写的项目
- https://github.com/gemgao/redenvelopes