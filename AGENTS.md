# 知芽 Zhiya — AGENTS.md

## 注意事项

- 每次改动完成后，都必须创建一个对应的 Git commit，以便后续追踪和回滚。
- 每次改动后，都必须编写或更新相关测试，并在交付给用户前，确保所有测试和验证全部通过。
- 开发软件及工具不要安装到C盘，可以统一安装 D:\DevTools。

## 目录结构

```
Zhiya/
├── AGENTS.md          # 本文件：开发规矩与目录结构说明
├── README.md          # 项目简介与目录索引
├── docs/              # 知识库（编号顺序即学习/实施顺序）
│   ├── 00-项目概述.md         # 名称含义、愿景、阶段规划
│   ├── 01-刷机原理.md         # vs 重装 Windows、分区结构、启动链、开机 logo 三阶段
│   ├── 02-刷机包与工具对照.md # 包类型、工具对照表、AOSP/HAL/blob 关系
│   ├── 03-Android安全模型.md  # UID 沙箱、userId 公式、Device Owner vs root
│   ├── 04-学习机实现方案.md   # Device Owner + 锁任务模式步骤、防绕过清单
│   ├── 05-机型选型.md         # 国产机型推荐与避雷、LineageOS 版本建议
│   ├── 06-自编译系统指南.md   # fork LineageOS 编译流程与定制点
│   └── 07-术语表.md           # 英中对照 + 音标 + 文中含义、缩略词
├── hardware/          # P2 硬件阶段产物
│   ├── schematics/   # 原理图
│   ├── pcb/         # PCB 工程
│   ├── bom/         # 物料清单
│   └── datasheets/  # 器件手册
├── system/            # P1 系统阶段产物
│   ├── device-tree/   # 机型 device tree（注明来源 commit）
│   ├── kernel-config/ # 内核配置
│   ├── build-scripts/ # 编译脚本（一键编译）
│   └── prebuilt/    # 预置资源：bootanimation、默认配置、预装 APP
├── flashing/          # 烧录阶段产物
│   ├── images/      # 各分区镜像（大文件走 releases 或网盘，不进 git）
│   ├── scripts/     # 刷机脚本（fastboot/adb）
│   └── splash/      # 开机 logo 资源：splash 分区镜像、bootanimation.zip
├── app/               # 学习机 APP（源码或外链说明）
├── tools/             # 通用工具脚本
└── releases/          # 各阶段发布产物归档，命名：zhiya-<阶段>-vX.Y.Z-<机型>-<日期>/
```

## 约定

- `docs/` 新增文档按编号续写，保持"原理 → 方案 → 实施"顺序。
- 大二进制（镜像、blob、PCB 导出）不进 git，进 `releases/` 归档或网盘，仓库里只留获取说明。
- 每次编译/烧录产物必须记录可复现信息：device tree commit、blob 版本、编译日期。
- 术语表（`docs/07-术语表.md`）持续维护：回复中给用户注释过的英文术语，顺手同步进去。
