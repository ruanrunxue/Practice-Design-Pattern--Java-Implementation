package com.yrunz.designpattern.sidecar;

import com.yrunz.designpattern.network.Socket;

/**
 * 工厂模式
 */

public interface SidecarFactory {
    Socket create();
}
