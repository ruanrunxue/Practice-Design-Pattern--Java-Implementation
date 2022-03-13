package com.yrunz.designpattern.sidecar;

import com.yrunz.designpattern.network.Socket;

/**
 * 工厂方法模式
 */

public interface SidecarFactory {
    Socket create();
}
