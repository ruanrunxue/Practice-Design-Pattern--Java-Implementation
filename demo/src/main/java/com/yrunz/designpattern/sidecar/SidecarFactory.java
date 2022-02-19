package com.yrunz.designpattern.sidecar;

import com.yrunz.designpattern.network.Socket;

public interface SidecarFactory {
    Socket create();
}
