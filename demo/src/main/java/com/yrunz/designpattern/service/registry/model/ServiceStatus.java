package com.yrunz.designpattern.service.registry.model;

// 服务状态定义
public enum ServiceStatus {
    NORMAL,  // 正常状态，可对外提供服务
    FAULT,   // 故障状态，无法对外提供服务
    UNKNOWN   // 未知状态，心跳中断时导致，无法对外提供服务
}
