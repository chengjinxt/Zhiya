# 知芽 Zhiya

儿童学习专用设备：自己做硬件 → 自己编译 Android 系统 → 烧录自己的系统 → 安装自己的学习机软件，一条龙。

- 项目概述：`docs/00-项目概述.md`
- 开发规矩：`AGENTS.md`

## 目录结构

```
Zhiya/
├── AGENTS.md          # 开发规矩与目录结构说明
├── README.md          # 本文件
├── docs/              # 知识库：刷机原理、安全模型、实现方案、选型、编译指南、术语表
├── hardware/          # 硬件阶段产物
│   ├── schematics/   # 原理图
│   ├── pcb/         # PCB 工程
│   ├── bom/         # 物料清单
│   └── datasheets/  # 器件手册
├── system/            # 系统阶段产物
│   ├── device-tree/   # 机型 device tree
│   ├── kernel-config/ # 内核配置
│   ├── build-scripts/ # 编译脚本
│   └── prebuilt/    # 预置资源（bootanimation、默认配置、预装 APP）
├── flashing/          # 烧录阶段产物
│   ├── images/      # 各分区镜像
│   ├── scripts/     # 刷机脚本
│   └── splash/      # 开机 logo 资源（splash 分区镜像、bootanimation.zip）
├── app/               # 学习机 APP（源码或外链说明）
├── tools/             # 通用工具脚本
└── releases/          # 各阶段发布产物归档（按版本/日期）
```

## 当前阶段

P0 旧机验证：`docs/` 知识库已落地，待定第一台试验机型。
