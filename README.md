# Re:Telegram
An Xposed module to enhance the Telegram

[日本語](./README_ja-JP.md) | [简体中文](./README_zh-CN.md)

[![Release](https://img.shields.io/github/release/Sakion-Team/Re-Telegram.svg)](https://github.com/Sakion-Team/Re-Telegram/releases/latest)
[![CI_Build](https://github.com/Sakion-Team/Re-Telegram/actions/workflows/android.yml/badge.svg)](https://github.com/Sakion-Team/Re-Telegram/actions/workflows/android.yml)
[<img height="26" src="https://shields.io/badge/Release-ffffff.svg?style=flat-square&logo=telegram" alt="Release" />](https://t.me/Sakion_Team)

## FAQ

### What is the difference between this and Telegram Anti-Recall
Re:Telegram has more features than Telegram Anti-Recall

### What features does Re:Telegram have?
Currently, Re:Telegram has the following features: AntiAntiForward, AntiRecall, NoSponsoredMessages, ProhibitChannelSwitching, AllowMoveAllChatFolder, UseSystemTypeface, HideStories

### Which telegram client are supported?
Official, Plus Messenger, Nagram, Nnngram, NekoX, Nekogram (No Test Apk and Google Store Version), NekoLite, exteraGram, Forkgram, MDgram (Old version), Yukigram, iMoe, OctoGram, Mercurygram

### Which telegram client will not be supported?
Nullgram (You can use Nnngram), Telegram X, Forkgram F-Droid Build, Cherrygram (Developer requirements)

### What if the client i am using is not supported?
Submit the issue and include the client download link in the content, i will try to support your client.

### How to install
- Install APK file
- Enable the module in XPosed / LSposed
- Reboot

Main (AntiAntiForward, AntiRecall, NoSponsoredMessages, ProhibitChannelSwitching, AllowMoveAllChatFolder, UseSystemTypeface) features are already enabled by default, but to use HideStories you have to
- Use a text editor either on your device or ADB side loaded.
Locate a `/data/data/YOUR_TELEGRAM_CLIENT/` folder, likely `/data/data/org.telegram.messenger/`.\
  Its owner is a system user similar to `u0_a503`, so you will have to elevate your privileges to get into.\
There should be a `Re-Telegram/configs.cfg` file. Edit it to set `"HideStories": true` in this JSON file.\
Save and ensure that the owner and rights are the same, otherwise Re:Telegram will not access the file.
Force-close and reopen Telegram on your device.\
Stories should have gone.
