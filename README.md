<div align="center">

# 🌐 Lan Server Properties (Minecraft 26.2 Fabric)

**Enhanced Minecraft Multiplayer & LAN Server Options for Fabric 26.2.**  
**专为 Minecraft 26.2 + Fabric 打造的现代化自定义局域网联机增强模组。**

[![License: CC0-1.0](https://img.shields.io/badge/License-CC0%201.0-lightgrey.svg)](https://creativecommons.org/publicdomain/zero/1.0/)
[![Minecraft](https://img.shields.io/badge/Minecraft-26.2-brightgreen.svg)](https://www.minecraft.net/)
[![Fabric Loader](https://img.shields.io/badge/Fabric%20Loader-%3E%3D0.19.0-blue.svg)](https://fabricmc.net/)

[English](#english) | [中文说明](#中文说明)

</div>

---

<a name="english"></a>
## 🌐 English Description

### 📖 About This Fork
In Minecraft 26.2, Mojang overhauled the client multiplayer menu architecture (retiring the legacy `ShareToLanScreen` and introducing the modernized `MultiplayerOptionsScreen` with `HeaderAndFooterLayout`).  
As a result, older versions of Lan Server Properties (originally created by **Rikka0w0**) could not load on Minecraft 26.2.

This repository provides a dedicated, optimized **Minecraft 26.2 + Fabric Loader 0.19+** port while preserving all classic quality-of-life multiplayer features.

### 🌟 Key Features

* **📌 Fixed / Customizable Port**: Fixes the random LAN port assignment in vanilla Minecraft. You can set a fixed port (e.g. `25565`), making it effortless to set up port forwarding or tunnel tools (SakuraFrp, Tailscale, ZeroTier, Radmin VPN, etc.).
* **🔓 Offline Mode & Online Mode Toggle**:
  * **Online Mode (`正版验证`)**: Only authenticated Microsoft/Mojang accounts can join.
  * **Offline Mode (`离线模式`)**: Allows unauthenticated or offline accounts to join.
  * **Offline + UUID Fix (`离线+UUID修复`)**: Allows offline accounts while preserving official UUIDs and skin mappings, preventing player inventory and pet ownership loss.
* **⚔️ PvP Toggle**: Easily enable or disable friendly fire between players on the LAN screen.
* **👥 Custom Player Limit**: Break through vanilla's 8-player cap and configure your server for larger groups (e.g. 1-64+ players).
* **📋 One-Click Copy IP & Port**: Displays your local IP address and port directly on screen with a single button to copy it to clipboard.
* **💾 Preference Saving**: Save your preferred LAN configuration so you never have to re-enter settings on your next session.

### 📥 Installation & Usage

1. **Install Fabric**:
   * Requires **Minecraft 26.2** with **Fabric Loader 0.19.5+** and **Fabric API**.
2. **Download**:
   * Grab the latest `lanserverproperties-26.2-1.14.0-fabric.jar` from [Releases](https://github.com/S1ncerelyJay/LanServerProperties-26.2/releases).
3. **Install**:
   * Drop the `.jar` into your `.minecraft/mods/` directory.
4. **Open LAN Game**:
   * Press `ESC` -> **Multiplayer Options / 对局域网开放...**
   * Configure your desired port, online mode, and player limits, then click **Done / Start LAN World**.

---

<a name="中文说明"></a>
## 🇨🇳 中文说明

### 📖 为什么有这个版本？
在 Minecraft 26.2 版本中，Mojang 彻底重构了多人联机与局域网界面架构（废弃了原有的 `ShareToLanScreen`，改用全新的 `MultiplayerOptionsScreen` 与 `HeaderAndFooterLayout` 布局）。原作者 **Rikka0w0** 的原版模组因旧接口移除而无法在 26.2 上加载运行。

本项目针对 **Minecraft 26.2 + Fabric Loader 0.19+** 进行了完整重构与适配，完美还原并增强了原版局域网联机设置功能。

### 🌟 核心特性

* **📌 固定/自定义端口（联机工具必备神器）**：
  * 原版游戏每次开启局域网都会随机分配端口（如 53214），导致每次与异地好友联机时都必须重新配置端口映射或内网穿透规则；
  * 本模组允许您自由固定端口（如默认 `25565`），配合 **SakuraFrp、ZeroTier、Tailscale、蒲公英、Radmin VPN** 等虚拟局域网或穿透工具时，只需配置一次规则，永久无需更换！
* **🔓 关闭正版验证（离线与混合联机模式）**：
  * **正版验证（默认）**：仅允许正版微软账号登录加入。
  * **纯离线模式**：完全关闭 `online-mode` 验证，离线玩家可直接进入。
  * **离线+UUID修复（推荐）**：既允许离线玩家加入，又智能保留正版玩家的 UUID 映射，**防止因开启离线模式导致正版玩家背包物品、等级、末影箱及驯服宠物归属权丢失**。
* **⚔️ 自由切换 PvP**：在界面一键开启或关闭玩家互相伤害。
* **👥 自定义最大人数**：突破原版 8 人的死板限制，随意指定最大在线人数（1-64+）。
* **📋 一键复制 IP 与端口**：界面实时显示本机内网 IP 与监听端口，点击即可直接复制到剪贴板，方便快速粘贴给局域网伙伴。
* **💾 自动保存配置**：支持“保存为默认配置”并在每次开启局域网时自动载入，省时省力。

### 📥 安装与联机使用教程

#### 1. 安装模组
1. 确保游戏版本为 **Minecraft 26.2**，已安装 **Fabric Loader 0.19.5+** 与 **Fabric API**。
2. 前往本仓库的 [Releases](https://github.com/S1ncerelyJay/LanServerProperties-26.2/releases) 页面下载 `lanserverproperties-26.2-1.14.0-fabric.jar`。
3. 将下载的 `.jar` 文件放入 `.minecraft/mods` 文件夹内即可。

#### 2. 配合虚拟局域网 / 穿透工具联机步骤
1. 进入单人世界 -> 按 `ESC` 键呼出暂停菜单 -> 点击 **多人游戏选项...**（Minecraft 26.2 原版的局域网功能在此处）。
2. 在弹出的配置菜单中：
   * 将 **局域网公开** 切换为 **开启**；
   * 将 **在线模式** 切换为 **离线+UUID修复**（或按需选择）；
   * 将 **端口** 保持为固定的 `25565`（或穿透工具映射的内网端口）；
   * 点击 **保存为默认** -> 点击 **应用并返回**。
3. 远程好友通过虚拟局域网 IP（或穿透域名地址）连接：例如 `26.x.x.x:25565`，即可无缝秒进！

---

## 📜 开源协议与鸣谢 / License & Credits

* 本项目基于 [Rikka0w0/LanServerProperties](https://github.com/rikka0w0/LanServerProperties/) 进行 26.2 版本重构。
* 遵循与原项目一致的 **[CC0-1.0 Universal](LICENSE) (Public Domain / 公共领域贡献)** 开源协议。
* 感谢原作者 Rikka0w0 提供的优秀设计！
